<template>
  <el-select v-model="sourceId" placeholder="选择kafka环境" @change="selectkafka" style="margin-bottom: 5px">
    <el-option v-for="item in sources" :key="item.name" :label="item.name" :value="item.id"></el-option>
  </el-select>
</template>

<script>
export default {
  name: "kafkaSelect",
  data() {
    return {
      sourceId: null,
      sources: []
    }
  },
  created() {
    this.getAllSource()

  },
  methods: {
    getAllSource() {
      this.axios.post("/kafka/getSource").then((response) => {
        this.sources = response.data
        // console.log(this.sources[0])
        // if (this.sources.length > 0) {
        //   this.source = this.sources[0].source
        //   this.selectkafka()
        // }
      }).catch((error) => {
        this.$message.error("查询所有kafka环境失败")
      })
    },
    selectkafka() {
      if (this.sourceId != null && this.sourceId != '') {
        this.$emit('kafkaChange', this.sourceId);
        // this.$emit('sourceId', this.source.id);
      }
    }
  }
}
</script>

<style scoped>

</style>
