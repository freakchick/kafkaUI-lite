<template>
  <div>
    <div style="display: flex">
      <kafkaSelect @kafkaChange="kafkaChange"></kafkaSelect>
      <el-input placeholder="搜索topic" v-model="keyword" class="input-with-select" style="width: 250px;margin-left: 5px"
                clearable @keyup.enter.native="searchTopic">
        <el-button slot="append" icon="el-icon-search" @click="searchTopic"></el-button>
      </el-input>
    </div>

    <div>
      <el-button type="primary" @click="dialogFormVisible = true" size="small" :disabled="!auth.add"
                 style="margin-bottom: 5px">创建topic
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

            <el-button size="mini" circle type="info" @click="getTopicDetail(scope.row.name)">
              <i class="iconfont icon-detail"></i>
            </el-button>
            <el-popconfirm title="确定删除吗？" @onConfirm="deleteConfirm(scope.row.name)"
                           v-if="!scope.row.internal">
              <el-button size="mini" circle type="danger" slot="reference" style="margin: 5px" :disabled="!auth.remove">
                <i class="el-icon-delete"></i>
              </el-button>
            </el-popconfirm>

            <el-button size="mini" round type="info" @click="getGroupByTopic(scope.row.name)">
              consumer
            </el-button>


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

    <el-dialog :title="'消费 '+selectedTopic+' 的group'" :visible.sync="groupVisible">
      <div v-if="groups.length==0">暂无数据</div>
      <el-collapse @change="handleChange" accordion>
        <el-collapse-item :title="item.value" :name="item.value" v-for="item in groups">
          <el-table :data="it" stripe border max-height="500" size="small" v-for="it in groupDetail" style="margin: 5px 0">
            <el-table-column property="topic" label="topic"></el-table-column>
            <el-table-column property="partition" label="分区号"></el-table-column>
            <el-table-column property="offset" label="消费偏移量"></el-table-column>
            <el-table-column property="lag" label="未消费消息条数">

            </el-table-column>
          </el-table>
        </el-collapse-item>
      </el-collapse>
    </el-dialog>

    <el-dialog :title="selectedTopic+' 分区详情'" :visible.sync="dialogTableVisible">
      <el-table :data="partitions" stripe border max-height="500" size="small">
        <el-table-column property="partition" label="分区号" width="80"></el-table-column>
        <el-table-column property="leader" label="leader分区">
          <template slot-scope="scope">
            <data-tag :right="scope.row.leader.id" left="broker"
                      :title="scope.row.leader.host+':'+scope.row.leader.port"></data-tag>

          </template>

        </el-table-column>
        <el-table-column label="所有副本">

          <template slot-scope="scope">
            <!--            <list :data-list="scope.row.replicas"></list>-->
            <!--            <span v-for="item in scope.row.replicas">{{item.id}}</span>-->
            <data-tag :right="item.id" left="broker" :title="item.host+':'+item.port"
                      v-for="item in scope.row.replicas"></data-tag>
          </template>
        </el-table-column>
        <el-table-column label="isr副本">


          <template slot-scope="scope">
            <data-tag :right="item.id" left="broker" :title="item.host+':'+item.port"
                      v-for="item in scope.row.replicas"></data-tag>
          </template>
        </el-table-column>
        <el-table-column property="beginningOffset" label="最小偏移量"></el-table-column>
        <el-table-column property="endOffset" label="最大偏移量"></el-table-column>
        <el-table-column label="消息数量" width="80">
          <template slot-scope="scope">
            <span>{{ scope.row.endOffset - scope.row.beginningOffset }}</span>
          </template>
        </el-table-column>
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
      selectedTopic: null,
      groups: [],
      dialogFormVisible: false,

      dialogTableVisible: false,
      groupVisible: false,
      partitions: [],
      topicDetal: {},
      activeName: "topic",
      auth: {add: true},
      keyword: null,
      groupDetail:[]
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
      this.selectedTopic = topic
      this.axios.post("/kafka/getTopicDetail",
          {"sourceId": this.sourceId, "topic": topic}).then((response) => {
        // this.$message.success(response.data)
        this.partitions = response.data.partitions
        this.topicDetal = response.data
        this.dialogTableVisible = true
      }).catch((error) => {
        this.$message.error("查询topic分区详情失败")
      })
    },
    getGroupByTopic(topic) {
      this.selectedTopic = topic
      this.axios.post("/kafka/getGroupsByTopic",
          {"sourceId": this.sourceId, "topic": topic}).then((response) => {
        this.groups = response.data.data
        this.groupVisible = true
      }).catch((error) => {
        this.$message.error("失败")
      })
    },
    handleChange(val) {
      console.log(val);
      this.axios.post("/kafka/group/detail", {"sourceId": this.sourceId, "group": val}).then((response) => {
        if (response.data.success) {
          this.groupDetail = response.data.data

        }

      }).catch((error) => {
        this.$message.error("查询group详情失败")
      })
    },
    searchTopic() {
      this.axios.post("/kafka/searchTopic",
          {"sourceId": this.sourceId, "topic": this.keyword}).then((response) => {
        if (response.data.success) {
          this.tableData = response.data.data
        } else
          this.$message.error(response.data.message)

      }).catch((error) => {
        this.$message.error("搜索topic失败")
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
  margin-bottom: 5px 0;
}
</style>
