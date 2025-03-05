<template>
  <div class="hospital-container">
    <!-- 顶部横幅 -->
    <div class="hospital-header">
      <div class="hospital-header-left">
        <img src="@/assets/imgs/logo.png" alt="医院 logo" />
        <div class="title">非著名的沧秋医院</div>
      </div>
      <div class="hospital-header-right">
        <!-- 根据用户是否登录显示不同内容 -->
        <template v-if="user.id">
          <!-- 登录后显示头像 -->
          <img :src="user.avatar" alt="用户头像" class="user-avatar" @click="goToPerson">
        </template>
        <template v-else>
          <!-- 未登录显示登录按钮 -->
          <el-button type="text" @click="$router.push('/login')">登录</el-button>
        </template>
      </div>
    </div>

    <!-- 导航栏 -->
    <div class="hospital-nav">
      <el-menu mode="horizontal" :default-openeds="[]" router style="border: none" :default-active="$route.path">
        <!-- 首页 -->
        <el-menu-item index="/home">
          <template #title>
            <i class="el-icon-date"></i>
            <span>首页</span>
          </template>
        </el-menu-item>

        <!-- 挂号 -->
        <el-submenu index="register">
          <template #title>
            <i class="el-icon-medal"></i>
            <span>挂号</span>
          </template>
          <el-menu-item index="/appointment-registration/reservation">预约挂号</el-menu-item>
          <el-menu-item index="/notice">当日挂号</el-menu-item>
        </el-submenu>

        <!-- 特色诊疗 -->
        <el-submenu index="special-treatment">
          <template #title>
            <i class="el-icon-medal"></i>
            <span>特色诊疗</span>
          </template>
          <el-menu-item index="/special-treatment/difficult-consultation">疑难会诊中心</el-menu-item>
          <el-menu-item index="/special-treatment/joint-clinic">联合门诊</el-menu-item>
          <el-menu-item index="/special-treatment/special-departments">特色科室</el-menu-item>
          <el-menu-item index="/special-treatment/special-diseases">专科专病</el-menu-item>
        </el-submenu>

        <!-- 医生介绍 -->
        <el-submenu index="doctor-introduction">
          <template #title>
            <i class="el-icon-user-solid"></i>
            <span>医生介绍</span>
          </template>
          <!-- 可添加医生介绍的子菜单 -->
          <el-menu-item index="/doctor-introduction/all-doctors">全部医生</el-menu-item>
        </el-submenu>

        <!-- 科室导航 -->
        <el-submenu index="department-introduction">
          <template #title>
            <i class="el-icon-office-building"></i>
            <span>科室导航</span>
          </template>
          <!-- 可添加科室导航的子菜单 -->
          <el-menu-item index="/department-introduction/department-list">科室列表</el-menu-item>
        </el-submenu>

        <!-- 就医指南 -->
        <el-menu-item index="/medical-guide">
          <template #title>
            <i class="el-icon-guide"></i>
            <span>就医指南</span>
          </template>
        </el-menu-item>

        <!-- 医院公告 -->
        <el-menu-item index="/hospital-announcement">
          <template #title>
            <i class="el-icon-bell"></i>
            <span>医院公告</span>
          </template>
        </el-menu-item>

        <!-- 新闻中心 -->
        <el-submenu index="news-center">
          <template #title>
            <i class="el-icon-news"></i>
            <span>新闻中心</span>
          </template>
          <el-menu-item index="/news-center/major-news">重大新闻</el-menu-item>
          <el-menu-item index="/news-center/hospital-dynamics">医院动态</el-menu-item>
          <el-menu-item index="/news-center/medical-frontier">医学前沿</el-menu-item>
          <el-menu-item index="/news-center/video-news">视频新闻</el-menu-item>
        </el-submenu>

        <!-- 健康科普 -->
        <el-submenu index="health-popularization">
          <template #title>
            <i class="el-icon-health"></i>
            <span>健康科普</span>
          </template>
          <!-- 可添加健康科普的子菜单 -->
          <el-menu-item index="/health-popularization/general-knowledge">科普常识</el-menu-item>
        </el-submenu>
      </el-menu>
    </div>

    <!-- 主体内容 -->
    <div class="hospital-main">
      <!-- 侧边栏 -->
      <div class="hospital-main-left">
        <!-- 可以根据需要添加侧边栏内容 -->
        <!-- 增加占位内容 -->
      </div>

      <!-- 数据表格 -->
      <div class="hospital-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>

    <!-- 底部信息 -->
    <div class="hospital-footer">
      <p>联系电话：010 - 20040520</p>
      <p>地址：天津市 XX 区 找得到算你厉害</p>
      <p>版权所有 &copy; 2025 沧秋sir</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "HospitalManager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    };
  },
  created() {
    if (!this.user.id) {
      this.$router.push('/login');
    }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}'); // 重新获取下用户的最新信息
    },
    goToPerson() {
      if (this.user.role === 'ADMIN') {
        this.$router.push('/adminPerson');
      }
    },
    logout() {
      localStorage.removeItem('xm-user');
      this.$router.push('/login');
    },
  },
};
</script>

<style scoped>
@import "@/assets/css/hospital.css";

</style>