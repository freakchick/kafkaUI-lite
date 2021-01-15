<template>
  <div>

    <el-tabs type="card">
      <el-tab-pane label="kafka" name="">
        <kafkaAuth></kafkaAuth>

      </el-tab-pane>
      <el-tab-pane label="zookeeper" name="cluster">

      </el-tab-pane>
      <el-tab-pane label="redis" name="group">

      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import kafkaAuth from '@/components/about/auth/kafkaAuth'

export default {
  name: "authority",
  data() {
    return {
      kafkaSources: []
    }
  },
  methods: {},
  created() {

  },
  components: {kafkaAuth}
}
</script>

<style scoped>

</style>