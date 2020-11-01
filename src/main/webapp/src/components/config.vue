<template>
    <div>
        <el-table :data="sources" stripe border>
            <el-table-column prop="name" label="名称" width="180"></el-table-column>
            <el-table-column prop="broker" label="地址" width="180"></el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button size="mini" type="danger"
                               @click="handleDelete(scope.$index, scope.row)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>


        <el-button @click="dialogFormVisible = true">添加</el-button>

        <el-dialog title="添加kafka地址" :visible.sync="dialogFormVisible" width="600px">
            <el-form label-width="80px">
                <el-form-item label="名称">
                    <el-input v-model="name"></el-input>
                </el-form-item>
                <el-form-item label="broker地址">
                    <el-input v-model="broker"></el-input>
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
        name: "config",
        data() {
            return {
                broker: null,
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
                this.axios.post("/deleteSource/" + id).then((response) => {
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
                this.axios.post("/getSource").then((response) => {
                    this.sources = response.data
                }).catch((error) => {
                })
            },
            add() {
                this.axios.post("/add", {"name": this.name, "broker": this.broker}).then((response) => {
                    this.sources = response.data
                    this.getAllSource()
                }).catch((error) => {
                })
            }
        }
    }
</script>

<style scoped>

</style>
