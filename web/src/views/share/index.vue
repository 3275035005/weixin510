<template>
  <div class="dashboard-container">
    <div class="container">
      <div class="tableBar">
        <label style="margin-right: 5px">菜谱名称：</label>
        <el-input
          v-model="input"
          placeholder="请输入菜谱名称"
          style="width: 15%"
          clearable
          @clear="init"
          @keyup.enter.native="initFun"
        />
        <label style="margin-right: 5px">审核状态：</label>
        <el-select v-model="status"
                   style="width: 14%"
                   placeholder="请选择审核状态"
                   clearable>
          <el-option label="审核中" value="0" />
          <el-option label="通过" value="1" />
          <el-option label="拒绝" value="2" />
        </el-select>
        <el-button class="normal-btn continue" @click="init(true)"
          >查询</el-button
        >
      </div>
      <el-table
        :data="tableData"
        stripe
        v-if="tableData.length"
        class="tableBox"
      >
        <el-table-column prop="setmealName" label="菜谱名称" />
        <el-table-column prop="dishName" label="食材" />
        <el-table-column prop="employeeName" label="分享用户" />
        <el-table-column prop="image" label="分享图片">
          <template slot-scope="scope">
            <el-image
              style="width: 100px; height: 100px"
              :src="scope.row.image"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="审核状态">
          <template slot-scope="scope">
            <div
              class="tableColumn-status"
              :class="{ 'stop-use': String(scope.row.status) === '0' }"
            >
              {{ scope.row.status === '0' ? '审核中' : (scope.row.status === '1' ? '通过' : '拒绝')}}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" />
        <el-table-column label="操作" width="160" align="center">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.status === '0'"
              type="text"
              size="small"
              class="non"
              :class="{
               blueBug:true
              }"
              @click="statusHandle(scope.row.id, '1', '通过')"
            >
              通过
            </el-button>
            <el-button
              v-if="scope.row.status === '0'"
              type="text"
              size="small"
              class="non"
              :class="{
                delBut: true
              }"
              @click="statusHandle(scope.row.id, '2', '拒绝')"
            >
              拒绝
            </el-button>

          </template>
        </el-table-column>
      </el-table>
      <Empty v-else :is-search="isSearch" />
      <el-pagination
        class="pageList"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="counts"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import HeadLable from '@/components/HeadLable/index.vue'
import { getShareList, updateById } from '@/api/share'
import { UserModule } from '@/store/modules/user'
import InputAutoComplete from '@/components/InputAutoComplete/index.vue'
import Empty from '@/components/Empty/index.vue'

@Component({
  name: 'Share',
  components: {
    HeadLable,
    InputAutoComplete,
    Empty,
  },
})
export default class extends Vue {
  private input: any = ''
  private counts: number = 0
  private page: number = 1
  private pageSize: number = 10
  private tableData = []
  private id = ''
  private status = ''
  private isSearch: boolean = false

  created() {
    this.init()
  }

  initProp(val) {
    this.input = val
    this.initFun()
  }

  initFun() {
    this.page = 1
    this.init()
  }

  get userName() {
    return UserModule.username
  }

  private async init(isSearch?: boolean) {
    this.isSearch = isSearch
    const params = {
      page: this.page,
      pageSize: this.pageSize,
      setmealName: this.input ? this.input : undefined,
      status: this.status ? this.status : undefined,
    }
    await getShareList(params)
      .then((res: any) => {
        if (String(res.data.code) === '1') {
          this.tableData = res.data && res.data.data && res.data.data.records
          this.counts = res.data.data.total
        }
      })
      .catch((err) => {
        this.$message.error('请求出错了：' + err.message)
      })
  }

  //状态修改
  private statusHandle(id, status, msg) {
    this.$confirm('确认审核'+msg+'美食状态?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(() => {
      updateById({ id: id, status: status })
        .then((res) => {
          if (String(res.status) === '200') {
            this.$message.success('状态审核成功！')
            this.init()
          }
        })
        .catch((err) => {
          this.$message.error('请求出错了：' + err.message)
        })
    })
  }

  private handleSizeChange(val: any) {
    this.pageSize = val
    this.init()
  }

  private handleCurrentChange(val: any) {
    this.page = val
    this.init()
  }
}
</script>

<style lang="scss" scoped>
.disabled-text {
  color: #bac0cd !important;
}
</style>
