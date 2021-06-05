<template>
  <div>

    <!--    <el-tooltip class="item" effect="dark" content="上键：向上翻输入历史; 下键：向下翻输入历史; ctrl + enter : 发送消息" placement="top-start">-->
    <!--      <i class="iconfont icon-jianpan"></i>-->
    <!--    </el-tooltip>-->

    <!--    <el-alert-->
    <!--        title="上键：向上翻输入历史(最多保存10条最近输入记录); 下键：向下翻输入历史; ctrl+enter: 发送消息"-->
    <!--        type="warning"-->
    <!--        show-icon>-->
    <!--    </el-alert>-->

    <el-checkbox v-model="batch"></el-checkbox>
    <el-tooltip class="item" effect="dark" content="按换行符（\n）分割成多条消息来发送" placement="top-start">
      <span>分割后批量发送</span>
    </el-tooltip>

    <el-input type="textarea" v-model="message" size="medium" rows="6"
              placeholder="上键：向上翻输入历史(最多保存10条最近输入记录); 下键：向下翻输入历史; ctrl+enter: 发送消息"
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
        <div v-for="item in messages" class="history">
          <div class="index">
            <div class="index-c">
              <i class="el-icon-circle-check success" v-if="item.success"></i>
              <i class="el-icon-circle-close fail" v-else></i>
              <i class="iconfont icon-single" v-if="!item.batch" title="单条消息"></i>
              <i class="iconfont icon-multi" v-else title="批量多条消息"></i>
            </div>
          </div>

          <div class="content">{{ item.content }}</div>
        </div>
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
      maxHistorySize: 10,
      cursor: null,
      batch: false
    }
  },
  props: ["topic", "sourceId"],
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
      if (this.history.length > this.maxHistorySize) {
        this.history = this.history.slice(-this.maxHistorySize)
      }
      this.cursor = this.history.length - 1
    },
    produce() {
      if (this.sourceId == null || this.sourceId == '' || this.topic == null || this.topic == '') {
        this.$message({
          showClose: true,
          message: '请先选择kafka和topic',
          type: 'error'
        });
        return
      }

      if (this.message == '' || this.message == null) {
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
        "sourceId": this.sourceId,
        "topic": this.topic,
        "message": m,
        "batch": this.batch
      }).then((response) => {
        this.messages.push({content: m, success: true, batch: this.batch})
        this.processHistory(m)

        this.scroll()
      }).catch((error) => {
        this.$message.error("发送失败")
        this.messages.push({content: m, success: false, batch: this.batch})
      })
    }
  }
}
</script>

<style scoped lang="scss">

.frame {
  display: flex;
  min-height: calc(100vh - 410px);

  //border: #06b176 1px solid;
  box-shadow: 1px 1px 5px #9e9e9e;

  .left {
    width: 30px;
    font-size: 30px;
    border-right: #7d7d7d 1px solid;
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

    .history {
      margin-bottom: 2px;
      display: flex;
      //margin: 3px;
      background-color: #f8e3bd;
      white-space: pre-line; //  字符串\n换行
      .index {
        width: 40px;
        background-color: #f8d79b;

        .index-c {
          width: 40px;

          .success {
            color: #42b983;
            font-weight: 900;
          }
          .fail{
            font-weight: 900;
            color: #ef1818;
          }
        }

      }

      .content {
        padding-left: 5px;
        overflow: hidden;
        overflow-wrap: break-word;
      }

    }
  }
}


</style>