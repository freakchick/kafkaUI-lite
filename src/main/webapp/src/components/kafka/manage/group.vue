<template>
    <div>
        <kafkaSelect @kafkaChange="kafkaChange"></kafkaSelect>

        <el-table :data="tableData" stripe border >
            <el-table-column prop="name" label="group名称"></el-table-column>

            <el-table-column label="操作">
                <template slot-scope="scope">

                    <el-button size="mini" circle type="primary" @click="getGroupDetail(scope.row.name)">
                        <i class="iconfont icon-detail"></i>
                    </el-button>
                    <el-popconfirm title="确定删除吗？" @confirm="deleteConfirm(scope.row.name)" v-if="!scope.row.internal">
                        <el-button size="mini" circle type="danger" slot="reference" style="margin-left: 5px">
                            <i class="el-icon-delete"></i>
                        </el-button>
                    </el-popconfirm>


                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="group消费偏移量详情" :visible.sync="dialogTableVisible">
            <el-table :data="item" stripe border max-height="500" size="small" v-for="item in detail" style="margin: 5px 0">
                <el-table-column property="topic" label="topic"></el-table-column>
                <el-table-column property="partition" label="分区号"></el-table-column>
                <el-table-column property="offset" label="offset"></el-table-column>
            </el-table>
        </el-dialog>
    </div>
</template>

<script>
    import kafkaSelect from '@/components/kafka/kafkaSelect.vue'

    export default {
        name: "group",
        data() {
            return {
                broker: null,
                tableData: [],
                detail: [],
                dialogTableVisible: false

            }
        },
        methods: {
            getGroups() {
                this.axios.post("/kafka/group/all", {"broker": this.broker}).then((response) => {
                    this.tableData = response.data
                }).catch((error) => {
                    this.$message.error("查询所有group失败")
                })
            },
            kafkaChange(broker) {
                this.broker = broker
                this.getGroups()
            },
            getGroupDetail(group) {
                this.axios.post("/kafka/group/detail", {"broker": this.broker, "group": group}).then((response) => {
                    this.detail = response.data
                    this.dialogTableVisible = true
                }).catch((error) => {
                    this.$message.error("查询group详情失败")
                })
            },
            deleteConfirm(group){
                this.axios.post("/kafka/group/delete", {"broker": this.broker, "group": group}).then((response) => {
                    this.$message.success("删除group成功")
                    this.getGroups()
                }).catch((error) => {
                    this.$message.error("删除group失败")
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
</style>