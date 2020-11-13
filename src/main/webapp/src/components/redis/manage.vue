<template>
  <div>
    <el-select v-model="sourceId" placeholder="选择redis" @change="selectSource">
      <el-option v-for="item in sources" :key="item.name" :label="item.name" :value="item.id"></el-option>
    </el-select>
    <el-select v-model="db" placeholder="选择db" @change="selectDb" :disabled="sourceId == null">
      <el-option v-for="item in dbs" :key="item" :label="'db'+item" :value="item"></el-option>
    </el-select>

    <el-select v-model="key" placeholder="选择key" @change="selectKey" :disabled="db == null">
      <el-option v-for="item in keys" :key="item" :label="item" :value="item"></el-option>
    </el-select>


    <span>数据：</span>
    <div v-if="keyType == 'string'">{{ value }}</div>
    <div v-if="keyType == 'hash'">{{ value }}</div>
    <div v-if="keyType == 'set'">{{ value }}</div>
    <div v-if="keyType == 'list'">{{ value }}</div>

  </div>
</template>

<script>
export default {
  name: "manage",
  data() {
    return {
      sources: [],
      dbs: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15],
      keys: ['key1','key2'],
      key: null,
      db: null,
      sourceId: null,
      keyType: 'string',
      value: null

    }
  },
  created() {
    this.getAllSource()
  },
  methods: {
    getAllSource(){
      this.axios.post("/redis/getAllSource/").then((response) => {
        this.sources = response.data
      }).catch((error) => {
      })
    },
    selectSource(){
      this.db = null
      this.key = null
    },
    selectDb() {
      this.key = null
      this.getAllkeys()
    },
    selectKey() {
      this.getData()
    },
    getAllkeys() {
      this.axios.post("/redis/getAllKeys", {"sourceId": this.sourceId, "db": this.db}).then((response) => {
        this.keys = response.data
      }).catch((error) => {
      })
    },
    getData() {
      this.axios.post("/redis/getData", {"sourceId": sourceId, "db": db, "key": key}).then((response) => {
        this.keyType = response.data.type
        const data = response.data.value
        if (Object.prototype.toString.call(data) === "[object Array]") {
          console.log('value是数组');
        } else if (Object.prototype.toString.call(data) === '[object Object]') {
          console.log('value是对象');
        } else if (Object.prototype.toString.call(data) === '[object String]') {
          console.log('value是string')
        }
      }).catch((error) => {
      })
    }
  },
}
</script>

<style scoped>

</style>
