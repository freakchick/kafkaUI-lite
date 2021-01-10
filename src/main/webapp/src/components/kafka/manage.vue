<template>
    <div>
        <el-tabs v-model="activeName" type="card" >
            <el-tab-pane label="topic管理" name="topic">
                <topic></topic>
            </el-tab-pane>
            <el-tab-pane label="集群管理" name="cluster">
                <cluster></cluster>
            </el-tab-pane>
            <el-tab-pane label="group管理" name="group">
                <group></group>
            </el-tab-pane>
        </el-tabs>


    </div>
</template>

<script>
    import topic from '@/components/kafka/manage/topic'
    import cluster from '@/components/kafka/manage/cluster'
    import group from '@/components/kafka/manage/group'

    export default {
        name: "manage",
        data() {
            return {

                activeName: "topic"

            }
        },
        created() {
        },
        methods: {

        },
        components: {
            topic,cluster,group
        }
    }
</script>

<style scoped>

</style>
