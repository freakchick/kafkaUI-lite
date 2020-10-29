import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'

Vue.config.productionTip = false

import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.use(VueAxios, axios);
import qs from 'qs'
axios.defaults.baseURL = '/api'

axios.defaults.headers = {'Content-Type': 'application/x-www-form-urlencoded'}
axios.interceptors.request.use(config => {
  //form表单提交multipart/form-data的时候，不需要序列化参数
  if(config.method === 'post' && config.headers['Content-Type'] === 'application/x-www-form-urlencoded'){
    config.data = qs.stringify(config.data,{ indices: false });
  }
  return config
})

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
