<template>
  <div>


    <!--        <el-input v-model="group" placeholder="请输入group" ></el-input>-->
    <div style="display: flex">
      <div class="label">group:</div>
      <el-autocomplete :disabled="disabled" v-model="group" :fetch-suggestions="getGroupByTopic"
                       placeholder="请输入消费group"></el-autocomplete>
    </div>

    <div>
      <el-radio v-model="mode" label="earliest" :disabled="disabled">历史消息(earliest)</el-radio>
      <el-radio v-model="mode" label="latest" :disabled="disabled">最新消息(latest)</el-radio>
    </div>

    <div style="margin: 10px 0;display: flex;">

      <span style="line-height: 40px">高亮显示关键字:</span>

      <el-input v-model="keyword" placeholder="请输入关键字" style="width: 300px;margin-left: 10px" clearable></el-input>
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
        <div v-for="(item,index) in message" class='history'>
          <div class="index">
            <div class="index-c"> {{ index + 1 }}</div>


          </div>
          <div :class="['message', {'autoBreak':autoBreak}]" v-html="getContent(item)"></div>

        </div>
        <p><i class="el-icon-loading" v-if="on"></i></p>
      </div>
    </div>
    <div>
      <data-tag :right="consumeCount" left="已消费消息条数"></data-tag>
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
      consumeCount: 0
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
    getContent(item) {
      if (this.keyword != '') {
        const p = item.split(this.keyword).join(`<span style="color: red;font-weight: 700;background-color: #F8B950;padding: 0 0px;">${this.keyword}</span>`)
        // console.log(p)
        return p
      } else {
        return item
      }
    },
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

        let url = `ws://${this.address}/push/websocket?topic=${this.topic}&sourceId=${this.sourceId}&group=${this.group}&offset=${this.mode}`
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
        // if (this.filter && event.data.indexOf(this.keyword) == -1) {
        //   return
        // }

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
.label {
  margin-right:  1px;
  padding: 0 10px;
  line-height: 40px;
  background-color: #06b176;
  color: #fff;
  height: 40px;
  border-radius: 3px;
  font-size: 16px;
  font-weight: 700;
}
.frame {
  display: flex;
  min-height: calc(100vh - 350px);
  max-height: 600px;
  box-shadow: 1px 1px 5px #72767b;
  margin-bottom: 5px;

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
      background-color: #fde6c0;
      display: flex;
      //flex-wrap: wrap;

      .index {
        //font-weight: 900;
        width: 25px;
        background-color: #f6bf6a;

        //margin-right: 3px;
      }

      .index-c {
        width: 25px;
        font-size: 10px;
      }

      .message {
        //background-color: #fde6c0;
        padding: 0 5px;
        white-space: pre-line; //  字符串\n换行

      }



      .autoBreak {
        overflow: hidden;
        overflow-wrap: break-word;
      }
    }

    //.autoBreak {
    //  overflow-wrap: break-word;
    //}


  }
}


</style>
