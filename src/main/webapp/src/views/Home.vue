<template>
  <div class="home">
    <el-container>
      <el-header>
        <router-link to="/config">配置</router-link>
        <router-link to="/manage">管理</router-link>
        <router-link to="/operate">操作</router-link>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
<!--      <el-footer>Footer</el-footer>-->
    </el-container>
  </div>
</template>

<script>
// @ is an alias to /src


export default {
  data () {
    return {
      msg: "我是home 组件"
    }
  },
  name: 'Home'
}
</script>

<style>

</style>
