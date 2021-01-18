<template>
  <div>

    <el-tabs type="card">
      <el-tab-pane label="kafka权限" name="">
        <common-auth :sources="kafkaSources" @save="saveKafkaAuth"></common-auth>
      </el-tab-pane>
      <el-tab-pane label="zookeeper权限" name="">
        <common-auth :sources="zkSources" @save="saveZKAuth"></common-auth>
      </el-tab-pane>
      <el-tab-pane label="redis权限" name="">
        <common-auth :sources="redisSources" @save="saveRedisAuth"></common-auth>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>

  import commonAuth from '@/components/about/auth/commonAuth'
  import {initKafka, initZK, initRedis} from '@/js/auth.js'

  export default {
    name: "authority",
    data() {
      return {
        kafkaSources: [],
        zkSources: [],
        redisSources: []
      }
    },
    methods: {
      getAllKafkaSource() {
        this.axios.post("/kafka/getAllSourceAuth").then((response) => {
          this.kafkaSources = response.data
          console.log(response.data)
        }).catch((error) => {
          this.$message.error("查询所有kafka环境失败")
        })
      },
      saveKafkaAuth(p) {
        this.axios.post("/kafka/auth", {"param": JSON.stringify(p)}).then((response) => {
          this.$message.success("授权成功")
          initKafka(this)
        }).catch((error) => {
          console.log(error)
          this.$message.error("授权失败")
        })
      },
      getAllZKSource() {
        this.axios.post("/zookeeper/getAllSourceAuth").then((response) => {
          this.zkSources = response.data
        }).catch((error) => {
          this.$message.error("查询所有zk环境失败")
        })
      },
      getAllRedisSource() {
        this.axios.post("/redis/getAllSourceAuth").then((response) => {
          this.redisSources = response.data
        }).catch((error) => {
          this.$message.error("查询所有zk环境失败")
        })
      },
      saveZKAuth(p) {
        this.axios.post("/zookeeper/auth", {"param": JSON.stringify(p)}).then((response) => {
          this.$message.success("授权成功")
          initZK(this)
        }).catch((error) => {
          console.log(error)
          this.$message.error("授权失败")
        })
      },
      saveRedisAuth(p) {
        this.axios.post("/redis/auth", {"param": JSON.stringify(p)}).then((response) => {
          this.$message.success("授权成功")
          initRedis(this)
        }).catch((error) => {
          console.log(error)
          this.$message.error("授权失败")
        })
      }
    },
    created() {
      this.getAllKafkaSource()
      this.getAllZKSource()
      this.getAllRedisSource()
    },
    components: {commonAuth}
  }
</script>

<style scoped>

</style>