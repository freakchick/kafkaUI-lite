<template>
  <div class="home">
    <h1>home</h1>
    <p>{{msg}}</p>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'

export default {
  data () {
    return {
      msg: "我是home 组件"
    }
  },
  name: 'Home',
  components: {
    HelloWorld
  }
}
</script>
