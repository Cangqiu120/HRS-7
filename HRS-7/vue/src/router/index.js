import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/home',  // 重定向到主页
    children: [
      { path: 'appointment-registration/reservation',
        name: 'Reservation',
        meta: { name: '挂号' },
        component: () => import('../views/manager/Reservation') },
      { path: 'appointment-registration/hospital/:id', // 新增医院详情路由
        name: 'HospitalDetail',
        meta: { name: '医院详情' },
        component: () => import('../views/manager/HospitalDetail') },
      {
        path: '/doctor/:hospitalId/:departmentId', // 定义路由路径
        name: 'Doctor', // 路由名称
        component: () => import('../views/manager/Doctor.vue') // 对应的组件
      },
      {
        path: '/registration/:doctorId', // 定义路由路径,
        name: 'Registration', // 路由名称
        component: () => import('../views/manager/Registration.vue') // 对应的组件
      },
      {
        path: '/submit-registration',
        name: 'SubmitRegistration',
        component: () => import('../views/manager/SubmitRegistration.vue') // 提交挂号界面的组件
      },
      {
        path: '/registration-success',
        name: 'RegistrationSuccess',
        component: ()=> import('../views/manager/RegistrationSuccess.vue') // 挂号成功界面的组件
      },
      {
        path:'/registration-detail',
        name:'OrderDetail',
        component:()=>import('../views/manager/RegistrationDetail.vue')
      },
      { path: '403', name: 'NoAuth', meta: { name: '无权限' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: '管理员信息' }, component: () => import('../views/manager/Admin') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: '公告信息' }, component: () => import('../views/manager/Notice') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/front/Home') },
      { path: 'person', name: 'Person', meta: { name: '个人信息' }, component: () => import('../views/front/Person') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: '注册' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问' }, component: () => import('../views/404.vue') },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 注：不需要前台的项目，可以注释掉该路由守卫
// 路由守卫
// router.beforeEach((to ,from, next) => {
//   let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
//   if (to.path === '/') {
//     if (user.role) {
//       if (user.role === 'USER') {
//         next('/front/home')
//       } else {
//         next('/home')
//       }
//     } else {
//       next('/login')
//     }
//   } else {
//     next()
//   }
// })

export default router
