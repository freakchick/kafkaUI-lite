<template>
  <div class="home">
    <el-container>
      <el-header>

        <el-menu
            default-active="1"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
            background-color="#545c64"
            text-color="#fff"
            active-text-color="#ffd04b"
            router>
          <el-submenu index="1">
            <template slot="title">zookeeper</template>
            <el-menu-item index="/zk/config">配置</el-menu-item>
            <el-menu-item index="/zk/manage">管理</el-menu-item>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title">kafka</template>
            <el-menu-item index="/config">配置</el-menu-item>
            <el-menu-item index="/manage">管理</el-menu-item>
            <el-submenu index="2-4">
              <template slot="title">操作</template>
              <el-menu-item index="/kafka/produce">生产</el-menu-item>
              <el-menu-item index="/kafka/consume">消费</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">redis</template>
            <el-menu-item index="/redis/config">配置</el-menu-item>
            <el-menu-item index="/redis/manage">管理</el-menu-item>
          </el-submenu>
        </el-menu>

      </el-header>
      <el-main>
        <!--        <el-breadcrumb separator-class="el-icon-arrow-right">-->
        <!--          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>-->
        <!--          <el-breadcrumb-item>活动管理</el-breadcrumb-item>-->
        <!--          <el-breadcrumb-item>活动列表</el-breadcrumb-item>-->
        <!--          <el-breadcrumb-item>活动详情</el-breadcrumb-item>-->
        <!--        </el-breadcrumb>-->
        <router-view></router-view>
      </el-main>
      <!--      <el-footer>Footer</el-footer>-->
    </el-container>
  </div>
</template>

<script>
// @ is an alias to /src


export default {
  data() {
    return {
      msg: "我是home 组件"
    }
  },
  name: 'Home',
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    }
  }
}
</script>

<style scoped>
.menu-item {
  color: #ffffff;


}

</style>
