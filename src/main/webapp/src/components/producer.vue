<template>
  <div>
    <h5> 生产消息</h5>

    <div class="frame" >
      <div class="left">
        <i class="el-icon-delete" @click="clear"></i>
      </div>
      <div class="right" ref="history">
        <p v-for="item in messages" class="history">
          <i class="el-icon-circle-check"></i> &nbsp;&nbsp;{{ item }}</p>
      </div>
    </div>

    <el-checkbox v-model="batch">多行內容切分成多条消息批量发送</el-checkbox>

    <el-input type="textarea" v-model="message" size="medium" rows="4" @keyup.enter.native="keyDown"
              @keyup.up.native="scrollUpHistory" @keyup.down.native="scrollDownHistory" maxlength="3000"
              show-word-limit>
    </el-input>
    <el-button @click="produce"><i class="iconfont icon-Send"></i>  发送</el-button>
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
    processHistory() {
      this.history.push(this.message)
      if (this.history.length > 5) {
        this.history = this.history.slice(-5)
      }
      this.cursor = this.history.length -1
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

      this.axios.post("/produce", {
        "broker": this.broker,
        "topic": this.topic,
        "message": this.message,
        "batch": this.batch
      }).then((response) => {
        this.messages.push(this.message)
        this.processHistory()
        this.message = null
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
  height: 200px;
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
    }
  }
}


</style>