import Vue from 'vue'
import App from './App'
import store from './store'
import "@/styles/common.scss"
// 1
Vue.config.productionTip = false
Vue.prototype.$store = store

App.mpType = 'app'
import uView from '@/node_modules/uview-ui'
Vue.use(uView)

const app = new Vue({
	store,
    ...App
})
app.$mount()
