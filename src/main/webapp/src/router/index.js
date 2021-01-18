import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import kafkaConfig from '../components/kafka/config'
import produce from '../components/kafka/operate/produce'
import consume from '../components/kafka/operate/consume'
import kafkaManage from '../components/kafka/manage'
import zkConfig from '../components/zk/config'
import zkManage from '../components/zk/manage'
import redisConfig from '../components/redis/config'
import redisManage from '../components/redis/manage'
import donate from '../components/about/donate'
import authority from '../components/about/authority'
import json from '../components/tool/json'
import time from '../components/tool/time'

Vue.use(VueRouter)

const routes = [
    {
        path: '/', name: 'Home', component: Home,
        children: [
            {path: '/kafka/config', name: 'kafkaConfig', component: kafkaConfig},
            {path: '/kafka/produce', name: 'produce', component: produce},
            {path: '/kafka/consume', name: 'consume', component: consume},
            {path: '/kafka/manage', name: 'kafkaManage', component: kafkaManage},
            {path: '/zk/config', name: 'zkConfig', component: zkConfig},
            {path: '/zk/manage', name: 'zkManage', component: zkManage},
            {path: '/redis/config', name: 'redisConfig', component: redisConfig},
            {path: '/redis/manage', name: 'redisManage', component: redisManage},
            {path: '/about/donate', name: 'donate', component: donate},
            {path: '/about/authority', name: 'authority', component: authority},
            {path: '/tool/json', name: 'json', component: json},
            {path: '/tool/time', name: 'time', component: time}
        ]
    }/*,
    {
        path: '/about',
        name: 'About',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/!* webpackChunkName: "about" *!/ '../views/About.vue')
    }*/
]

const router = new VueRouter({
    routes
})

export default router
