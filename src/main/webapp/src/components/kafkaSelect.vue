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
      }).catch((error) => {
      })
    },
    selectkafka() {
      // console.log(this.broker)
      this.$emit('kafkaChange',this.broker);
    }
  }
}
</script>

<style scoped>

</style>
