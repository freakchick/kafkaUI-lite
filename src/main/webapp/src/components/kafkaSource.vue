<template>
    <el-select v-model="broker" placeholder="选择kafka">
        <el-option v-for="item in sources" :key="item.name" :label="item.name" :value="item.broker"></el-option>
    </el-select>
</template>

<script>
    export default {
        name: "kafkaSource",
        data() {
            return {
                broker: null,
                sources: []
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
            }
        }
    }
</script>

<style scoped>

</style>
