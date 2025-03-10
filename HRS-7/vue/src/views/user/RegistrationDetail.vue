<template>
  <div class="order-detail-page">
    <h2>订单详情</h2>
    <div v-if="isLoading">
      <p>正在加载订单详情，请稍候...</p>
    </div>
    <div v-else-if="registrationDetail">
      <!-- 修改为使用 registrationDetail 对象 -->
      <p>订单编号：{{ registrationDetail.order }}</p>
      <p>医院：{{ registrationDetail.hospitalName }}</p>
      <p>科室：{{ registrationDetail.departmentName }}</p>
      <p>医生姓名：{{ registrationDetail.doctorName }}</p>
      <p>挂号时间：{{ registrationDetail.registerTime }}</p>
      <p>订单价格：{{ registrationDetail.price }} 元</p>
      <p>用户姓名：{{ registrationDetail.userName }}</p>
      <p>用户性别：{{ registrationDetail.userGender }}</p>
      <p>用户联系方式：{{ registrationDetail.userPhone }}</p>
      <p>订单提交日期：{{ registrationDetail.orderTime }}</p>
    </div>
    <div v-else>
      <p>获取订单详情失败，请稍后重试。</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'OrderDetail',
  data() {
    return {
      // 修改为 registrationDetail
      registrationDetail: null,
      isLoading: true
    };
  },
  created() {
    this.fetchOrderDetail();
  },
  methods: {
    fetchOrderDetail() {
      const orderId = Number(this.$route.query.order);
      if (!orderId) {
        this.handleError('订单号参数缺失', null);
        this.isLoading = false;
        return;
      }
      this.$request.get('/registration/detail', {
        params: {
          orderId: orderId
        }
      })
          .then(response => {
            if (response.data) {
              // 修改为赋值给 registrationDetail
              this.registrationDetail = response.data;
            } else {
              this.handleError('获取订单详情失败', response.data);
            }
          })
          .catch(error => {
            this.handleError('获取订单详情失败', error);
          })
          .finally(() => {
            this.isLoading = false;
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
.order-detail-page {
  padding: 20px;
}
</style>