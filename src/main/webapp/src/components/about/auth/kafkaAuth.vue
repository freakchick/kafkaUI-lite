<template>
  <div>

    <el-button type="primary" plain @click="save">保存</el-button>

    <el-table :data="kafkaSources" stripe border>
      <!--          <el-table-column prop="id" label="id">-->

      <!--          </el-table-column>-->
      <el-table-column prop="name" label="name"></el-table-column>
      <el-table-column label="权限">

        <template slot-scope="scope">
          <el-checkbox label="新增" v-model="scope.row.auth.add"></el-checkbox>
          <el-checkbox label="修改" v-model="scope.row.auth.update"></el-checkbox>
          <el-checkbox label="删除" v-model="scope.row.auth.remove"></el-checkbox>
          <el-checkbox label="查询" checked disabled></el-checkbox>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    name: "kafkaAuth",
    data() {
      return {
        kafkaSources: []
      }
    },
    methods: {
      getAllKafkaSource() {
        this.axios.post("/kafka/getAllSourceAuth").then((response) => {
          this.kafkaSources = response.data
        }).catch((error) => {
          this.$message.error("查询所有kafka环境失败")
        })
      },
      save() {
        let p = {}
        for (let item of this.kafkaSources) {
          p[item.id] = item.auth
        }

        this.axios.post("/kafka/auth", {"param": JSON.stringify(p)}).then((response) => {
          this.$message.success("授权成功")
        }).catch((error) => {
          this.$message.error("授权失败")
        })
      }
    },
    created() {
      this.getAllKafkaSource()
    }
  }
</script>

<style scoped>

</style>