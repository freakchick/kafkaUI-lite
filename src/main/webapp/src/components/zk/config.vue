<template>
    <div>
        <el-table :data="sources" stripe border>
            <el-table-column prop="name" label="集群名称"></el-table-column>
            <el-table-column prop="address" label="地址"></el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button size="mini" type="danger"
                               @click="handleDelete(scope.$index, scope.row)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>


        <el-button type="primary" @click="dialogFormVisible = true" style="margin-top: 5px">添加集群</el-button>

        <el-dialog title="添加zookeeper地址" :visible.sync="dialogFormVisible" width="600px">
            <el-form label-width="80px">
                <el-form-item label="集群名称">
                    <el-input v-model="name"></el-input>
                </el-form-item>
                <el-form-item label="地址">
                    <el-input v-model="address"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogFormVisible = false;add()">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                address: '127.0.0.1:2181',
                sources: [],
                name: null,
                dialogFormVisible: false
            }
        },
        created() {
            this.getAllSource()
        },
        methods: {
            deleteSource(id) {
                this.axios.post("/zookeeper/deleteSource/" + id).then((response) => {
                    this.sources = response.data
                    this.getAllSource()
                }).catch((error) => {
                })
            },
            handleDelete(index, row) {
                console.log(index, row);
                this.deleteSource(row.id)
            },
            getAllSource() {
                this.axios.post("/zookeeper/getAllSource").then((response) => {
                    this.sources = response.data
                }).catch((error) => {
                    this.$message.error("查询所有zk环境失败")
                })
            },
            add() {
                this.axios.post("/zookeeper/add", {"name": this.name, "address": this.address}).then((response) => {
                    this.sources = response.data
                    this.getAllSource()
                }).catch((error) => {
                    this.$message.error("添加zk环境失败")
                })
            }
        }
    }
</script>

<style scoped>

</style>
