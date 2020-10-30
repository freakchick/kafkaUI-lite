<template>
  <div>
    <kafkaSelect @kafkaChange="kafkaChange"></kafkaSelect>

    <el-table :data="tableData">
      <el-table-column prop="name" label="topic" width="180"></el-table-column>
    </el-table>

    <el-input v-model="topic.name" placeholder="请输入topic名称"></el-input>
    <el-input v-model="topic.partition" placeholder="请输入分区数量"></el-input>
    <el-input v-model="topic.replica" placeholder="请输入副本数量"></el-input>
    <el-button @click="addTopic">添加</el-button>

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
      tableData: [],
      topic: {
        name: null,
        partition: null,
        replica: null
      }

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
    },
    addTopic() {
      this.topic['broker'] = this.broker
      this.axios.post("/createTopic", this.topic).then((response) => {
        this.getTopics()
      }).catch((error) => {
      })
    }
  },
  components: {
    kafkaSelect
  }
}
</script>

<style scoped>

</style>
