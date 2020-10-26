<template>
  <div id="app">
    <Home></Home>
    <header>
      <router-link to="/">Home</router-link>
      <router-link to="/about">About</router-link>
    </header>
    <!-- 对应的组件内容渲染到router-view中 -->
    <router-view></router-view>

  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import Home from '@/views/Home.vue'

export default {
  name: 'app',
  components: {
    HelloWorld,Home
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;

}
</style>
