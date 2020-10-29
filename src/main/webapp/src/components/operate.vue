<template>
  <div>
    <el-select v-model="broker" placeholder="选择kafka">
      <el-option v-for="item in brokers" :key="item.address" :label="item.name" :value="item.address"></el-option>
    </el-select>
    <el-select v-model="topic" placeholder="选择topic">
      <el-option v-for="item in topics" :key="item" :label="item" :value="item"></el-option>
    </el-select>
    <h2> 生产消息</h2>
    <el-input type="textarea" size="medium" rows="10" maxlength="3000" show-word-limit></el-input>

    <consumer></consumer>

  </div>
</template>

<script>
import consumer from '@/components/consumer.vue'

export default {
  name: "operate",
  data() {
    return {
      broker: null,
      brokers: [{"address": "127.0.0.1:9092", "name": "local kafka"}],
      topic: null,
      topics: ["aa", "bb"]

    }
  },

  methods: {},
  components: {
    consumer
  }
}
</script>

<style scoped>

</style>
