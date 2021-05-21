<template>
  <div class="home">
    <el-container>
      <el-header style="padding: 0;">
        <div class="head">
          <div class="logo">KafkaUI-lite</div>

          <el-menu
              default-active="1"
              class="menu"
              mode="horizontal"
              @select="handleSelect"
              background-color="#08B0AA"
              text-color="#fff"
              active-text-color="#01293b"
              router>
            <el-submenu index="1">
              <template slot="title">
                          <span class="menu">
                          <i class="iconfont icon-kafka icon"/>kafka</span>
              </template>
              <el-menu-item index="/kafka/config">{{ $t('config') }}</el-menu-item>
              <el-menu-item index="/kafka/manage">{{ $t('manage') }}</el-menu-item>
              <el-submenu index="2-4">
                <template slot="title">操作</template>
                <el-menu-item index="/kafka/produce">生产</el-menu-item>
                <el-menu-item index="/kafka/consume">消费</el-menu-item>
              </el-submenu>
            </el-submenu>
            <el-submenu index="2">
              <template slot="title">
                <span class="menu"><i class="iconfont icon-Zookeeper icon"/>zookeeper</span>
              </template>
              <el-menu-item index="/zk/config">配置</el-menu-item>
              <el-menu-item index="/zk/manage">管理</el-menu-item>
            </el-submenu>

            <el-submenu index="3">
              <template slot="title">
                <span class="menu"><i class="iconfont icon-redis icon"/>redis</span>
              </template>
              <el-menu-item index="/redis/config">配置</el-menu-item>
              <el-menu-item index="/redis/manage">管理</el-menu-item>
            </el-submenu>
            <!--          <el-submenu index="4">-->
            <!--            <template slot="title">-->
            <!--              <span class="menu"><i class="iconfont icon-hbase icon"/>hbase</span>-->
            <!--            </template>-->
            <!--            <el-menu-item index="">开发中......</el-menu-item>-->
            <!--          </el-submenu>-->
            <el-submenu index="8">
              <template slot="title">
                <span class="">工具</span>
              </template>
              <el-menu-item index="/tool/json">JSON</el-menu-item>
              <el-menu-item index="/tool/time">时间戳</el-menu-item>
            </el-submenu>
            <el-submenu index="9">
              <template slot="title">
                <span class="">关于</span>
              </template>
              <el-menu-item index="/about/authority">权限设置</el-menu-item>
              <el-menu-item index="/about/donate">开源</el-menu-item>
            </el-submenu>
          </el-menu>
        </div>
      </el-header>
      <el-main>
<!--        <div class="bread">-->
<!--          <el-breadcrumb separator-class="el-icon-arrow-right">-->
<!--            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>-->
<!--            <el-breadcrumb-item>活动管理</el-breadcrumb-item>-->
<!--            <el-breadcrumb-item>活动列表</el-breadcrumb-item>-->
<!--            <el-breadcrumb-item>活动详情</el-breadcrumb-item>-->
<!--          </el-breadcrumb>-->
<!--        </div>-->
        <div class="content">

          <router-view></router-view>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
// @ is an alias to /src
import {initKafka, initRedis, initZK} from '@/js/auth.js'

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
    },
    init() {

      initKafka(this)
      initZK(this)
      initRedis(this)
    }
  },
  created() {
    this.init()

  }
}
</script>

<style scoped lang="scss">
.head {
  display: flex;
  background-color: #08B0AA;
  box-shadow: 1px 1px 5px #8c8b8b;

  .logo {

    width: 400px;
    line-height: 60px;
    color: #fff;
    font-size: 30px;
    font-weight: 700;
    padding-left: 20px;
  }

  .menu {
    width: 100%;
    font-size: 22px;
    font-weight: 900;
    border : 0 solid #fff;
  }
}

.icon {
  color: #ffffff;
  font-size: 23px !important;
  font-weight: normal;
}

.content {
  padding: 10px;
  max-width: 1700px;
  margin: 0 auto;
  box-shadow: 1px 1px 5px #8c8b8b;
  min-height: calc(100vh - 100px) ;
  border-radius: 10px;
  background-color: #fff;
}


</style>
