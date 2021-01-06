<template>
  <div>
    <el-row style="padding-bottom: 10px">
      <el-col :span="12">
        kafka:
        <kafkaSelect @kafkaChange="getTopics"></kafkaSelect>
      </el-col>
      <el-col :span="12">
        topic:
        <el-select v-model="topic" filterable placeholder="选择topic" clearable>
          <el-option v-for="item in topics" :key="item.name" :label="item.name" :value="item.name"></el-option>
        </el-select>
      </el-col>
    </el-row>

    <consumer :broker="broker" :topic="topic"></consumer>

  </div>
</template>

<script>
import consumer from '@/components/kafka/consumer.vue'
import producer from '@/components/kafka/producer.vue'
import kafkaSelect from '@/components/kafka/kafkaSelect.vue'

export default {
  name: "operate",
  data() {
    return {
      broker: null,
      sources: [],
      topic: null,
      topics: [],
      message: null
    }
  },

  methods: {
    getTopics(broker) {
      this.broker = broker
      console.log(broker);
      this.axios.post("/kafka/getTopics", {"brokers": this.broker}).then((response) => {
        this.topics = response.data
      }).catch((error) => {
        this.$message.error("查询所有topic失败")
      })
    },

  },
  components: {
    consumer, kafkaSelect, producer
  }
}
</script>

<style scoped>

</style>
