<template>
  <div class="app-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>医生排班管理</span>
        <el-button
            style="float: right; padding: 3px 0"
            type="text"
            @click="showNextWeek"
        >
          排下一周班次
        </el-button>
      </div>

      <el-row :gutter="20">
        <el-col :span="6">
          <el-card>
            <div slot="header" class="clearfix">
              <span>科室列表</span>
            </div>
            <el-tree
                :data="departments"
                :props="defaultProps"
                @node-click="handleDepartmentClick"
            ></el-tree>
          </el-card>
        </el-col>

        <el-col :span="18">
          <el-card>
            <div slot="header" class="clearfix">
              <span>排班表 - {{ selectedDepartment ? selectedDepartment.name : '请选择科室' }}</span>
              <div style="float: right">
                <el-date-picker
                    v-model="currentWeek"
                    type="week"
                    format="yyyy 第 WW 周"
                    placeholder="选择周"
                    @change="fetchSchedules"
                >
                </el-date-picker>
              </div>
            </div>

            <el-table
                v-loading="loading"
                :data="scheduleData"
                border
                style="width: 100%"
            >
              <el-table-column prop="doctorName" label="医生" width="150" fixed>
                <template slot-scope="scope">
                  {{ scope.row.doctorName }}
                </template>
              </el-table-column>
              <el-table-column
                  v-for="day in weekDays"
                  :key="day.date"
                  :label="day.label"
                  width="150"
              >
                <template slot-scope="scope">
                  <el-select
                      v-model="scope.row.schedules[day.date]"
                      placeholder="班次"
                      size="mini"
                      @change="handleShiftChange(scope.row.doctorId, day.date, $event)"
                  >
                    <el-option label="白班" value="DAY"></el-option>
                    <el-option label="夜班" value="NIGHT"></el-option>
                    <el-option label="休息" :value="null"></el-option>
                  </el-select>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import {
  getSchedulesByDepartment,
  addSchedule,
  updateSchedule,
  deleteSchedule
} from '@/api/scheduleApi'
import { parseTime } from '@/utils'

export default {
  name: 'Schedule',
  data() {
    return {
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'),
      loading: false,
      hospitalId: null,
      departments: [],
      selectedDepartment: null,
      doctors: [],
      currentWeek: this.getNextWeekStartDate(),
      weekDays: [],
      scheduleData: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  created() {
    this.fetchDepartments()
    this.generateWeekDays()
  },
  methods: {
    getNextWeekStartDate() {
      const today = new Date()
      const nextMonday = new Date(today)
      nextMonday.setDate(today.getDate() + ((1 + 7 - today.getDay()) % 7 || 7))
      return nextMonday
    },
    generateWeekDays() {
      const startDate = new Date(this.currentWeek)
      this.weekDays = []
      for (let i = 0; i < 7; i++) {
        const date = new Date(startDate)
        date.setDate(startDate.getDate() + i)
        this.weekDays.push({
          date: parseTime(date, '{y}-{m}-{d}'),
          label: parseTime(date, '{m}-{d} 周') + ['日', '一', '二', '三', '四', '五', '六'][date.getDay()]
        })
      }
    },
    fetchDepartments() {
      this.$request.get('/hospital/departmentList', {
        params: {
          userId: this.user.id
        }
      }).then(response => {
        if (response.code == 200) {
          this.departments = response.data
        }else {
          this.$message.error(response.data.message || '获取科室列表失败')
        }
      })
    },

    async handleDepartmentClick(data) {
      this.selectedDepartment = data
      await this.fetchDoctors(data.id)
      await this.fetchSchedules()
    },

    fetchDoctors(departmentId) {
      this.$request.get('/doctor/selectDocByAdminIdAndDepId', {
        params: {
           userId: this.user.id,
          departmentId: departmentId
        }
      }).then(response => {
        if (response.code == 200) {
          this.doctors = response.data
          this.prepareScheduleData()
        }else {
          this.$message.error(response.data.message || '获取医生列表失败')
        }
      })
    },

    prepareScheduleData() {
      this.scheduleData = this.doctors.map(doctor => {
        const schedules = {}
        this.weekDays.forEach(day => {
          schedules[day.date] = null
        })
        return {
          doctorId: doctor.id,
          doctorName: doctor.name,
          schedules: schedules
        }
      })
    },
    async fetchSchedules() {
      if (!this.selectedDepartment) return

      this.loading = true
      this.generateWeekDays()
      this.prepareScheduleData()

      try {
        const startDate = new Date(this.currentWeek)
        const endDate = new Date(startDate)
        endDate.setDate(startDate.getDate() + 6)

        const response = await getSchedulesByDepartment(
            this.user.id,
            this.selectedDepartment.id,
            startDate,
            endDate
        )

        // 更新排班数据
        response.data.forEach(schedule => {
          const scheduleDate = parseTime(schedule.scheduleDate, '{y}-{m}-{d}')
          const doctorSchedule = this.scheduleData.find(
              item => item.doctorId === schedule.doctorId
          )
          if (doctorSchedule) {
            doctorSchedule.schedules[scheduleDate] = schedule.shift_type
          }
        })
      } catch (error) {
        console.error('获取排班信息失败:', error)
      } finally {
        this.loading = false
      }
    },
    async handleShiftChange(doctorId, date, shiftType) {
      try {
        // 首先查找是否已有排班记录
        const existingSchedule = await this.findExistingSchedule(doctorId, date)

        if (shiftType) {
          // 有班次选择
          if (existingSchedule) {
            // 更新现有排班
            await updateSchedule({
              id: existingSchedule.id,
              shift_type: shiftType
            })
          } else {
            // 新增排班
            await addSchedule({
              hospitalId: this.user.id,
              departmentId: this.selectedDepartment.id,
              doctorId: doctorId,
              scheduleDate: date,
              shiftType: shiftType,
              fatigueScore: 0
            })
          }
        } else {
          // 选择休息，删除排班记录
          if (existingSchedule) {
            await deleteSchedule(existingSchedule.id)
          }
        }

        this.$message.success('排班更新成功')
      } catch (error) {
        console.error('更新排班失败:', error)
        this.$message.error('排班更新失败')
      }
    },
    async findExistingSchedule(doctorId, date) {
      try {
        const response = await getSchedulesByDepartment(
            this.user.id,
            this.selectedDepartment.id,
            new Date(date),
            new Date(date)
        )
        return response.data.find(
            item => item.doctorId === doctorId &&
                parseTime(item.scheduleDate, '{y}-{m}-{d}') === date
        )
      } catch (error) {
        console.error('查找排班记录失败:', error)
        return null
      }
    },
    showNextWeek() {
      const nextWeek = new Date(this.currentWeek)
      nextWeek.setDate(nextWeek.getDate() + 7)
      this.currentWeek = nextWeek
      this.fetchSchedules()
    }
  }
}
</script>

<style scoped>
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}
</style>