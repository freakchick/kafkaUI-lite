<template>
    <div class="home">
        <el-container>
            <el-header>
                <router-link to="/config" class="menu-item">配置</router-link>
                <router-link to="/manage" class="menu-item">管理</router-link>
                <router-link to="/operate" class="menu-item">操作</router-link>
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
        name: 'Home'
    }
</script>

<style scoped>
    .menu-item {
        color: #13ce66;
        font-size: 28px;
        margin: 0 20px;
        text-decoration: none;



    }
    a:hover {
      color: red
    }
</style>
