<template>
    <div>
        <kafkaSource></kafkaSource>
        <el-select v-model="topic" placeholder="选择topic">
            <el-option v-for="item in topics" :key="item" :label="item" :value="item"></el-option>
        </el-select>
        <h2> 生产消息</h2>
        <el-input type="textarea" size="medium" rows="10" maxlength="3000" show-word-limit></el-input>

        <consumer></consumer>

    </div>
</template>

<script>
    import consumer from '@/components/consumer.vue'
    import kafkaSource from '@/components/kafkaSource.vue'

    export default {
        name: "operate",
        data() {
            return {
                broker: null,
                sources: [],
                topic: null,
                topics: ["aa", "bb"]

            }
        },

        components: {
            consumer, kafkaSource
        }
    }
</script>

<style scoped>

</style>
