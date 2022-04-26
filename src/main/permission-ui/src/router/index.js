import Vue from 'vue'
import Router from 'vue-router'
import Permission from '../components/Permission'

Vue.use(Router)

export default new Router({
  // mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Permission',
      component: Permission
    }
  ]
})
