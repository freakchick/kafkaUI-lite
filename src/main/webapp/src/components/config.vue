<template>
  <div>
    broker地址:
    <el-input v-model="broker"></el-input>
    <el-button>添加</el-button>
  </div>
</template>

<script>
export default {
  name: "config",
  data() {
    return {
      broker: null
    }
  }
}
</script>

<style scoped>

</style>
