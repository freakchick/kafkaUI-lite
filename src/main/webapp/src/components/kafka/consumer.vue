<template>
  <div>


    <!--        <el-input v-model="group" placeholder="请输入group" ></el-input>-->
    <div>
      <el-autocomplete :disabled="disabled" v-model="group" :fetch-suggestions="getGroupByTopic"
                       placeholder="请输入消费group"></el-autocomplete>
    </div>

    <div>
      <el-radio v-model="mode" label="earliest" :disabled="disabled">历史消息(earliest)</el-radio>
      <el-radio v-model="mode" label="latest" :disabled="disabled">最新消息(latest)</el-radio>
    </div>

    <div style="margin: 10px 0;display: flex">

      <el-checkbox label="根据关键字过滤显示消息" v-model="filter" style="line-height: 40px"></el-checkbox>
      <el-input v-model="keyword" placeholder="请输入关键字" style="width: 300px;margin-left: 10px"></el-input>
    </div>


    <div class="frame">
      <div class="left">
        <i class="iconfont icon-start" v-if="!on" @click="start" style="color:#12b812"></i>
        <i class="iconfont  icon-stopiconcopy" v-if="on" @click="stop" style="color: #f83b3b"></i>
        <i :class='["iconfont icon-dibu", {"active" :autoScrollToBottom }]' @click="autoScroll"></i>
        <i :class='["iconfont icon-huanhang", {"active" :autoBreak }]' @click="autoChangeLine"></i>
        <i class="el-icon-delete" @click="clear"></i>

      </div>
      <div class="right" ref="frame">
        <p v-for="item in message" :class="['history', {'autoBreak':autoBreak}]">
          <i class="iconfont icon-jiedian-shell shell"></i>
          {{ item }}
        </p>
        <p><i class="el-icon-loading" v-if="on"></i></p>
      </div>
    </div>
    <div >
      <data-tag :right="consumeCount" left="已消费消息条数" ></data-tag>
    </div>
  </div>
</template>

<script>
import dataTag from '@/components/common/dataTag.vue'
import kafkaSelect from "@/components/kafka/kafkaSelect";

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
      autoScrollToBottom: true,
      autoBreak: true,
      filter: false,
      keyword: null,
      consumeCount:0
    }
  },
  created() {
    this.getAddress()
  },
  props: ["topic", "broker", "sourceId"],
  watch: {
    message() {
      if (this.autoScrollToBottom) {
        this.$nextTick(() => {
          this.$refs.frame.scrollTop = this.$refs.frame.scrollHeight
        })
      }
    }
  },
  methods: {
    autoScroll() {
      this.autoScrollToBottom = !this.autoScrollToBottom
      if (this.autoScrollToBottom) {
        this.$nextTick(() => {
          this.$refs.frame.scrollTop = this.$refs.frame.scrollHeight
        })
      }
    },
    autoChangeLine() {
      this.autoBreak = !this.autoBreak
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

      this.consumeCount = 0
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
      this.axios.post("/kafka/getIp").then((response) => {
        // console.log(response.data);
        this.address = response.data
      }).catch((error) => {
      })
    },
    getGroupByTopic(queryString, cb) {
      // console.log(queryString)
      if (this.broker != null && this.topic != null && this.broker != '' && this.topic != '') {
        this.axios.post("/kafka/getGroupByTopic", {
          "broker": this.broker, "topic": this.topic
        }).then((response) => {
          if (response.data.success)
            cb(response.data.data)
          else {
            cb([])
          }
        }).catch((error) => {
          cb([])
        })
      } else {
        cb([])
      }
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
        this.consumeCount = this.consumeCount + 1
        if (this.filter && event.data.indexOf(this.keyword) == -1) {
          return
        }

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
  },
  components: {dataTag}
}
</script>

<style scoped lang="scss">

.frame {
  display: flex;
  height: 500px;
  max-height: 700px;
  border-radius: 2px;
  border: black 1px solid;

  .left {
    width: 30px;
    font-size: 30px;
    border-right: #8c939d 1px solid;
    background-color: #EDEBEB;
    display: flex;
    flex-direction: column;

    i {
      font-size: 20px;
      text-align: center;
      font-weight: 900;
      margin: 1px;
      color: #151313;
      border-radius: 4px;
    }

    i:hover {
      background-color: #cacac6;

    }

    .active {
      background-color: #ababa7;
    }
  }

  .right {
    overflow-y: scroll;

    width: 100%;
    background-color: #FBF7F7;

    .history {
      margin: 3px;
      background-color: #f8e3bd;

      .shell {
        font-weight: 900;
        background-color: #8c8b8b;
        margin-right: 3px;
      }

    }

    .autoBreak {
      overflow-wrap: break-word;
    }


  }
}


</style>
