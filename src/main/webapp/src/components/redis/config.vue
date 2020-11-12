<template>
  <div>
    <el-table :data="tableData" stripe border>
      <el-table-column prop="name" label="名称" width="180"></el-table-column>
      <el-table-column prop="address" label="地址" width="180"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>


    <el-button @click="dialogFormVisible = true">添加</el-button>

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
      password:null,
      tableData: [],
      name: null,
      dialogFormVisible: false
    }
  }
}
</script>

<style scoped>

</style>
