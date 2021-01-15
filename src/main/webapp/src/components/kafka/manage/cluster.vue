<template>
  <div>
    <kafkaSelect @kafkaChange="kafkaChange"></kafkaSelect>

    <el-table :data="tableData" stripe border>
      <el-table-column prop="id" label="broker id">
        <template slot-scope="scope">
          <span style="margin-right: 5px">{{scope.row.id}}</span>
          <el-tag type="danger" v-if="scope.row.controller" effect="dark" size="mini">controller</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="host" label="host"></el-table-column>
      <el-table-column prop="port" label="端口"></el-table-column>
      <!--            <el-table-column  label="类型">-->
      <!--                <template slot-scope="scope">-->
      <!--                    <div v-if="scope.row.internal">系统topic</div>-->
      <!--                    <div v-else>用户topic</div>-->

      <!--                </template>-->
      <!--            </el-table-column>-->
    </el-table>
  </div>
</template>

<script>
import kafkaSelect from '@/components/kafka/kafkaSelect.vue'

export default {
  name: "cluster",
  data() {
    return {
      broker: null,
      tableData: []

    }
  },
  methods: {

    getCluster() {
      this.axios.post("/kafka/cluster/info", {"broker": this.broker}).then((response) => {
        this.tableData = (response.data)
      }).catch((error) => {
        this.$message.error("查询集群信息失败")
      })
    },
    kafkaChange(broker) {
      this.broker = broker
      this.getCluster()
    },
  },
  components: {
    kafkaSelect
  }
}
</script>

<style scoped>

</style>