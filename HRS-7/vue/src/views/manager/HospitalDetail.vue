<template>
  <div class="hospital-detail">
    <div v-if="isLoading" class="loading-text">加载中，请稍候...</div>
    <div v-else>
      <h1>{{ hospital.name }}</h1>
      <p><strong>医院地址:</strong> {{ hospital.address }}</p>
      <p><strong>联系电话:</strong> {{ hospital.phone }}</p>
      <p><strong>医院等级:</strong> {{ getHospitalLevel(hospital.level) }}</p>
      <p><strong>医院简介:</strong> {{ hospital.introduction }}</p>
      <p v-if="hospital.departments && hospital.departments.length > 0"><strong>科室列表:</strong></p>
      <ul v-if="hospital.departments">
        <li v-for="(department, index) in hospital.departments" :key="index">
          <!-- 使用 router-link 跳转到医生页面 -->
          <router-link :to="getDoctorRoute(department.id)">
            {{ department.name}}
          </router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'HospitalDetail',
  data() {
    return {
      hospital: {
        name: '',
        address: '',
        phone: '',
        level: '',
        introduction: '',
        departments: [] // 假设 departments 是一个数组，包含 id 和 name 字段
      },
      isLoading: true
    };
  },
  created() {
    this.fetchHospitalDetail();
  },
  methods: {
    fetchHospitalDetail() {
      const hospitalId = this.$route.params.id; // 从路由参数中获取医院 ID
      this.$request.get(`/hospital/selectAllData?id=${hospitalId}`)
          .then(response => {
            if (response.data) {
              this.hospital = response.data;
            } else {
              console.error('获取医院详情数据格式错误:', response.data);
              this.$message.error('获取医院详情失败，请稍后重试');
            }
          })
          .catch(error => {
            console.error('获取医院详情失败:', error);
            this.$message.error('获取医院详情失败，请稍后重试');
          })
          .finally(() => {
            this.isLoading = false;
          });
    },
    // 根据 level 值返回对应的等级名称
    getHospitalLevel(level) {
      const levelMap = {
        1: '1级丙等',
        2: '1级乙等',
        3: '1级甲等',
        4: '2级丙等',
        5: '2级乙等',
        6: '2级甲等',
        7: '3级丙等',
        8: '3级乙等',
        9: '3级甲等'
      };
      return levelMap[level] || '未知等级'; // 如果 level 不在映射表中，返回 '未知等级'
    },
    // 生成跳转到医生页面的路由
    getDoctorRoute(departmentId) {
      const hospitalId = this.$route.params.id; // 获取当前医院 ID
      return {
        name: 'Doctor', // 路由名称
        params: {
          hospitalId: hospitalId, // 传递医院 ID
          departmentId: departmentId // 传递科室 ID
        }
      };
    }
  }
};
</script>

<style scoped>
.hospital-detail {
  padding: 20px;
}

.loading-text {
  text-align: center;
  font-size: 16px;
  color: #666;
}
</style>