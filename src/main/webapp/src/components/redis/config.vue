<template>
  <div>
    <el-table :data="sources" stripe border>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="ip" label="ip"></el-table-column>
      <el-table-column prop="port" label="端口"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>


    <el-button type="primary" @click="dialogFormVisible = true" style="margin-top: 5px">添加</el-button>

    <el-dialog title="添加redis地址" :visible.sync="dialogFormVisible" width="600px">
      <el-form label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="name"></el-input>
        </el-form-item>
        <el-form-item label="ip地址">
          <el-input v-model="ip"></el-input>
        </el-form-item>
        <el-form-item label="端口">
          <el-input v-model="port"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="password"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="connect">连接测试</el-button>
        <el-button type="primary" @click="dialogFormVisible = false;add()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "config",
  data() {
    return {
      ip: '127.0.0.1',
      port: 6379,
      password: null,
      sources: [],
      name: null,
      dialogFormVisible: false
    }
  },
  created() {
    this.getAllSource()
  },
  methods: {
    deleteSource(id) {
      this.axios.post("/redis/deleteSource/" + id).then((response) => {
        this.sources = response.data
        this.$message.success("删除redis环境成功")
        this.getAllSource()

      }).catch((error) => {
        this.$message.error("删除redis环境失败")
      })
    },
    handleDelete(index, row) {
      console.log(index, row);
      this.deleteSource(row.id)
    },
    getAllSource() {
      this.axios.post("/redis/getAllSource").then((response) => {
        this.sources = response.data
      }).catch((error) => {
        this.$message.error("查询所有redis环境失败")
      })
    },
    add() {
      this.axios.post("/redis/add", {
        "name": this.name,
        "ip": this.ip,
        "port": this.port,
        "password": this.password
      }).then((response) => {
        this.sources = response.data
        this.$message.success("添加redis环境成功")
        this.getAllSource()
      }).catch((error) => {
        this.$message.error("添加redis环境失败")
      })
    },
    connect() {
      this.axios.post("/redis/connect", {
        "name": this.name,
        "ip": this.ip,
        "port": this.port,
        "password": this.password
      }).then((response) => {
        if (response.data)
          this.$message.success("连接成功")
        else
          this.$message.error("连接失败")
      }).catch((error) => {
        this.$message.error("连接测试失败")
      })
    }
  }
}
</script>

<style scoped>

</style>
