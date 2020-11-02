<template>
  <div>
    <kafkaSelect @kafkaChange="kafkaChange"></kafkaSelect>

    <h3>所有topic：</h3>
    <el-table :data="tableData" stripe border>
      <el-table-column prop="name" label="topic"></el-table-column>
    </el-table>

    <el-button @click="dialogFormVisible = true">创建topic</el-button>
    <el-dialog title="创建topic" :visible.sync="dialogFormVisible" width="500px">
      <el-form label-width="80px">
        <el-form-item label="topic名称">
          <el-input v-model="topic.name" placeholder="请输入topic名称"></el-input>
        </el-form-item>
        <el-form-item label="分区数量">
          <el-input-number v-model="topic.partition" :min="1" label="请输入分区数量"></el-input-number>
<!--          <el-input v-model="topic.partition" placeholder="请输入分区数量"></el-input>-->
        </el-form-item>
        <el-form-item label="副本数量">
          <el-input-number v-model="topic.replica" :min="1" label="请输入分区数量"></el-input-number>
<!--          <el-input v-model="topic.replica" placeholder="请输入副本数量"></el-input>-->
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false;addTopic()">确 定</el-button>
      </div>
    </el-dialog>




<!--    <el-button @click="addTopic">添加</el-button>-->

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
      },
      dialogFormVisible: false

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
