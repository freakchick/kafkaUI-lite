<template>
  <div>
    <el-select v-model="broker" placeholder="选择kafka">
      <el-option v-for="item in brokers" :key="item.address" :label="item.name" :value="item.address"></el-option>
    </el-select>
    <el-select v-model="topic" placeholder="选择topic">
      <el-option v-for="item in topics" :key="item" :label="item" :value="item"></el-option>
    </el-select>
    <h2> 生产消息</h2>

    <el-input type="textarea" size="medium" rows="10" v-model="sender" maxlength="3000" show-word-limit></el-input>
    <h2> 消费消息</h2>
    <el-radio v-model="mode" label="1">历史消息</el-radio>
    <el-radio v-model="mode" label="2">最新消息</el-radio>

    <el-switch v-model="on" active-text="开始消费" inactive-text="停止" active-color="#13ce66" inactive-color="#ff4949"></el-switch>

    <el-input v-model="group" placeholder="请输入group"></el-input>
    <el-input type="textarea" size="medium" rows="10" v-model="sender" maxlength="3000" show-word-limit></el-input>

  </div>
</template>

<script>
export default {
  name: "operate",
  data() {
    return {
      broker: null,
      brokers: [{"address": "127.0.0.1:9092", "name": "local kafka"}],
      topic: null,
      topics :["aa","bb"],
      sender:null,
      mode:"1",
      on:false
    }
  }
}
</script>

<style scoped>

</style>
