import Vue from 'vue'
import Router from 'vue-router'
import error from './views/404.vue'
import Cookies from 'js-cookie'

Vue.use(Router)

// 登录后才可访问的路由
let routesRequireLogin = [{
  path: '/',
  component: () => import('./views/Index.vue'),
  children: [{
    path: '/',
    component: () => import('./views/PostManager.vue')
  }, {
    path: '/archives',
    component: () => import('./views/CategoryManager.vue')
  }, {
    path: '/tags',
    component: () => import('./views/TagManager.vue')
  }, {
    path: '/param',
    component: () => import('./views/ParamManager.vue')
  }, {
    path: '/friend',
    component: () => import('./views/FriendManager.vue')
  }, {
    path: '/upload',
    component: () => import('./views/Upload.vue'),
  }]
}, {
  path: '/publish',
  component: () => import('./views/EditPost.vue'),
}, {
  path: '/edit',
  component: () => import('./views/EditPost.vue'),
}]

// 进入路由前判断登录是否过期，如果过期跳转到404
routesRequireLogin.forEach(i => {
  i.beforeEnter = (to, from, next) => {
    let loginTime = Cookies.get(window.LOGIN_TIME);
    if (loginTime)
      next();
    else
      next('login');
  }
})

const loginRoute = {
  path: '/login',
  component: () => import('./views/Login.vue')
}

const route404 = {
  path: '*',
  component: error,
  name: 'error'
}

const router = new Router();
router.addRoutes([loginRoute, ...routesRequireLogin, route404]);

export default router