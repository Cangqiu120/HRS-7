<template>
  <div class="registration-detail">
    <h2>挂号详情</h2>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">加载中...</div>

    <!-- 错误提示 -->
    <div v-if="error" class="error">{{ error }}</div>

    <!-- 详情信息 -->
    <div v-if="detail" class="detail-info">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="订单号">{{ detail.order }}</el-descriptions-item>
        <el-descriptions-item label="医院名称">{{ detail.hospitalName }}</el-descriptions-item>
        <el-descriptions-item label="科室名称">{{ detail.departmentName }}</el-descriptions-item>
        <el-descriptions-item label="医生姓名">{{ detail.doctorName }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ detail.orderTime }}</el-descriptions-item>
        <el-descriptions-item label="预约时间">{{ detail.registerTime }}</el-descriptions-item>
        <el-descriptions-item label="挂号班次">{{ detail.shiftType === 'DAY' ? '白班' : '夜班' }}</el-descriptions-item>
        <el-descriptions-item label="费用">{{ detail.price }} 元</el-descriptions-item>
        <el-descriptions-item label="患者姓名">{{ detail.userName }}</el-descriptions-item>
        <el-descriptions-item label="患者性别">{{ detail.userGender }}</el-descriptions-item>
        <el-descriptions-item label="患者手机号">{{ formatPhone(detail.userPhone) }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">{{ formatStatus(detail.status) }}</el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- 返回按钮 -->
    <div class="actions">
      <el-button type="primary" @click="handleBack">返回</el-button>
      <!-- 添加申诉按钮，当 status 为 3 时显示 -->
      <el-button v-if="detail && detail.status === 3" type="warning" @click="handleAppeal">申诉</el-button>
      <el-button v-if="detail && detail.status === 3" type="success" @click="handleReferralConfirm1">当日复诊</el-button>
      <el-button v-if="detail && detail.status === 3" type="success" @click="handleReferralConfirm2">预约复诊</el-button>
    </div>
  </div>
</template>

<script>
import {parseTime} from "@/utils";

export default {
  name: 'SelectRegistrationDetail',
  data() {
    return {
      detail: null, // 挂号详情信息
      loading: false, // 加载状态
      error: '' // 错误信息
    };
  },
  created() {
    this.fetchDetail(); // 初始化加载详情信息
  },
  methods: {
    // 获取挂号详情信息
    fetchDetail() {
      this.loading = true;
      this.error = '';
      const id = this.$route.params.id; // 从路由参数中获取挂号 ID
      console.log(id);
      this.$request.get('/registration/detail', {
        params: { orderId: id }
      })
          .then(response => {
            if (response.data) {
              this.detail = response.data;
            } else {
              this.error = response.data.message || '获取详情失败';
            }
          })
          .catch(error => {
            console.error('获取挂号详情失败:', error);
            this.error = '获取挂号详情失败，请稍后重试';
          })
          .finally(() => {
            this.loading = false;
          });
    },

    // 格式化手机号（脱敏显示）
    formatPhone(phone) {
      if (phone && phone.length === 11) {
        return phone.substring(0, 3) + '****' + phone.substring(7);
      }
      return phone;
    },

    // 格式化订单状态
    formatStatus(status) {
      switch (status) {
        case 0:
          return '待问诊';
        case 1:
          return '已问诊';
        case 2:
          return '已开药';
        case 3:
          return '已完成';
        case 4:
          return '已取消';
        case 5:
          return '复诊';
        default:
          return '未知状态';
      }
    },

    // 返回上一页
    handleBack() {
      this.$router.go(-1);
    },

    // 申诉按钮点击事件处理函数
    handleAppeal() {
      console.log('点击了申诉按钮');
      this.$confirm('确定选择申诉吗', '申诉', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
          .then(() => {
            this.$router.push({
              name: 'Appeal',
              query: {
                orderId: this.detail.order
              }
            })
          })
          .catch(() => {
          });
    },

    // 当日复诊确认
    handleReferralConfirm1() {
      console.log('点击了当日复诊确认按钮');
      this.$confirm('确定选择当日复诊吗', '当日复诊', {
        confirmButtonText: '当日复诊',
        cancelButtonText: '取消',
        type: 'warning'
      })
          .then(() => {
            // 点击确定，执行当日复诊逻辑
            this.handleImmediateReferral();
          })
          .catch(() => {
          });
    },

    // 预约复诊确认
    handleReferralConfirm2() {
      console.log('点击了预约复诊确认按钮');
      this.$confirm('确定选择预约复诊吗', '预约复诊', {
        confirmButtonText: '预约复诊',
        cancelButtonText: '取消',
        type: 'warning'
      })
          .then(() => {
            // 点击确定，执行当日复诊逻辑
            this.handleScheduledReferral();
          })
          .catch(() => {
          });
    },

    // 当日复诊逻辑
    handleImmediateReferral() {
      console.log('点击了当日复诊按钮');
      this.$router.push({
        name: 'Registration',
        query: {
          doctorName: this.detail.doctorName,
          hospitalName: this.detail.hospitalName,
          departmentName: this.detail.departmentName,
          isRevisit: true,
          isImmediate: true, // 标识这是当日复诊操作
          status: 5
        }
      });
    },

    // 预约复诊逻辑
    handleScheduledReferral() {
      console.log('点击了预约复诊按钮');
      this.$router.push({
        name: 'Registration',
        query: {
          doctorName: this.detail.doctorName,
          isRevisit: true,
          isImmediate: false,
          status: 5
        }
      });
    }
  }
};
</script>

<style scoped>
.registration-detail {
  padding: 20px;
}

.loading,
.error {
  text-align: center;
  margin-top: 20px;
  font-size: 16px;
  color: #666;
}

.error {
  color: #f56c6c;
}

.detail-info {
  margin-top: 20px;
}

.actions {
  margin-top: 20px;
  text-align: center;
}
</style>