<template>
  <div>
    <!-- 搜索框 -->
    <div class="search-container">
      <el-input v-model="searchKeyword" placeholder="请输入医院名称" @keyup.enter="fetchHospitalList(1)"></el-input>
      <el-button @click="fetchHospitalList(1)">搜索</el-button>
    </div>

    <!-- 医院列表 -->
    <el-table :data="hospitalList" stripe>
      <el-table-column prop="name" label="医院名称">
        <template slot-scope="scope">
          <el-link type="primary" @click="goToHospitalDetail(scope.row.id)">{{ scope.row.name }}</el-link>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        background
        layout="prev, pager, next"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="totalHospitals"
        @current-change="handlePageChange"
    ></el-pagination>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Reservation',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      searchKeyword: '', // 搜索关键词
      hospitalList: [], // 医院列表
      currentPage: 1, // 当前页码
      pageSize: 10, // 每页显示条数
      totalHospitals: 0 // 总条数
    };
  },
  created() {
    this.fetchHospitalList(1); // 初始化加载医院列表
  },
  methods: {
    // 获取医院列表
    fetchHospitalList(pageNumber) {
      this.currentPage = pageNumber;
      this.$request.get('/hospital/selectPage', {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          name: this.searchKeyword
        }
      })
          .then(response => {
            if (response.data && response.data.list) {
              this.hospitalList = response.data.list;
            }
            if (response.data && response.data.total) {
              this.totalHospitals = response.data.total;
            }
          })
          .catch(error => {
            console.error('获取医院列表失败:', error);
            this.$message.error('获取医院列表失败，请稍后重试');
          });
    },
    // 重置搜索
    resetSearch() {
      this.searchKeyword = '';
      this.fetchHospitalList(1);
    },
    // 分页切换
    handlePageChange(newPage) {
      this.fetchHospitalList(newPage);
    },
    // 跳转到医院详情页面
    goToHospitalDetail(hospitalId) {
      this.$router.push(`/appointment-registration/hospital/${hospitalId}`);
    }
  }
};
</script>

<style scoped>
.search-container {
  margin-bottom: 20px;
}
</style>