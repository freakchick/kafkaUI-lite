<template>
  <el-select v-model="broker" placeholder="选择kafka" @change="selectkafka" clearable>
    <el-option v-for="item in sources" :key="item.name" :label="item.name" :value="item.broker"></el-option>
  </el-select>
</template>

<script>
export default {
  name: "kafkaSelect",
  data() {
    return {
      broker: null,
      sources: []
    }
  },
  created() {
    this.getAllSource()

  },
  methods: {
    getAllSource() {
      this.axios.post("/getSource").then((response) => {
        this.sources = response.data
        console.log(this.sources[0])
        if (this.sources.length > 0) {
          this.broker = this.sources[0].broker
          this.selectkafka()
        }
      }).catch((error) => {
      })
    },
    selectkafka() {
      // console.log(this.broker)
      this.$emit('kafkaChange', this.broker);
    }
  }
}
</script>

<style scoped>

</style>
