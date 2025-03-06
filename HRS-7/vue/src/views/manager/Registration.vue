<template>
  <div class="registration-page">
    <div class="doctor-info" v-if="doctor">
      <h2>{{ doctor.name }} - {{ getDoctorTitle(doctor.status) }}</h2>
      <p>{{ doctor.introduction }}</p>
    </div>
    <div class="registration-info" v-if="doctor">
      <h2>挂号信息</h2>
      <ul>
        <!-- 上午时间段 -->
        <li v-for="(timeSlot, index) in morningTimeSlots" :key="index">
          <router-link
              :to="getRegistrationRoute(getDates()[0], timeSlot)"
              class="registration-link"
          >
            {{ getDates()[0] }} - {{ timeSlot }} - 价格：{{ getPrice(doctor.status, timeSlot) }} 元
          </router-link>
        </li>
        <!-- 下午时间段 -->
        <li v-for="(timeSlot, index) in afternoonTimeSlots" :key="index + 100">
          <router-link
              :to="getRegistrationRoute(getDates()[0], timeSlot)"
              class="registration-link"
          >
            {{ getDates()[0] }} - {{ timeSlot }} - 价格：{{ getPrice(doctor.status, timeSlot) }} 元
          </router-link>
        </li>
      </ul>
    </div>
    <div v-else>
      <p>未获取到医生信息，请稍后重试。</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Registration',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      doctor: null,
      isLoading: true,
      morningTimeSlots: ['上午 9-10点', '上午 10-11点', '上午 11-12点'], // 上午时间段
      afternoonTimeSlots: ['下午 2-3点', '下午 3-4点', '下午 4-5点', '下午 5-6点'] // 下午时间段
    };
  },
  created() {
    const doctorId = this.$route.params.doctorId;
    if (doctorId) {
      this.fetchDoctorInfo(doctorId);
    } else {
      this.isLoading = false;
    }
  },
  methods: {
    fetchDoctorInfo(doctorId) {
      this.$request.get('/doctor/selectDoctorById', {
        params: { doctorId }
      })
          .then(response => {
            if (response.data) {
              this.doctor = response.data;
            } else {
              console.error('获取医生详细信息失败:', response.data);
              this.$message.error('获取医生详细信息失败，请稍后重试');
            }
          })
          .catch(error => {
            console.error('获取医生详细信息失败:', error);
            this.$message.error('获取医生详细信息失败，请稍后重试');
          })
          .finally(() => {
            this.isLoading = false;
          });
    },
    getRegistrationRoute(date, timeSlot) {
      return {
        name: 'SubmitRegistration', // 提交挂号界面的路由名称
        query: {
          doctorId: this.$route.params.doctorId, // 医生 ID
          date: date, // 挂号日期
          timeSlot: timeSlot, // 具体时间段（如 "上午 9-10点"）
          price: this.getPrice(this.doctor.status, timeSlot) // 挂号价格
        }
      };
    },
    getDoctorTitle(status) {
      switch (status) {
        case 1:
          return '医师';
        case 2:
          return '副主治医师';
        case 3:
          return '主治医师';
        case 4:
          return '专家';
        case 5:
          return '急诊';
        default:
          return '未知职称';
      }
    },
    getDates() {
      const today = new Date();
      const year = today.getFullYear();
      const month = String(today.getMonth() + 1).padStart(2, '0');
      const day = String(today.getDate()).padStart(2, '0');
      return [`${year}-${month}-${day}`];
    },
    getPrice(status, timeSlot) {
      // 这里可以根据职称和时间段设置不同的价格
      const isMorning = timeSlot.includes('上午');
      switch (status) {
        case 1:
          return isMorning ? 50 : 40;
        case 2:
          return isMorning ? 80 : 70;
        case 3:
          return isMorning ? 100 : 90;
        case 4:
          return isMorning ? 150 : 120;
        case 5:
          return isMorning ? 60 : 50;
        default:
          return 0;
      }
    }
  }
};
</script>

<style scoped>
.registration-page {
  padding: 20px;
}

.doctor-info {
  height: 30%;
  border-bottom: 1px solid #ccc;
  padding-bottom: 20px;
}

.registration-info {
  height: 70%;
  padding-top: 20px;
}

.registration-link {
  display: block;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-bottom: 10px;
  text-decoration: none;
  color: #333;
  transition: background-color 0.3s ease;
}

.registration-link:hover {
  background-color: #f0f0f0;
}
</style>