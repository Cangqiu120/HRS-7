<template>
  <div class="warehouse-management">
    <div class="left-sidebar">
      <el-menu :default-active="activeOption" @select="handleOptionSelect">
        <el-menu-item index="drugManagement">药物管理</el-menu-item>
        <el-menu-item index="instrumentManagement">仪器管理</el-menu-item>
      </el-menu>
    </div>
    <div class="main-content">
      <h2 class="page-title">仓库管理</h2>
      <el-table :data="currentWarehouseList" border style="width: 100%;">
        <el-table-column prop="id" label="id" width="35" ></el-table-column>
        <el-table-column prop="name" label="物品名称" width="100"></el-table-column>
        <el-table-column prop="stock" label="库存" width="100"></el-table-column>
        <el-table-column prop="department" label="所属部门" width="150"></el-table-column>
        <el-table-column prop="manufacturer" label="生产日期" width="150"></el-table-column>
        <el-table-column prop="sellBy" label="有效期" width="150"></el-table-column>
        <el-table-column prop="supplier" label="供应商" width="100"></el-table-column>
        <el-table-column prop="price" label="价格" width="100"></el-table-column>
        <el-table-column prop="status" label="状态" width="100" :formatter="formatStatus"></el-table-column>
        <el-table-column label="操作" width="250"> <!-- 调整宽度以容纳新按钮 -->
          <template slot-scope="scope">
            <el-button size="mini" @click="handleManage(scope.row)">管理</el-button>
            <el-button size="mini" type="success" @click="handlePurchase(scope.row)">进货</el-button>
            <!-- 根据状态显示不同的按钮文本和点击事件 -->
            <el-button
                size="mini"
                :type="scope.row.status === 1? 'danger' :'success'"
                @click="toggleStatus(scope.row)"
            >
              {{ scope.row.status === 1? '停用' : '取消停用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 模态框 -->
      <el-dialog
          title="管理信息"
          :visible.sync="dialogVisible"
          width="30%"
      >
        <p v-if="message">{{ message }}</p>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import moment from'moment'; // 引入 moment 库用于处理时间
export default {
  name: "WarehouseManagement",
  data() {
    return {
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'),
      activeOption: "drugManagement", // 默认选中药物管理
      drugWarehouseList: [],
      instrumentWarehouseList: [],
      currentWarehouseList: [],
      currentType: 1, // 新增一个变量来记录当前的type
      dialogVisible: false, // 控制模态框的显示隐藏
      message: '', // 模态框中显示的信息
    };
  },
  created() {
    // 页面创建时默认获取药物管理数据
    this.fetchStockDetail(1);
  },
  methods: {
    handleOptionSelect(option) {
      if (option === "drugManagement") {
        this.currentWarehouseList = this.drugWarehouseList;
        this.currentType = 1; // 当选择药物管理时，更新currentType为1
        this.fetchStockDetail(1);
      } else if (option === "instrumentManagement") {
        this.currentWarehouseList = this.instrumentWarehouseList;
        this.currentType = 2; // 当选择仪器管理时，更新currentType为2
        this.fetchStockDetail(2);
      }
    },

    fetchStockDetail(type) {
      this.$request.get('/stock/selectStockByType', {
        params: {
          type: type, // 传递类型参数
          hospitalAdminId: this.user.id,
        },
      }).then(response => {
        if (response.code == 200) {
          if (type === 1) {
            this.drugWarehouseList = response.data;
            this.currentWarehouseList = this.drugWarehouseList;
          } else if (type === 2) {
            this.instrumentWarehouseList = response.data;
            this.currentWarehouseList = this.instrumentWarehouseList;
          }
        } else {
          this.error = response.data.message || '获取数据失败';
        }
      })
          .catch(error => {
            console.error('获取数据失败:', error);
            this.error = '获取数据失败，请稍后重试';
          })
          .finally(() => {
            this.loading = false;
          });
    },

    formatStatus(row, column, cellValue) {
      // 新增的判断逻辑，根据currentType来确定使用哪个列表的数据
      if (this.currentType === 1 && this.drugWarehouseList.length > 0) {
        const index = this.currentWarehouseList.indexOf(row);
        if (index!== -1) {
          cellValue = this.drugWarehouseList[index].status;
        }
      }
      //状态 0.停用 1.正常  2.过期  3.缺货
      switch (cellValue) {
        case 0:
          return '停用';
        case 1:
          return '正常';
        case 2:
          return '过期';
        case 3:
          return '缺货';
        default:
          return '未知状态';
      }
    },

    handleManage(row) {
      console.log('点击了管理按钮，管理的物品是：', row.id);
      this.dialogVisible = true;
      const currentTime = moment();
      const expireTime = moment(row.sellBy);
      const diff = expireTime.diff(currentTime, 'days');
      if (diff <= 7 && diff > 0) { // 假设临近有效期为7天内
        this.message = `即将过期，剩余 ${diff} 天`;
        this.updateStatus(row);
      } else if (row.stock < 15) {
        this.message = '库存即将不足';
      } else {
        this.message = '一切正常';
      }
    },

    updateStatus(row) {

      this.$request.post(`/stock/updateStatus/${row.id}`)
          .then(response => {
            if (response.code == 200) {
              console.log('状态更新成功');
              // 刷新数据
              this.fetchStockDetail(this.currentType);
            } else {
              console.error('状态更新失败:', response.data.message);
            }
          })
          .catch(error => {
            console.error('状态更新请求失败:', error);
          });
    },

    handlePurchase(row) {
      console.log('点击了进货按钮，进货的物品是：', row.name);
      this.$router.push({
        name: 'Purchase',
        query: {
          name : row.name
        }
      })
    },

    toggleStatus(row) {
      const newStatus = row.status === 1? 0 : 1;
      const url = `/stock/updateStatusDeactivated/${row.id}/${newStatus}`;
      this.$request.post(url).then(response => {
            if (response.code == 200) {
              console.log('状态更新成功');
              this.fetchStockDetail(this.currentType);
            } else {
              console.error('状态更新失败:', response.data.message);
            }
          })
          .catch(error => {
            console.error('状态更新请求失败:', error);
          });
    },
  },
};
</script>

<style scoped>
.warehouse-management {
  display: flex;
}

.left-sidebar {
  width: 200px;
  background-color: #f4f4f4;
  padding: 10px;
}

.main-content {
  flex: 1;
  padding: 20px;
}

.page-title {
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: bold;
}
</style>