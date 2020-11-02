<template>
  <div id="app">
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
  /*display: flex;*/
  /*justify-content: center;*/
  width: 700px;
  margin: 0 auto;

}
</style>
