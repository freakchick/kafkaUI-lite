<template>
  <div>
    <el-select v-model="address" placeholder="选择zookeeper" @change="getRootNodes">
      <el-option v-for="item in sources" :key="item.id" :label="item.name" :value="item.address"></el-option>
    </el-select>

    <el-table :data="currentNode" border>
      <el-table-column prop="path" label="当前节点"></el-table-column>
      <el-table-column label="数据">
        <template slot-scope="scope">
          <el-input v-model="scope.row.value" type="textarea" autosize :disabled="!editable" ref="input">
          </el-input>
          <el-button size="mini" round type="primary" v-if="editable"
                     @click="update(scope.row)">提交
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div>
      <el-button size="small" type="primary" @click="clickAdd" circle>
        <i class="el-icon-circle-plus-outline"></i>
      </el-button>
      <el-button size="small" type="danger" @click="clickRemove" circle>
        <i class="el-icon-delete"></i>
      </el-button>
      <el-button size="small" type="warning" @click="clickEdit" circle>
        <i class="el-icon-edit"></i>
      </el-button>
      <el-button size="small" type="primary" @click="refresh" circle>
        <i class="el-icon-refresh-right"></i>
      </el-button>
    </div>
    <div class="tree">
      <el-tree :data="nodes" :load="loadNode" highlight-current lazy @node-click="handleNodeClick"></el-tree>
    </div>

    <el-dialog title="添加zookeeper节点" :visible.sync="dialogFormVisible" width="600px">
      <el-form label-width="80px">
        <el-form-item label="节点路径">
          <el-input v-model="createNode.path"></el-input>
        </el-form-item>
        <el-form-item label="节点数据">
          <el-input v-model="createNode.data"></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-checkbox v-model="recursion">如果父节点不存在，递归创建父节点</el-checkbox>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false;add()">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="是否确认删除节点" :visible.sync="dialogFlag" width="600px">
      <span>{{ createNode.path }}</span>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFlag = false">取消</el-button>
        <el-button type="danger" @click="dialogFlag = false;removeNode()">确定</el-button>
      </div>
    </el-dialog>

  </div>

</template>

<script>
export default {
  data() {
    return {
      sources: [],
      address: null,
      nodes: null,
      currentNode: [],
      data: null,
      editable: false,
      dialogFormVisible: false, dialogFlag: false,
      createNode: {"path": null, "data": null},
      recursion: true //是否递归创建父节点
    }
  },
  methods: {

    // 异步树叶子节点懒加载逻辑
    loadNode(node, resolve) {
      // 一级节点处理

      // 其余节点处理
      if (node.level >= 1) {
        // 注意！把resolve传到你自己的异步中去
        this.getChildren(node, resolve);
      }
    },
    getChildren(node, resolve) {
      this.axios.post("/zookeeper/getNodes", {
        "address": this.address,
        "path": node.data.path
      }).then((response) => {
        resolve(response.data);
      }).catch((error) => {
        this.$message.error("查询子节点失败")
      })

    },

    getAllSource() {
      this.axios.post("/zookeeper/getAllSource").then((response) => {
        this.sources = response.data
      }).catch((error) => {
        this.$message.error("查询所有zk环境失败")
      })
    },
    getRootNodes() {
      this.nodes = null
      this.axios.post("/zookeeper/getRootNodes", {"address": this.address}).then((response) => {
        this.nodes = response.data
      }).catch((error) => {
        this.$message.error("查询根节点失败")
      })
    },
    getAllNode() {
      this.axios.post("/zookeeper/getAllNodes", {"address": this.address}).then((response) => {
        this.nodes = response.data
      }).catch((error) => {
      })
    },
    handleNodeClick(data) {
      this.currentNode = []
      this.currentNode.push(data)
      this.createNode.path = data.path

    },
    clickEdit() {
      if (this.createNode.path == null) {
        this.$message.error("请先选择节点")
        return
      }
      this.editable = !this.editable
    },
    update(row) {
      console.log(row)

      this.axios.post("/zookeeper/setData",
          {
            "address": this.address,
            "path": row.path,
            "data": row.value.trim().length == 0 ? null : row.value.trim()
          }).then((response) => {
        this.$message.success("修改数据成功")
        this.editable = false
      }).catch((error) => {
        this.$message.error("修改数据失败")
      })
    },
    clickAdd() {
      this.dialogFormVisible = true
    },
    add() {
      if (this.createNode.data.trim().length == 0) {

      }
      this.axios.post("/zookeeper/createNode",
          {
            "address": this.address,
            "path": this.createNode.path,
            "data": this.createNode.data.trim().length == 0 ? null : this.createNode.data.trim(),
            "recursion": this.recursion
          }).then((response) => {
        this.$message.success("添加节点成功")
        this.refresh()
        this.editable = false
      }).catch((error) => {
        this.$message.error("添加节点失败")
      })
    },
    clickRemove() {
      if (this.createNode.path == null) {
        this.$message.error("请先选择节点")
        return
      }
      this.dialogFlag = true
    },
    removeNode() {
      this.axios.post("/zookeeper/removeNode",
          {"address": this.address, "path": this.createNode.path}).then((response) => {
        this.$message.success("删除节点成功")
        this.refresh()
      }).catch((error) => {
        this.$message.error("删除节点失败")
      })
    },

    refresh() {
      this.currentNode = []
      this.getRootNodes()
    }
  },
  created() {
    this.getAllSource()
  }
}
</script>

<style scoped >
.tree {
  border: 1px solid #00a0e9;
  /*height: 500px;*/
  max-height: 600px;
  overflow: auto;
}

div {
  margin: 5px 0;
}

i {
  font-size: 14px;
}
</style>
