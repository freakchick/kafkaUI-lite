<template>
  <div>

    <el-button type="primary" plain @click="save" style="margin-bottom: 5px">保存</el-button>

    <el-table :data="sources" stripe border>
      <el-table-column prop="name" label="name"></el-table-column>
      <el-table-column label="权限">

        <template slot-scope="scope">
          <el-checkbox label="新增" v-model="scope.row.auth.add"></el-checkbox>
          <el-checkbox label="修改" v-model="scope.row.auth.update"></el-checkbox>
          <el-checkbox label="删除" v-model="scope.row.auth.remove"></el-checkbox>
          <el-checkbox label="查询" checked disabled></el-checkbox>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

  export default {
    data() {
      return {
      }
    },
    props: ["sources"],
    methods: {
      save() {
        let p = {}
        for (let item of this.sources) {
          p[item.id] = item.auth
        }
        this.$emit('save', p)
      }
    },
    created() {
    }
  }
</script>

<style scoped>

</style>