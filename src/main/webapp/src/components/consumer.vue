<template>
  <div>
    <h2> 消费消息</h2>
    <el-radio v-model="mode" label="earliest" :disabled="disabled">历史消息</el-radio>
    <el-radio v-model="mode" label="latest" :disabled="disabled">最新消息</el-radio>

    <el-switch v-model="on" active-text="开始消费" inactive-text="停止" active-color="#13ce66"
               inactive-color="#ff4949" @change="start"></el-switch>
    <el-button type="danger" icon="el-icon-delete" @click="clear" circle></el-button>

    <el-input v-model="group" placeholder="请输入group" :disabled="disabled"></el-input>
    <!--    <el-input type="textarea" size="medium" rows="10" v-model="message" maxlength="3000" show-word-limit></el-input>-->
    <div class="frame" ref="frame">
      <p v-for="item in message"><i class="el-icon-d-arrow-right"></i> &nbsp;&nbsp;{{ item }}</p>
      <p><i class="el-icon-loading" v-if="on"></i></p>
    </div>
  </div>
</template>

<script>
export default {
  name: "consumer.vue",
  data() {
    return {

      mode: "latest",
      on: false,
      group: null,
      address: null,

      message: [],
      websocket: null,
      disabled: false
    }
  },
  created() {
    this.getAddress()
  },
  props: ["topic", "broker"],
  methods: {
    clear() {
      this.message = []
    },
    start() {
      this.disabled = !this.disabled
      console.log(this.on)
      if (this.on) {
        if ('WebSocket' in window) {

          let url = `ws://${this.address}/push/websocket?topic=${this.topic}&broker=${this.broker}&group=${this.group}&offset=${this.mode}`
          console.log(url)
          this.websocket = new WebSocket(url)
          this.initWebSocket()
        } else {
          alert('当前浏览器 不支持')
        }

      } else {
        this.websocket.close()
      }

    },
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
      this.websocket.onmessage = (event) => {
        // 根据服务器推送的消息做自己的业务处理
        console.log('服务端返回：' + event.data)
        this.message.push(event.data)
        this.scroll()
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
.frame {
  width: 600px;
  height: 200px;
  border-radius: 10px;
  background-color: blanchedalmond;
  color: #42b983;
  border: black 2px solid;
  overflow-y: scroll;
}
</style>
