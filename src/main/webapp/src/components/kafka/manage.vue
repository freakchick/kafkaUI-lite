<template>
    <div>


        <kafkaSelect @kafkaChange="kafkaChange"></kafkaSelect>
        <div>
            <el-button type="primary" @click="dialogFormVisible = true">创建topic</el-button>
        </div>
        <div>
            <el-table :data="tableData" stripe border>
                <el-table-column prop="name" label="topic"></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-popconfirm title="确定删除吗？" @onConfirm="deleteConfirm(scope.row.name)">
                            <el-button size="mini" circle type="danger" slot="reference" style="margin: 0 5px">
                                <i class="el-icon-delete"></i>
                            </el-button>
                        </el-popconfirm>

                        <el-button size="mini" circle type="primary" @click="getTopicDetail(scope.row.name)">
                            <i class="iconfont icon-detail"></i>
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


        <el-dialog title="topic 详情" :visible.sync="dialogTableVisible">
<!--            <div>名称：{{topicDetal.name}}</div>-->
<!--            <div>分区数量： {{partitions.length}}</div>-->
<!--            <div>分区详情：</div>-->
            <el-table :data="partitions" stripe border max-height="500" size="small">
                <el-table-column property="partition" label="分区号" width="80"></el-table-column>
                <el-table-column property="leader" label="leader分区"></el-table-column>
                <el-table-column property="replicas" label="副本"></el-table-column>
                <el-table-column property="isr" label="isr"></el-table-column>
            </el-table>
        </el-dialog>

    </div>
</template>

<script>
    import kafkaSelect from '@/components/kafka/kafkaSelect.vue'

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
                dialogFormVisible: false,

                dialogTableVisible: false,
                partitions: [],
                topicDetal: {}

            }
        },
        created() {
        },
        methods: {
            getTopics() {
                this.axios.post("/kafka/getTopics", {"brokers": this.broker}).then((response) => {
                    this.tableData = response.data
                }).catch((error) => {
                    this.$message.error("查询所有topic失败")
                })
            },
            kafkaChange(broker) {
                this.broker = broker
                this.getTopics()
            },
            addTopic() {
                this.topic['broker'] = this.broker
                this.axios.post("/kafka/createTopic", this.topic).then((response) => {
                    this.$message.success("创建topic成功")
                    this.getTopics()
                }).catch((error) => {
                    this.$message.error("创建topic失败")
                })
            },
            deleteConfirm(topic) {
                this.axios.post("/kafka/deleteTopic",
                    {"broker": this.broker, "topic": topic}).then((response) => {
                    this.$message.success("删除topic成功")
                    this.getTopics()
                }).catch((error) => {
                    this.$message.error("删除topic失败")
                })
            },
            getTopicDetail(topic) {
                this.axios.post("/kafka/getTopicDetail",
                    {"broker": this.broker, "topic": topic}).then((response) => {
                    // this.$message.success(response.data)
                    this.partitions = response.data.partitions
                    this.topicDetal = response.data
                    this.dialogTableVisible = true
                }).catch((error) => {
                    this.$message.error("失败")
                })
            }
        },
        components: {
            kafkaSelect
        }
    }
</script>

<style scoped>
    i {
        font-size: 14px;
    }

    div {
        margin: 5px 0;
    }
</style>
