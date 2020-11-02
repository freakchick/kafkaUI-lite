<template>
  <div>
    <h2> 消费消息</h2>
    <el-form label-width="100px" width="200px">
      <el-form-item label="groupId：">
        <el-input v-model="group" placeholder="请输入group" :disabled="disabled"></el-input>
      </el-form-item>
      <el-form-item label="消费模式：">
        <el-radio v-model="mode" label="earliest" :disabled="disabled">历史消息</el-radio>
        <el-radio v-model="mode" label="latest" :disabled="disabled">最新消息</el-radio>
      </el-form-item>
    </el-form>


    <!--        <el-switch v-model="on" active-text="开始消费" inactive-text="停止" active-color="#13ce66"-->
    <!--                   inactive-color="#ff4949" @change="start"></el-switch>-->

    <div class="frame" ref="frame">
      <div class="left">
        <i class="iconfont icon-start" v-if="!on" @click="start" style="color:#12b812"></i>
        <i class="iconfont  icon-stopiconcopy" v-if="on" @click="stop" style="color: #f83b3b"></i>
        <i :class='["iconfont icon-dibu", {"active" :autoScrollToBottom }]' @click="autoScroll" style="color: #514c4c"></i>
        <i class="iconfont icon-huanhang" @click="autoChangeLine" style="color: #4c4747"></i>
        <i class="el-icon-delete" @click="clear" style="color: #f64646"></i>

      </div>
      <div class="right">
        <p v-for="item in message" class="history">
          <i class="iconfont icon-jiedian-shell"></i>{{ item }}</p>
        <p><i class="el-icon-loading" v-if="on" style="color: #13ce66"></i></p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "consumer.vue",
  data() {
    return {
      mode: "earliest",
      on: false,
      group: null,
      address: null,
      message: [],
      websocket: null,
      disabled: false,
      autoScrollToBottom: false
    }
  },
  created() {
    this.getAddress()
  },
  props: ["topic", "broker"],
  methods: {
    autoScroll() {
      this.autoScrollToBottom = !this.autoScrollToBottom
    },
    autoChangeLine() {

    },
    clear() {
      this.message = []
    },
    start() {
      if (this.broker == null || this.broker == '' || this.topic == null || this.topic == '') {
        this.$message({
          showClose: true,
          message: '请先选择kafka和topic',
          type: 'error'
        });
        return
      }
      if (this.group == null || this.group == '') {
        this.$message({
          showClose: true,
          message: '请先输入group',
          type: 'error'
        });
        return
      }

      this.disabled = true
      this.on = true

      if ('WebSocket' in window) {

        let url = `ws://${this.address}/push/websocket?topic=${this.topic}&broker=${this.broker}&group=${this.group}&offset=${this.mode}`
        console.log(url)
        this.websocket = new WebSocket(url)
        this.initWebSocket()
      } else {
        alert('当前浏览器 不支持')
      }


    },
    stop() {
      this.disabled = false
      this.on = false
      this.websocket.close()
    },
    scroll() {
      this.$nextTick(() => {

        this.$refs.frame.scrollTop = 100000;
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
        // console.log('服务端返回：' + event.data)
        this.message.push(event.data)
        if (this.autoScrollToBottom)
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
  display: flex;
  /*width: 600px;*/
  height: 200px;
  border-radius: 2px;
  /*background-color: blanchedalmond;*/
  /*color: #42b983;*/
  border: black 1px solid;

}

.left {
  width: 30px;
  font-size: 30px;
  border-right: #8c939d 1px solid;
  background-color: #EDEBEB;
  display: flex;
  flex-direction: column;
}

.left i {
  font-size: 20px;
  text-align: center;
  font-weight: 900;
  padding: 4px 0;
}

.left i:hover {
  background-color: #D6D5CF;
}

.left .active {
  background-color: #D6D5CF;
}

.right {
  /*background-color: #d9ecff;*/
  overflow-y: scroll;
  width: 100%;
  background-color: #FBF7F7;
}

.right p {
  /*background-color: #FAF9F5;*/
  margin: 3px;
}
</style>
