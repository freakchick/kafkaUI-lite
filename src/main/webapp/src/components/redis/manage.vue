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
      <el-button size="small" type="primary" @click="clickAdd" circle :disabled="!auth.add">
        <i class="el-icon-circle-plus-outline" title="添加redis key"></i>
      </el-button>
      <el-button size="small" type="danger" @click="clickDelete" circle :disabled="!auth.remove">
        <i class="el-icon-delete" title="删除redis key"></i>
      </el-button>
      <!--      <el-button size="small" type="warning" @click="" circle>-->
      <!--        <i class="el-icon-edit"></i>-->
      <!--      </el-button>-->
      <el-button size="small" type="primary" @click="getData" circle>
        <i class="el-icon-refresh-right" title="刷新数据"></i>
      </el-button>
    </div>

    <div v-show="rediskey != null">
      <div class="tag-group">
        <el-tag effect="light" type="warning" size="medium" :hit="true">{{ keyType }}</el-tag>
        <el-tag effect="light" type="primary" size="medium" :hit="true">{{ rediskey }}</el-tag>
      </div>
      <!--      <div class="type">key：{{ rediskey }}</div>-->
      <!--      <div class="type">数据类型：{{ keyType }}</div>-->

      <div v-if="keyType == 'string'">
        <el-input type="textarea" v-model="value" :autosize="{ minRows: 2, maxRows: 30 }" show-word-limit>
        </el-input>
        <!--        <el-button type="primary" round icon="el-icon-edit" @click="setData" style="margin-top: 5px">修改数据-->
        <!--        </el-button>-->

      </div>

      <div v-if="keyType == 'hash'">
        <el-table :data="value" stripe border max-height="600" size="mini">
          <el-table-column label="hash key">
            <template slot-scope="scope">
              <el-input v-model="scope.row.key"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="value">
            <template slot-scope="scope">
              <el-input v-model="scope.row.value"></el-input>
            </template>
          </el-table-column>
          <!--          <el-table-column label="操作">-->
          <!--            <template slot-scope="scope">-->
          <!--              <el-button size="mini" type="danger" circle icon="el-icon-delete"-->
          <!--                         @click="handleDelete(scope.$index, scope.row)">-->
          <!--              </el-button>-->
          <!--              <el-button size="mini" type="warning" @click="" circle>-->
          <!--                <i class="el-icon-edit"></i>-->
          <!--              </el-button>-->
          <!--            </template>-->
          <!--          </el-table-column>-->
        </el-table>

      </div>
      <div v-if="keyType == 'set'">
        <el-table :data="value" stripe border size="mini" max-height="600">
          <el-table-column label="members">
            <template slot-scope="scope">
              <el-input v-model="scope.row.value"></el-input>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div v-if="keyType == 'list'">
        <el-table :data="value" stripe border size="mini" max-height="600">
          <el-table-column label="value">
            <template slot-scope="scope">
              <el-input v-model="scope.row.value"></el-input>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <el-dialog title="添加redis key" :visible.sync="dialogFormVisible">
      <add-key ref="add" @addSuccess="getAllkeys"></add-key>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false;addRedisKey()">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="是否确认删除这个redis key" :visible.sync="show2">
      <div class="tag-group">
        <el-tag effect="light" type="danger" size="medium" :hit="true">db{{ db }}</el-tag>
        <el-tag effect="light" type="danger" size="medium" :hit="true">{{ rediskey }}</el-tag>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="show2 = false">取消</el-button>
        <el-button type="danger" @click="show2 = false;deleteRedisKey()">确定</el-button>
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
        listValue: [],
        dialogFormVisible: false,
        addedKey: null,
        addedKeyType: "string",
        addedHashValue: [],
        addedListValue: [],
        show2: false,
        auth: {}
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
        this.auth = this.$store.getters.getRedisAuth(this.sourceId)
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
      clickDelete() {
        if (this.sourceId == null || this.db == null) {
          this.$message.error("请先选择redis环境和db号")
          return
        }
        this.show2 = true
      },
      addRedisKey() {
        this.$refs.add.commit(this.sourceId, this.db)
      },
      deleteRedisKey() {
        this.axios.post("/redis/deleteKey", {
          "sourceId": this.sourceId,
          "db": this.db,
          "key": this.rediskey
        }).then((response) => {
          this.rediskey = null
          this.$message.success("删除redis key成功")
          this.getAllkeys()
        }).catch((error) => {
          this.$message.error("删除redis key失败")
        })
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

  .tag-group span {
    margin: 10px 10px 10px 0;
    font-size: 14px;
    font-weight: 700;
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
