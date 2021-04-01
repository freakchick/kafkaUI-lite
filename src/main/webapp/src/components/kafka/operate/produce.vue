<template>
  <div>
    <h3>kafka生产消息：</h3>
    <div style="display: flex;margin-top: 5px">

        <kafkaSelect @kafkaChange="kafkaChange"></kafkaSelect>
        <el-select v-model="topic" filterable placeholder="选择topic" clearable style="margin-left: 5px;">
          <el-option v-for="item in topics" :key="item.name" :label="item.name"
                     :value="item.name"></el-option>
        </el-select>

    </div>


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
