<template>
    <div>
        <h2> 消费消息</h2>
        <el-radio v-model="mode" label="1">历史消息</el-radio>
        <el-radio v-model="mode" label="2">最新消息</el-radio>

        <el-switch v-model="on" active-text="开始消费" inactive-text="停止" active-color="#13ce66"
                   inactive-color="#ff4949" @change="onchange"></el-switch>

        <el-input v-model="group" placeholder="请输入group"></el-input>
        <el-input type="textarea" size="medium" rows="10" v-model="sender" maxlength="3000" show-word-limit></el-input>
    </div>
</template>

<script>
    export default {
        name: "consumer.vue",
        data() {
            return {
                broker: null,
                mode: "1",
                on: false,
                group: null,
                address: null,

                sender: null,
                websocket: null
            }
        },
        created() {
            this.getAddress()
        },
        methods: {
            onchange() {
                console.log(this.on)
                if (this.on) {
                    if ('WebSocket' in window) {
                        this.websocket = new WebSocket('ws://' + this.address + '/push/websocket')
                        this.initWebSocket()
                    } else {
                        alert('当前浏览器 不支持')
                    }
                } else {
                    this.websocket.close()
                }

            },
            getAddress() {
                this.axios.post("/getIp").then((response) => {
                    console.log(response.data);
                    this.address = response.data
                }).catch((error) => {
                })
            },
            initWebSocket() {
                // 连接错误
                this.websocket.onerror = () => {
                    console.log('WebSocket连接发生错误   状态码：' + this.websocket.readyState)
                }
                // 连接成功
                this.websocket.onopen = () => {
                    console.log('WebSocket连接成功    状态码：' + this.websocket.readyState)
                }
                // 收到消息的回调
                this.websocket.onmessage = function (event) {
                    // 根据服务器推送的消息做自己的业务处理
                    console.log('服务端返回：' + event.data)
                }
                // 连接关闭的回调
                this.websocket.onclose = () => {
                    console.log('WebSocket连接关闭    状态码：' + this.websocket.readyState)
                }
                // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
                window.onbeforeunload = () => {
                    this.websocket.close()
                }
            }
        }
    }
</script>

<style scoped>

</style>
