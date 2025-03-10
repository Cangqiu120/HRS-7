<template>
  <div class="submit-registration-page">
    <h2>提交挂号信息</h2>
    <div v-if="isRevisit">
<!--      复诊-->
      <p>医生姓名：{{ doctorName }}</p>
      <p>挂号日期：{{ registrationDate }}</p>
      <p>时间段：{{ registrationTimeSlot }}</p>
      <p>价格：{{ registrationPrice }} 元</p>
      <p>用户：{{ nowUserName }}</p>
      <p>用户联系方式：{{ nowUserPhone }}</p>
      <!-- 添加确认提交按钮 -->
      <button @click="submitRegistration">确认提交</button>

    </div>
    <div v-else>
      <div v-if="doctor">
        <p>医生姓名：{{ doctor.name }}</p>
        <p>挂号日期：{{ registrationDate }}</p>
        <p>时间段：{{ registrationTimeSlot }}</p>
        <p>价格：{{ registrationPrice }} 元</p>
        <p>用户：{{ nowUser.name }}</p>
        <p>用户联系方式：{{ nowUser.phone }}</p>
        <!-- 添加确认提交按钮 -->
        <button @click="submitRegistration">确认提交</button>
      </div>
      <div v-else>
        <p>未获取到医生信息，请稍后重试。</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SubmitRegistration',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      doctor: null,
      nowUser: null,
      registrationDate: this.$route.query.date,
      registrationTimeSlot: this.$route.query.timeSlot,
      registrationPrice: this.$route.query.price,
      isLoading: true,
      isRevisit: this.$route.query.isRevisit,
      doctorName: this.$route.query.doctorName,
      nowUserName: this.$route.query.userName,
      nowUserPhone: this.$route.query.userPhone
    };
  },
  created() {
    const doctorId = this.$route.query.doctorId;
    const userId = this.user.id;
    const isRevisit = this.$route.query.isRevisit;

    // 检查日期和时间段是否有效
    if (!this.registrationDate ||!this.registrationTimeSlot) {
      console.error('日期或时间段参数为空');
      this.$message.error('日期或时间段参数为空，请检查后重试');
      return;
    }

    if(isRevisit){
      //复诊

    }else{
      //挂号
      if (doctorId) {
        this.fetchDoctorInfo(doctorId);
      } else {
        this.isLoading = false;
      }
      this.fetchUserInfo(userId);
      console.log('当前用户信息:', this.user);
    }
  },
  methods: {
    fetchDoctorInfo(doctorId) {
      this.isLoading = true;
      this.$request.get('/doctor/selectDoctorById', {
        params: { doctorId }
      })
          .then(response => {
            if (response.data) {
              this.doctor = response.data;
            } else {
              this.handleError('获取医生详细信息失败', response.data);
            }
          })
          .catch(error => {
            this.handleError('获取医生详细信息失败', error);
          })
          .finally(() => {
            this.isLoading = false;
          });
    },
    fetchUserInfo(userId) {
      this.isLoading = true;
      const errorMessage = '获取用户详细信息失败，请稍后重试';
      this.$request.get(`/user/selectById/${userId}`)
          .then(response => {
            if (response.data) {
              this.nowUser = response.data;
            } else {
              this.handleError(errorMessage, response.data);
            }
          })
          .catch(error => {
            this.handleError(errorMessage, error);
          })
          .finally(() => {
            this.isLoading = false;
          });
    },
    submitRegistration() {
      console.log('提交挂号信息:', {
        doctorId: this.doctor.id,
        userId: this.nowUser.id,
        registrationDate: this.registrationDate,
        registrationTimeSlot: this.registrationTimeSlot,
        registrationPrice: this.registrationPrice
      });
      this.$request.post('/registration/insert', {
        doctorId: this.doctor.id,
        userId: this.nowUser.id,
        registrationDate: this.registrationDate,
        registrationTimeSlot: this.registrationTimeSlot,
        registrationPrice: this.registrationPrice
      })
          .then(response => {
            // 从响应数据中提取 register 对象的 order 属性
            const order = response.data && response.data.order;
            this.$message.success('挂号信息提交成功');
            // 跳转到成功页面并传递相关数据，包括 order 属性
            this.$router.push({
              name: 'RegistrationSuccess',
              query: {
                order: order
              }
            });
          })
          .catch(error => {
            this.handleError('提交挂号信息失败', error);
          });
    },
    handleError(message, error) {
      console.error(message, error);
      this.$message.error(`${message}，请稍后重试`);
    }
  }
};
</script>

<style scoped>
.submit-registration-page {
  padding: 20px;
}
</style>