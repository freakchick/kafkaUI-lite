import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'

Vue.config.productionTip = false

import axios from 'axios'
import VueAxios from 'vue-axios'

//使用vue-axios，这样才可以全局使用this.axios调用
Vue.use(VueAxios, axios);
import qs from 'qs'

import VueClipboard from 'vue-clipboard2'
import store from './store'
Vue.use(VueClipboard)

// axios.defaults.baseURL = '/api'

axios.defaults.headers = {'Content-Type': 'application/x-www-form-urlencoded'}
//全局拦截post请求的参数，用qs序列化
axios.interceptors.request.use(config => {
  //form表单提交multipart/form-data的时候，不需要序列化参数
  if(config.method === 'post' && config.headers['Content-Type'] === 'application/x-www-form-urlencoded'){
    config.data = qs.stringify(config.data,{ indices: false });
  }
  return config
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
