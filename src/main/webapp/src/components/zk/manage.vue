<template>
  <div>

    <el-select v-model="address" placeholder="选择zookeeper" @change="selectSource">
      <el-option v-for="item in sources" :key="item.id" :label="item.name" :value="item.address"></el-option>
    </el-select>

    <el-tree :data="nodes"  @node-click="handleNodeClick"></el-tree>

    <div>
      <span>{{data}}</span>
    </div>
  </div>

</template>

<script>
export default {
  data() {
    return {
      sources: [{"id": 1, "name": "local zk", "address": "127.0.0.1:2181"}],
      address: null,
      nodes: null,
      data:null
    }
  },
  methods: {
    getAllSource() {
      this.axios.post("/zookeeper/getAllSource").then((response) => {
        this.sources = response.data
      }).catch((error) => {
      })
    },
    selectSource() {
      this.getAllNode()
    },
    getAllNode() {
      this.axios.post("/zookeeper/getAllNodes", {"address": this.address}).then((response) => {
        this.nodes = response.data
      }).catch((error) => {
      })
    },
    handleNodeClick(data) {
      console.log(data)
      this.axios.post("/zookeeper/getData", {"address": this.address,"path":data['path']}).then((response) => {
        this.data = response.data
      }).catch((error) => {
      })
    }
  },
  created() {
    this.getAllSource()
  }
}
</script>

<style scoped>

</style>
