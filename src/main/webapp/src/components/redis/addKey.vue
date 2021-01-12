<template>
  <el-form label-width="60px">
    <el-form-item label="key:">
      <el-input v-model="addedKey"></el-input>
    </el-form-item>
    <el-form-item label="类型:">
      <el-radio v-model="addedKeyType" label="string">string</el-radio>
      <el-radio v-model="addedKeyType" label="hash">hash</el-radio>
      <el-radio v-model="addedKeyType" label="set">set</el-radio>
      <el-radio v-model="addedKeyType" label="list">list</el-radio>
    </el-form-item>

    <el-form-item label="值:">
      <el-input v-if="addedKeyType=='string'" v-model="stringValue"></el-input>
      <div v-if="addedKeyType=='hash'">
        <el-table :data="addedHashValue" stripe border size="mini" :show-header="false" empty-text="">
          <template slot="empty">
            <p></p>
          </template>
          <el-table-column label="hash key">
            <template slot-scope="scope">
              <el-input v-model="scope.row.key" placeholder="输入hash key"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="hash value">
            <template slot-scope="scope">
              <el-input v-model="scope.row.value" placeholder="输入hash value"></el-input>
            </template>
          </el-table-column>

          <el-table-column label="" width="50">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" circle @click="deleteHashRow(scope.$index)"
                         icon="el-icon-delete">
                <!--                    <i class="el-icon-delete"></i>-->
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-button size="mini" type="primary" circle @click="addHashRow">
          <i class="el-icon-circle-plus-outline"></i>
        </el-button>
      </div>

      <div v-if="addedKeyType=='list' || addedKeyType=='set'">
        <el-table :data="addedListValue" stripe border size="mini" :show-header="false" empty-text="">
          <template slot="empty">
            <p></p>
          </template>
          <el-table-column label="value">
            <template slot-scope="scope">
              <el-input v-model="scope.row.value" placeholder="输入值"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="" width="50">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" circle @click="deleteListRow(scope.$index)"
                         icon="el-icon-delete">
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button size="mini" type="primary" circle @click="addListRow">
          <i class="el-icon-circle-plus-outline"></i>
        </el-button>
      </div>
    </el-form-item>

  </el-form>
</template>

<script>
export default {
  name: "addKey",
  data() {
    return {

      addedKey: null,
      addedKeyType: "string",
      stringValue: null,
      addedHashValue: [],
      addedListValue: []
    }
  },
  // props: ["sourceId", "db"],
  computed: {
    // sourceId2() {
    //   return this.sourceId
    // },
    // db2() {
    //   return this.db
    // }
  },
  methods: {
    addHashRow() {
      this.addedHashValue.push({key: null, value: null})
    },
    addListRow() {
      this.addedListValue.push({value: null})
    },
    deleteHashRow(index) {
      this.addedHashValue.splice(index, 1)
    },
    deleteListRow(index) {
      this.addedListValue.splice(index, 1)
    },
    commit(sourceId, db) {
      console.log(sourceId, db)
      let data;
      if (this.addedKeyType == 'hash') {
        data = JSON.stringify(this.addedHashValue)
      } else if (this.addedKeyType == 'list' || this.addedKeyType == 'set') {
        data = JSON.stringify(this.addedListValue)
      } else if (this.addedKeyType == 'string') {
        data = this.stringValue
      }
      this.axios.post("/redis/addKey", {
        "sourceId": sourceId,
        "db": db,
        "key": this.addedKey,
        "type": this.addedKeyType,
        "value": data
      }).then((response) => {
        if (response.data.success) {
          this.$message.success(response.data.message)
          this.$emit('addSuccess');
        } else {
          this.$message.error(response.data.message)
        }

      }).catch((error) => {
        this.$message.error("添加redis key失败")
      })
    }
  }
}
</script>

<style scoped>

</style>