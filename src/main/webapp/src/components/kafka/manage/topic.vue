<template>
  <div>
    <kafkaSelect @kafkaChange="kafkaChange"></kafkaSelect>
    <div>
      <el-button type="primary" @click="dialogFormVisible = true" size="small" :disabled="!auth.add">创建topic
      </el-button>
    </div>
    <div>
      <el-table :data="tableData" stripe border max-height="650" size="small">
        <el-table-column prop="name" label="topic名称"></el-table-column>
        <el-table-column label="类型">
          <template slot-scope="scope">
            <div v-if="scope.row.internal">系统topic</div>
            <div v-else>用户topic</div>

          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">

            <el-button size="mini" circle type="primary" @click="getTopicDetail(scope.row.name)">
              <i class="iconfont icon-detail"></i>
            </el-button>
            <el-popconfirm title="确定删除吗？" @onConfirm="deleteConfirm(scope.row.name)"
                           v-if="!scope.row.internal">
              <el-button size="mini" circle type="danger" slot="reference" style="margin: 5px" :disabled="!auth.remove">
                <i class="el-icon-delete"></i>
              </el-button>
            </el-popconfirm>


          </template>
        </el-table-column>
      </el-table>
    </div>


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


    <el-dialog title="topic 详情" :visible.sync="dialogTableVisible">
      <el-table :data="partitions" stripe border max-height="500" size="small">
        <el-table-column property="partition" label="分区号" width="80"></el-table-column>
        <el-table-column property="leader" label="leader分区">
          <template slot-scope="scope">
            <data-tag :right="scope.row.leader.id" left="broker" :title="scope.row.leader.host+':'+scope.row.leader.port"></data-tag>

          </template>

        </el-table-column>
        <el-table-column label="所有副本">

          <template slot-scope="scope">
            <!--            <list :data-list="scope.row.replicas"></list>-->
            <!--            <span v-for="item in scope.row.replicas">{{item.id}}</span>-->
            <data-tag :right="item.id" left="broker" :title="item.host+':'+item.port" v-for="item in scope.row.replicas"></data-tag>
          </template>
        </el-table-column>
        <el-table-column label="isr副本">


          <template slot-scope="scope">
            <data-tag :right="item.id" left="broker" :title="item.host+':'+item.port" v-for="item in scope.row.replicas"></data-tag>
          </template>
        </el-table-column>
        <el-table-column property="endOffset" label="消息数量" width="80"></el-table-column>
      </el-table>
    </el-dialog>

  </div>
</template>

<script>
  import kafkaSelect from '@/components/kafka/kafkaSelect.vue'
  import dataTag from '@/components/common/dataTag.vue'

  export default {
    name: "topic",
    data() {
      return {
        sourceId: null,
        // broker: null,
        sources: [],
        tableData: [],
        topic: {
          name: null,
          partition: null,
          replica: null
        },
        dialogFormVisible: false,

        dialogTableVisible: false,
        partitions: [],
        topicDetal: {},
        activeName: "topic",
        auth: {add: true}

      }
    },
    created() {

    },
    methods: {
      getTopics() {
        this.axios.post("/kafka/getTopics", {"sourceId": this.sourceId}).then((response) => {
          if (response.data.success) {
            this.tableData = response.data.data
          } else
            this.$message.error(response.data.message)
        }).catch((error) => {
          this.$message.error("查询所有topic失败")
        })
      },
      kafkaChange(sourceId) {
        this.sourceId = sourceId
        this.auth = this.$store.getters.getKafkaAuth(sourceId)
        this.getTopics()
      },
      addTopic() {
        this.topic['sourceId'] = this.sourceId
        this.axios.post("/kafka/createTopic", this.topic).then((response) => {
          this.$message.success("创建topic成功")
          this.getTopics()
        }).catch((error) => {
          this.$message.error("创建topic失败")
        })
      },
      deleteConfirm(topic) {
        this.axios.post("/kafka/deleteTopic",
          {"sourceId": this.sourceId, "topic": topic}).then((response) => {
          this.$message.success("删除topic成功")
          this.getTopics()
        }).catch((error) => {
          this.$message.error("删除topic失败")
        })
      },
      getTopicDetail(topic) {
        this.axios.post("/kafka/getTopicDetail",
          {"sourceId": this.sourceId, "topic": topic}).then((response) => {
          // this.$message.success(response.data)
          this.partitions = response.data.partitions
          this.topicDetal = response.data
          this.dialogTableVisible = true
        }).catch((error) => {
          this.$message.error("查询topic分区详情失败")
        })
      }

    },
    components: {
      kafkaSelect, dataTag
    }
  }
</script>

<style scoped>
  i {
    font-size: 13px;
  }

  li {
    border-bottom: 1px solid #000000;
  }

  div {
    margin: 5px 0;
  }
</style>
