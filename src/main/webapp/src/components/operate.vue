<template>
    <div>
        <el-form label-width="80px">
            <el-form-item label="kafka">
                <kafkaSelect @kafkaChange="getTopics"></kafkaSelect>
            </el-form-item>
            <el-form-item label="topic">
                <el-select v-model="topic" placeholder="选择topic" clearable>
                    <el-option v-for="item in topics" :key="item.name" :label="item.name"
                               :value="item.name"></el-option>
                </el-select>
            </el-form-item>
        </el-form>

        <h5> 生产消息</h5>
        <el-input type="textarea" v-model="message" size="medium" rows="4" maxlength="3000" show-word-limit></el-input>
        <el-button @click="produce">发送</el-button>

        <consumer :broker="broker" :topic="topic"></consumer>

    </div>
</template>

<script>
    import consumer from '@/components/consumer.vue'
    import kafkaSelect from '@/components/kafkaSelect.vue'

    export default {
        name: "operate",
        data() {
            return {
                broker: null,
                sources: [],
                topic: null,
                topics: [],
                message: null
            }
        },
        methods: {
            getTopics(broker) {
                this.broker = broker
                console.log(broker);
                this.axios.post("/getTopics", {"brokers": this.broker}).then((response) => {
                    this.topics = response.data
                }).catch((error) => {
                })
            },
            produce() {
                this.axios.post("/produce", {
                    "broker": this.broker,
                    "topic": this.topic,
                    "message": this.message
                }).then((response) => {
                    this.topics = response.data
                }).catch((error) => {
                })
            }
        },
        components: {
            consumer, kafkaSelect
        }
    }
</script>

<style scoped>

</style>
