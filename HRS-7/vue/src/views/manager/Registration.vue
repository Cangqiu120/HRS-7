<template>
  <div class="registration-page">
    <div class="doctor-info" v-if="doctor">
      <h2>{{ doctor.name }} - {{ getDoctorTitle(doctor.status) }}</h2>
      <p>{{doctor.introduction}}</p>
    </div>
    <div class="registration-info" v-if="doctor">
      <h2>挂号信息</h2>
      <ul>
        <li v-for="(date, index) in getDates()" :key="index">
          {{ date }} - 上午 - 价格：{{ getPrice(doctor.status, '上午') }} 元
        </li>
        <li v-for="(date, index) in getDates()" :key="index + 100">
          {{ date }} - 下午 - 价格：{{ getPrice(doctor.status, '下午') }} 元
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
      doctor: null,
      isLoading: true
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
    getPrice(status, time) {
      // 这里可以根据职称和上下午设置不同的价格
      switch (status) {
        case 1:
          return time === '上午' ? 50 : 40;
        case 2:
          return time === '上午' ? 80 : 70;
        case 3:
          return time === '上午' ? 100 : 90;
        case 4:
          return time === '上午' ? 150 : 120;
        case 5:
          return time === '上午' ? 60 : 50;
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
</style>