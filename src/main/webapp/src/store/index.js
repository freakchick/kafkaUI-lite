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
        }
    },
    actions: {},
    getters: {
        getKafkaAuth: (state) => (sourceId) => {
            console.log(state.kafkaAuth)
            console.log(state.kafkaAuth[sourceId] == undefined)
            // if (state.kafkaAuth.hasOwnProperty(sourceId)) {
            //     return state.kafkaAuth[sourceId];
            // } else {
            //     return true;
            // }
        }
    },
})
