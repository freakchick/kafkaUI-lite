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
            active-text-color="#ffd04b">
          <el-submenu index="1">
            <template slot="title">zookeeper</template>
            <el-menu-item index="1-1">
              <router-link to="/zk/config" class="menu-item">配置</router-link>
            </el-menu-item>
            <el-menu-item index="1-2">
              <router-link to="/zk/manage" class="menu-item">管理</router-link>
            </el-menu-item>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title">kafka</template>
            <el-menu-item index="2-1">
              <router-link to="/config" class="menu-item">配置</router-link>
            </el-menu-item>
            <el-menu-item index="2-2">
              <router-link to="/manage" class="menu-item">管理</router-link>
            </el-menu-item>
            <el-menu-item index="2-3">
              <router-link to="/operate" class="menu-item">操作</router-link>
            </el-menu-item>
            <el-submenu index="2-4">
              <template slot="title">操作</template>
              <el-menu-item index="2-4-1">生产</el-menu-item>
              <el-menu-item index="2-4-2">
                <router-link to="/operate" class="menu-item">消费</router-link>
              </el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">redis</template>
            <el-menu-item index="3-1">
              <router-link to="/redis/config" class="menu-item">配置</router-link>
            </el-menu-item>
            <el-menu-item index="3-2">
              <router-link to="/redis/manage" class="menu-item">管理</router-link>
            </el-menu-item>
          </el-submenu>
        </el-menu>

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
color:#ffffff;


}

</style>
