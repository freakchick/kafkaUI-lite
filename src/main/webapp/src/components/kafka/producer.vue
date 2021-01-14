<template>
  <div>

    <el-tooltip class="item" effect="dark" content="上键：向上翻输入历史; 下键：向下翻输入历史; ctrl + enter : 发送消息" placement="top-start">
      <i class="iconfont icon-jianpan"></i>
    </el-tooltip>

    <el-input type="textarea" v-model="message" size="medium" rows="6"
              placeholder="请输入消息内容"
              :autosize="{ minRows: 6, maxRows: 15 }"
              @keyup.enter.native="keyDown"
              @keyup.up.native="scrollUpHistory" @keyup.down.native="scrollDownHistory"
              show-word-limit>
    </el-input>
    <el-button type="primary" @click="produce" style="margin: 5px 0"><i class="iconfont icon-Send"></i> 发送</el-button>


    <div class="frame">
      <div class="left">
        <i class="el-icon-delete" @click="clear"></i>
      </div>
      <div class="right" ref="history">
        <p v-for="item in messages" class="history">
          <i class="el-icon-circle-check success"></i> &nbsp;&nbsp;{{ item }}</p>
      </div>
    </div>

<!--    <el-checkbox v-model="batch">多行內容切分成多条消息批量发送</el-checkbox>-->

     </div>
</template>

<script>
export default {
  name: "producer",
  data() {
    return {
      message: null,
      messages: [],
      history: [],
      cursor: null,
      batch: false
    }
  },
  props: ["topic", "broker"],
  methods: {
    keyDown(e) {
      if (e.ctrlKey && e.keyCode == 13) {   //用户点击了ctrl+enter触发
        this.produce()
      } else { //用户点击了enter触发

      }
    },
    //键盘按上键翻滚历史
    scrollUpHistory() {

      this.message = this.history[this.cursor]
      this.cursor--
      if (this.cursor < 0) {
        this.cursor = this.history.length - 1
      }
    },
    scrollDownHistory() {

      this.cursor++
      if (this.cursor >= this.history.length) {
        this.cursor = 0
      }
      this.message = this.history[this.cursor]

    },
    clear() {
      this.messages = []
    },
    scroll() {
      this.$nextTick(() => {
        this.$refs.history.scrollTop = 10000;
      })
    },
    processHistory(message) {
      this.history.push(message)
      if (this.history.length > 5) {
        this.history = this.history.slice(-5)
      }
      this.cursor = this.history.length - 1
    },
    produce() {
      if (this.broker == null || this.broker == '' || this.topic == null || this.topic == '') {
        this.$message({
          showClose: true,
          message: '请先选择kafka和topic',
          type: 'error'
        });
        return
      }

      if (this.message == '' || this.message == null){
        this.$message({
          showClose: true,
          message: '禁止发送空消息',
          type: 'error'
        });
        return
      }

      const m = this.message
      this.message = null
      this.axios.post("/kafka/produce", {
        "broker": this.broker,
        "topic": this.topic,
        "message": m,
        "batch": this.batch
      }).then((response) => {
        this.messages.push(m)
        this.processHistory(m)

        this.scroll()
      }).catch((error) => {
      })
    }
  }
}
</script>

<style scoped lang="scss">

.frame {
  display: flex;
  height: 400px;
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
      padding: 4px 0;
      color: #151313;
    }

    i:hover {
      background-color: #ababa7;
    }

    .active {
      background-color: #ababa7;
    }
  }

  .right {
    overflow-y: scroll;
    width: 100%;
    background-color: #FBF7F7;

    p {
      margin: 3px;
      background-color: #f8e3bd;

      .success {
        color: #42b983;
        font-weight: 900;
      }
    }
  }
}


</style>