<template>
  <div>

    <el-tabs type="card">
      <el-tab-pane label="kafka" name="">
        <kafkaAuth></kafkaAuth>

      </el-tab-pane>
      <el-tab-pane label="zookeeper" name="cluster">
        <zk-auth></zk-auth>
      </el-tab-pane>
      <el-tab-pane label="redis" name="group">

      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import kafkaAuth from '@/components/about/auth/kafkaAuth'
import zkAuth from '@/components/about/auth/zkAuth'

export default {
  name: "authority",
  data() {
    return {
    }
  },
  methods: {},
  created() {

  },
  components: {kafkaAuth,zkAuth}
}
</script>

<style scoped>

</style>