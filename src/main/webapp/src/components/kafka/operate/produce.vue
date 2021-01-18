<template>
  <div>
    <el-row style="padding-bottom: 10px">
      <el-col :span="12">
        kafka:<kafkaSelect @kafkaChange="kafkaChange"></kafkaSelect>
      </el-col>
      <el-col :span="12">
        topic:
        <el-select v-model="topic" filterable placeholder="选择topic" clearable>
          <el-option v-for="item in topics" :key="item.name" :label="item.name"
                     :value="item.name"></el-option>
        </el-select>
      </el-col>
    </el-row>


    <producer :sourceId="sourceId" :topic="topic"></producer>

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
      sourceId: null,
      sources: [],
      topic: null,
      topics: [],
      message: null
    }
  },

  methods: {
    kafkaChange(sourceId) {
      this.sourceId = sourceId
      // this.auth = this.$store.getters.getKafkaAuth(sourceId)
      this.getTopics()
    },
    getTopics(sourceId) {
      this.axios.post("/kafka/getTopics", {"sourceId": this.sourceId}).then((response) => {
        if (response.data.success) {
          this.topics = response.data.data
        } else
          this.$message.error(response.data.message)
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
