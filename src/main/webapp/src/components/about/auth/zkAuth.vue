<template>
  <div>

    <el-button type="primary" plain @click="save">保存</el-button>

    <el-table :data="sources" stripe border>
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
  import {initZK} from '@/js/auth.js'

  export default {
    name: "zkAuth",
    data() {
      return {
        sources: []
      }
    },
    methods: {
      getAllZKSource() {
        this.axios.post("/zookeeper/getAllSourceAuth").then((response) => {
          this.sources = response.data
        }).catch((error) => {
          this.$message.error("查询所有zk环境失败")
        })
      },
      initAuth(){
        initZK(this)
      },
      save() {
        let p = {}
        for (let item of this.sources) {
          p[item.id] = item.auth
        }

        this.axios.post("/zookeeper/auth", {"param": JSON.stringify(p)}).then((response) => {
          this.initAuth()
          this.$message.success("授权成功")
        }).catch((error) => {
          console.log(error)
          this.$message.error("授权失败")
        })
      }
    },
    created() {
      this.getAllZKSource()
    }
  }
</script>

<style scoped>

</style>