<template>
  <div>
    <el-select v-model="address" placeholder="选择zookeeper" @change="selectSource">
      <el-option v-for="item in sources" :key="item.id" :label="item.name" :value="item.address"></el-option>
    </el-select>

      <el-table :data="curentNode" border style="margin-top: 5px">
        <el-table-column prop="path" label="path" width="250"></el-table-column>
        <el-table-column prop="value" label="data" ></el-table-column>

      </el-table>

    <div class="tree">
      <el-tree :data="nodes" :load="loadNode" lazy @node-click="handleNodeClick"></el-tree>
    </div>

  </div>

</template>

<script>
export default {
  data() {
    return {
      sources: [],
      address: null,
      nodes: null,
      curentNode : []
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
      this.axios.post("/zookeeper/getNodes", {"address": this.address, "path": node.data.path}).then((response) => {
        resolve(response.data);
      }).catch((error) => {
      })

    },

    getAllSource() {
      this.axios.post("/zookeeper/getAllSource").then((response) => {
        this.sources = response.data
      }).catch((error) => {
      })
    },
    selectSource() {
      this.axios.post("/zookeeper/getRootNodes", {"address": this.address}).then((response) => {
        this.nodes = response.data
      }).catch((error) => {
      })
    },
    getAllNode() {
      this.axios.post("/zookeeper/getAllNodes", {"address": this.address}).then((response) => {
        this.nodes = response.data
      }).catch((error) => {
      })
    },
    handleNodeClick(data) {
      console.log(data)
      console.log(data.path)
      this.curentNode = []
      this.curentNode.push(data)

    }
  },
  created() {
    this.getAllSource()
  }
}
</script>

<style scoped lang="scss">
.select {
  margin-bottom: 5px;
}

.tree {
  margin-top: 5px;
  border: 1px solid #42b983;

}
</style>
