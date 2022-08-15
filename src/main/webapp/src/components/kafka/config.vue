<template>
  <div>
    <el-alert type="success" effect="dark" v-if="warning">
      <template slot="title">
        <span>添加环境成功，默认分配只读权限，要修改权限请前往
          <router-link to='/about/authority'>这里</router-link></span>
      </template>
    </el-alert>

    <el-table :data="sources" stripe border>
      <el-table-column prop="name" label="集群名称"></el-table-column>
      <el-table-column prop="broker" label="地址"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" type="danger"
                     @click="handleDelete(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>


    <el-button type="primary" @click="dialogFormVisible = true" style="margin-top: 5px">添加环境</el-button>

    <el-dialog title="添加kafka地址" :visible.sync="dialogFormVisible" width="600px">
      <el-form label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="name"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="broker"></el-input>
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="username"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="password"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false;add()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {initKafka, initRedis, initZK} from '@/js/auth.js'

export default {
  name: "config",
  data() {
    return {
      broker: '127.0.0.1:9092',
      sources: [],
      name: null,
      username: null,
      password: null,
      dialogFormVisible: false,
      warning: false
    }
  },
  created() {
    this.getAllSource()
  },
  methods: {
    deleteSource(id) {
      this.axios.post("/kafka/deleteSource/" + id).then((response) => {
        this.$message.success("删除kafka环境成功")
        this.getAllSource()
      }).catch((error) => {
        this.$message.error("删除kafka环境失败")
      })
    },
    handleDelete(index, row) {
      console.log(index, row);
      this.deleteSource(row.id)
    },
    getAllSource() {
      this.axios.post("/kafka/getSource").then((response) => {
        this.sources = response.data
      }).catch((error) => {
        this.$message.error("查询所有kafka环境失败")
      })
    },
    add() {
      this.axios.post("/kafka/add", {"name": this.name, "broker": this.broker,
        "username":this.username, "password":this.password}).then((response) => {
        // this.$message.success("添加kafka环境成功")
        this.warning = true
        initKafka(this)
        this.getAllSource()
      }).catch((error) => {
        this.$message.error("添加kafka环境失败")
      })
    }
  }
}
</script>

<style scoped>

</style>
