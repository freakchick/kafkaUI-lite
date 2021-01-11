<template>
  <div>
    <el-select v-model="sourceId" placeholder="选择redis" @change="selectSource" class="select">
      <el-option v-for="item in sources" :key="item.name" :label="item.name" :value="item.id"></el-option>
    </el-select>
    <el-select v-model="db" placeholder="选择db" @change="selectDb" :disabled="sourceId == null" class="select">
      <el-option v-for="item in dbs" :key="item" :label="'db'+item" :value="item"></el-option>
    </el-select>

    <el-select v-model="rediskey" filterable placeholder="选择key" @change="selectKey" :disabled="db == null"
               class="select">
      <el-option v-for="item in keys" :key="item" :label="item" :value="item"></el-option>
    </el-select>

    <div>
      <el-button size="small" type="primary" @click="clickAdd" circle>
        <i class="el-icon-circle-plus-outline" title="添加redis key"></i>
      </el-button>
      <el-button size="small" type="danger" @click="" circle>
        <i class="el-icon-delete" title="删除redis key"></i>
      </el-button>
      <el-button size="small" type="warning" @click="" circle>
        <i class="el-icon-edit"></i>
      </el-button>
      <el-button size="small" type="primary" @click="getData" circle>
        <i class="el-icon-refresh-right" title="刷新数据"></i>
      </el-button>
    </div>

    <div v-show="rediskey != null">

      <!--      <el-button type="primary" circle size="mini" icon="el-icon-refresh-right" @click="getData" style="margin-top: 5px">-->
      <!--      </el-button>-->
      <div class="type">数据类型：{{ keyType }}</div>
      <div class="type">key：{{ rediskey }}</div>

      <div v-if="keyType == 'string'">
        <el-input type="textarea" v-model="value" autosize show-word-limit>
        </el-input>
<!--        <el-button type="primary" round icon="el-icon-edit" @click="setData" style="margin-top: 5px">修改数据-->
<!--        </el-button>-->

      </div>

      <div v-if="keyType == 'hash'">
        <el-table :data="value" stripe border>
          <el-table-column prop="key" label="hash key"></el-table-column>
          <el-table-column prop="value" label="value"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" circle icon="el-icon-delete"
                         @click="handleDelete(scope.$index, scope.row)">
              </el-button>
            </template>
          </el-table-column>
        </el-table>

      </div>
      <div v-if="keyType == 'set' || keyType == 'list'">
        <el-table :data="value" stripe border>
          <el-table-column prop="value" label="value"></el-table-column>
        </el-table>
      </div>
    </div>

    <el-dialog title="添加redis key" :visible.sync="dialogFormVisible">
      <add-key ref="add"></add-key>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false;addRedisKey()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import addKey from '@/components/redis/addKey'

export default {
  name: "manage",
  data() {
    return {
      sources: [],
      dbs: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15],
      keys: [],
      rediskey: null,
      db: null,
      sourceId: null,
      keyType: null,
      value: null,
      hashValue: [],
      listValue:[],
      dialogFormVisible: false,
      addedKey: null,
      addedKeyType: "string",
      addedHashValue: [],
      addedListValue: []
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
        this.$message.error("查询所有redis环境失败")
      })
    },
    selectSource() {
      this.db = null
      this.rediskey = null
    },
    selectDb() {
      this.rediskey = null
      this.hashValue = []
      this.getAllkeys()
      console.log(this.rediskey)
    },
    selectKey() {
      console.log("selectKey");
      this.getData()
    },
    getAllkeys() {
      this.axios.post("/redis/getAllKeys", {"sourceId": this.sourceId, "db": this.db}).then((response) => {
        this.keys = response.data
      }).catch((error) => {
        this.$message.error("查询所有key失败")
      })
    },
    getData() {
      this.axios.post("/redis/getData", {
        "sourceId": this.sourceId,
        "db": this.db,
        "key": this.rediskey
      }).then((response) => {
        this.keyType = response.data.type
        this.value = response.data.value
        // if (response.data.type === 'hash') {
        //   this.hashValue = []
        //   for (var k in this.value) {
        //     this.hashValue.push({'key': k, 'value': this.value[k]})
        //   }
        // }



      }).catch((error) => {
        this.$message.error("查询redis数据失败")
      })
    },
    setData() {

    },
    clickAdd() {
      if (this.sourceId == null || this.db == null) {
        this.$message.error("请先选择redis环境和db号")
        return
      }
      this.dialogFormVisible = true
    },
    addRedisKey() {
      this.$refs.add.commit(this.sourceId, this.db)
    }

  },
  components: {
    addKey
  }
}
</script>

<style scoped>

.select {
  padding-right: 5px;
  padding-bottom: 5px;
  width: 200px;
}

.type {
  padding: 5px 0;
  margin: 5px 0;
  /*background-color: #8cc5ff;*/
  font-size: 18px;
}

.stringValue {
  padding: 5px;
  margin: 5px 0;
  border: 1px solid black;
  background-color: blanchedalmond;
  height: 300px;
}
</style>
