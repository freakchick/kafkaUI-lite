import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Config from '../components/kafka/config'
import operate from '../components/kafka/operate'
import manage from '../components/kafka/manage'
import zkConfig from '../components/zk/config'
import redisConfig from '../components/redis/config'


Vue.use(VueRouter)

const routes = [
    {
        path: '/', name: 'Home', component: Home,
        children: [
            {path: '/config', name: 'Config', component: Config},
            {path: '/operate', name: 'operate', component: operate},
            {path: '/manage', name: 'manage', component: manage},
            {path: '/zk/config', name: 'zkConfig', component: zkConfig},
            {path: '/redis/config', name: 'redisConfig', component: redisConfig}
        ]
    },
    {
        path: '/about',
        name: 'About',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
    }
]

const router = new VueRouter({
    routes
})

export default router
