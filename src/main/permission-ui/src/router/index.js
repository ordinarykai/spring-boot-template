import Vue from 'vue'
import Router from 'vue-router'
// import Permission from '../components/Permission'
import PermissionCopy from '../components/PermissionCopy'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    // {
    //   path: '/',
    //   name: 'Permission',
    //   component: Permission
    // },
    {
      path: '/',
      name: 'PermissionCopy',
      component: PermissionCopy
    }
  ]
})
