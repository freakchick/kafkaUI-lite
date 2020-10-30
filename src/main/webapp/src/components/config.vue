<template>
    <div>
        <el-table :data="sources">
            <el-table-column prop="name" label="名称" width="180"></el-table-column>
            <el-table-column prop="broker" label="地址" width="180"></el-table-column>
        </el-table>
        broker地址:
        <el-input v-model="name" placeholder="请输入名称"></el-input>
        <el-input v-model="broker" placeholder="请输入broker地址"></el-input>
        <el-button @click="add">添加</el-button>
    </div>
</template>

<script>
    export default {
        name: "config",
        data() {
            return {
                broker: null,
                sources: [],
                name: null
            }
        },
        created() {
            this.getAllSource()
        },
        methods: {
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
