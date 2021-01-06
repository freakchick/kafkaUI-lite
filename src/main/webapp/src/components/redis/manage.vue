<template>
  <div>
    <el-select v-model="sourceId" placeholder="选择redis" @change="selectSource" class="select">
      <el-option v-for="item in sources" :key="item.name" :label="item.name" :value="item.id"></el-option>
    </el-select>
    <el-select v-model="db" placeholder="选择db" @change="selectDb" :disabled="sourceId == null" class="select">
      <el-option v-for="item in dbs" :key="item" :label="'db'+item" :value="item"></el-option>
    </el-select>

    <el-select v-model="key" filterable placeholder="选择key" @change="selectKey" :disabled="db == null" class="select">
      <el-option v-for="item in keys" :key="item" :label="item" :value="item"></el-option>
    </el-select>


    <div>数据：</div>
    <el-button type="primary" @click="getData" style="margin-top: 5px">刷新数据</el-button>
    <div v-if="keyType != null">数据类型：{{ keyType }}</div>
    <div v-if="keyType == 'string'">{{ value }}</div>
    <div v-if="keyType == 'hash'">
      <div v-for="key in Object.keys(value)">
        <span class="cell">{{ key }}</span>
        <span class="cell">{{ value[key] }}</span>
      </div>
    </div>
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
      keys: [],
      key: null,
      db: null,
      sourceId: null,
      keyType: null,
      value: null

    }
  },
  created() {
    this.getAllSource()
  },
  methods: {
    getAllSource() {
      this.axios.post("/redis/getAllSource/").then((response) => {
        this.sources = response.data
      }).catch((error) => {
      })
    },
    selectSource() {
      this.db = null
      this.key = null
    },
    selectDb() {
      this.key = null
      this.getAllkeys()
    },
    selectKey() {
      console.log("selectKey");
      this.getData()
    },
    getAllkeys() {
      this.axios.post("/redis/getAllKeys", {"sourceId": this.sourceId, "db": this.db}).then((response) => {
        this.keys = response.data
      }).catch((error) => {
      })
    },
    getData() {
      this.axios.post("/redis/getData", {
        "sourceId": this.sourceId,
        "db": this.db,
        "key": this.key
      }).then((response) => {
        this.keyType = response.data.type
        this.value = response.data.value

        console.log(Object.keys(this.value))

      }).catch((error) => {
      })
    }
  },
}
</script>

<style scoped>
.cell {
  display: inline-block;
  border: 1px solid #82848a;
  padding: 5px;
}

.select {
  padding-right: 5px;
  padding-bottom: 5px;
  width: 200px;
}
</style>
