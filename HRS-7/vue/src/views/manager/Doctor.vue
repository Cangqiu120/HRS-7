<template>
  <div class="doctor-page">
    <h1>医生列表</h1>
    <div v-if="isLoading" class="loading-text">加载中，请稍候...</div>
    <div v-else>
      <ul>
        <li v-for="(doctor, index) in doctors" :key="index" @click="goToRegistration(doctor.id)">
          {{ doctor.name }} - {{ getDoctorTitle(doctor.status) }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Doctor',
  data() {
    return {
      doctors: [], // 医生列表
      isLoading: true
    };
  },
  created() {
    this.fetchDoctors();
  },
  methods: {
    fetchDoctors() {
      const hospitalId = this.$route.params.hospitalId; // 获取医院 ID
      const departmentId = this.$route.params.departmentId; // 获取科室 ID
      console.log('hospitalId:', hospitalId);
      console.log('departmentId:', departmentId);

      // 调用 doctor/selectdoc 接口
      this.$request.get('/doctor/selectDoc', {
        params: {
          hospitalId: hospitalId,
          departmentId: departmentId
        }
      })
          .then(response => {
            if (response.data) {
              this.doctors = response.data; // 假设接口返回的数据是医生列表
            } else {
              console.error('获取医生列表数据格式错误:', response.data);
              this.$message.error('获取医生列表失败，请稍后重试');
            }
          })
          .catch(error => {
            console.error('获取医生列表失败:', error);
            this.$message.error('获取医生列表失败，请稍后重试');
          })
          .finally(() => {
            this.isLoading = false;
          });
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
    goToRegistration(doctorId) {
      this.$router.push({
        name: 'Registration',
        params: { doctorId }
      });
    }
  }
};
</script>

<style scoped>
.doctor-page {
  padding: 20px;
}

.loading-text {
  text-align: center;
  font-size: 16px;
  color: #666;
}

li {
  cursor: pointer;
}
</style>