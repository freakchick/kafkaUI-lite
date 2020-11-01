<template>
    <div>
        <h5> 生产消息</h5>
        <div class="frame" ref="frame">
            <p v-for="item in messages"><i class="el-icon-circle-check"></i> &nbsp;&nbsp;{{ item }}</p>
        </div>
        <el-input type="textarea" v-model="message" @keyup.enter.native="produce"> size="medium" rows="4"
            maxlength="3000" show-word-limit>
        </el-input>
        <el-button @click="produce">发送</el-button>
    </div>
</template>

<script>
    export default {
        name: "producer",
        data() {
            return {
                message: null,
                messages: []
            }
        },
        props: ["topic", "broker"],
        methods: {
            scroll() {
                this.$nextTick(() => {
                    // setTimeout(() => {
                    //   var scrollTop = this.$el.querySelector('.scrolldivmain')
                    //   scrollTop.scrollTop = scrollTop.scrollHeight
                    // }, 13)
                    // var h = this.$refs.frame.scrollHeight();
                    this.$refs.frame.scrollTop = 10000;
                })
            },
            produce() {
                if (this.broker == null || this.broker == '' || this.topic == null || this.topic == '') {
                    this.$message({
                        showClose: true,
                        message: '请先选择kafka和topic',
                        type: 'error'
                    });
                }

                this.axios.post("/produce", {
                    "broker": this.broker,
                    "topic": this.topic,
                    "message": this.message
                }).then((response) => {
                    this.messages.push(this.message)
                    this.message = null
                    this.scroll()
                }).catch((error) => {
                })
            }
        }
    }
</script>

<style scoped>
    .frame {
        width: 600px;
        max-height: 200px;
        /*border-radius: 10px;*/
        background-color: blanchedalmond;
        color: #42b983;
        /*border: black 2px solid;*/
        overflow-y: scroll;
    }
</style>