//vuex 中的权限信息刷新

function initZK(that) {

  that.axios.post("/zookeeper/getAllSourceAuth").then((response) => {
    let obj = {}
    for (let item of response.data) {
      obj[item.id] = item.auth
    }
    console.log('我被调用成功')
    that.$store.commit('setZKAuth', obj)
  }).catch((error) => {
    that.$message.error("查询所有zookeeper权限失败")
  })
}

function initKafka(that) {
  that.axios.post("/kafka/getAllSourceAuth").then((response) => {
    let obj = {}
    for (let item of response.data) {
      obj[item.id] = item.auth
    }
    that.$store.commit('setKafkaAuth', obj)
  }).catch((error) => {
    that.$message.error("查询所有kafka权限失败")
  })
}

export {
  initZK, initKafka
}