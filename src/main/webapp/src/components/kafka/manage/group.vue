<template>
  <div>
    <div style="display: flex">
      <kafkaSelect @kafkaChange="kafkaChange"></kafkaSelect>

      <el-input placeholder="搜索group" v-model="keyword" style="width: 250px;margin-left: 5px" clearable
                @keyup.enter.native="searchGroup">
        <el-button slot="append" icon="el-icon-search" @click="searchGroup"></el-button>
      </el-input>
    </div>
    <el-table :data="tableData" stripe border>
      <el-table-column prop="name" label="group名称"></el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">

          <el-button size="mini" circle type="primary" @click="getGroupDetail(scope.row.name)">
            <i class="iconfont icon-detail"></i>
          </el-button>
          <el-popconfirm title="确定删除吗？" @onConfirm="deleteConfirm(scope.row.name)" v-if="!scope.row.internal">
            <el-button size="mini" circle type="danger" slot="reference" style="margin-left: 5px"
                       :disabled="!auth.remove">
              <i class="el-icon-delete"></i>
            </el-button>
          </el-popconfirm>


        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="group消费偏移量详情" :visible.sync="dialogTableVisible">
<!--      <vxe-table-->
<!--          border-->
<!--          resizable-->
<!--          :span-method="mergeRowMethod"-->
<!--          :data="detail">-->
<!--        <vxe-table-column type="seq" width="60"></vxe-table-column>-->
<!--        <vxe-table-column field="topic" title="topic"></vxe-table-column>-->
<!--        <vxe-table-column field="partition" title="分区号"></vxe-table-column>-->
<!--        <vxe-table-column field="offset" title="消费偏移量"></vxe-table-column>-->
<!--        <vxe-table-column field="lag" title="未消费消息条数"></vxe-table-column>-->
<!--      </vxe-table>-->
      <group-table :data="detail"></group-table>
    </el-dialog>
  </div>
</template>

<script>
import kafkaSelect from '@/components/kafka/kafkaSelect.vue'
import GroupTable from '@/components/common/GroupTable.vue'

export default {
  name: "group",
  data() {
    return {
      sourceId: null,
      tableData: [],
      detail: [],
      dialogTableVisible: false,
      auth: {},
      keyword: null
    }
  },
  methods: {

    searchGroup() {
      this.axios.post("/kafka/group/search", {"sourceId": this.sourceId, "keyword": this.keyword}).then((response) => {
        if (response.data.success)
          this.tableData = response.data.data
        else
          this.$message.error(response.data.message)
      }).catch((error) => {
        this.$message.error("查询所有group失败")
      })
    },
    getGroups() {
      this.axios.post("/kafka/group/all", {"sourceId": this.sourceId}).then((response) => {
        if (response.data.success) {
          this.tableData = response.data.data
          console.log(this.tableData)
        } else
          this.$message.error(response.data.message)
      }).catch((error) => {
        this.$message.error("查询所有group失败")
      })
    },
    kafkaChange(sourceId) {
      this.sourceId = sourceId
      this.auth = this.$store.getters.getKafkaAuth(sourceId)
      this.getGroups()
    },
    getGroupDetail(group) {
      this.axios.post("/kafka/group/detail", {"sourceId": this.sourceId, "group": group}).then((response) => {
        if (response.data.success) {
          this.detail = response.data.data
          this.dialogTableVisible = true
        }

      }).catch((error) => {
        this.$message.error("查询group详情失败")
      })
    },
    deleteConfirm(group) {
      this.axios.post("/kafka/group/delete", {"sourceId": this.sourceId, "group": group}).then((response) => {
        this.$message.success("删除group成功")
        this.getGroups()
      }).catch((error) => {
        this.$message.error("删除group失败")
      })
    }
  },
  components: {
    kafkaSelect,GroupTable
  }
}
</script>

<style scoped>
i {
  font-size: 14px;
}
</style>
