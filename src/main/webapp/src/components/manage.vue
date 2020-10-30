<template>
  <div>
    <kafkaSelect @kafkaChange="kafkaChange"></kafkaSelect>

    <el-table :data="tableData" >
      <el-table-column prop="name" label="topic" width="180"></el-table-column>
    </el-table>

  </div>
</template>

<script>
import kafkaSelect from '@/components/kafkaSelect.vue'

export default {
  name: "manage",
  data() {
    return {
      broker: null,
      sources: [],
      tableData: []
    }
  },
  created() {
  },
  methods: {
    getTopics() {
      this.axios.post("/getTopics", {"brokers": this.broker}).then((response) => {
        this.tableData = response.data
      }).catch((error) => {
      })
    },
    kafkaChange(broker) {
      this.broker = broker
      this.getTopics()
    }
  },
  components: {
    kafkaSelect
  }
}
</script>

<style scoped>

</style>
