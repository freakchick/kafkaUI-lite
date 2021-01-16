import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        kafkaAuth: {},
        zkAuth: {}
    },
    mutations: {
        setKafkaAuth(state, payload) {
            state.kafkaAuth = payload
        },
        setZKAuth(state, payload) {
            state.zkAuth = payload
        }
    },
    actions: {},
    getters: {
        getKafkaAuth: (state) => (sourceId) => {
            let a = state.kafkaAuth[sourceId]
            console.log("getters...",a)
            return a
        },
        getZKAuth: (state) => (sourceId) => {
            let a = state.zkAuth[sourceId]
            return a
        }
    },
})
