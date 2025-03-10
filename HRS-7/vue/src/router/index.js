import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err);
};

const routes = [
  {
    path: '/',
    name: 'User',
    component: () => import('../views/UserFront.vue'),
    redirect: '/home', // 重定向到主页
    children: [
      { path: 'Same-day-registration/reservation', name: 'Reservation', meta: { name: '当日挂号' }, component: () => import('@/views/user/Reservation') },
      { path: 'appointment-registration/hospital/:id', name: 'HospitalDetail', meta: { name: '医院详情' }, component: () => import('@/views/user/HospitalDetail') },
      { path: '/doctor/:hospitalId/:departmentId', name: 'Doctor', component: () => import('@/views/user/Doctor.vue') },
      { path: '/registration/:doctorId', name: 'Registration', component: () => import('@/views/user/Registration.vue') },
      { path: '/submit-registration', name: 'SubmitRegistration', component: () => import('@/views/user/SubmitRegistration.vue') },
      { path: '/registration-success', name: 'RegistrationSuccess', component: () => import('@/views/user/RegistrationSuccess.vue') },
      { path: '/registration-detail', name: 'OrderDetail', component: () => import('@/views/user/RegistrationDetail.vue') },
      { path: '403', name: 'NoAuth', meta: { name: '无权限' }, component: () => import('@/views/user/403') },
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('@/views/user/Home') },
      { path: 'admin', name: 'Admin', meta: { name: '管理员信息' }, component: () => import('@/views/user/Admin') },
      { path: 'person', name: 'Person', meta: { name: '个人信息' }, component: () => import('@/views/user/Person.vue') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('@/views/user/Password') },
      {path: 'selectRegistration',name:'SelectRegistration',meta:{name:'查看挂号信息'},component:()=>import('@/views/user/SelectRegistration.vue')},
      {path: 'selectRegistrationDetail',name: 'SelectRegistrationDetail',meta:{name:'查看挂号详情'},component:()=>import('@/views/user/SelectRegistrationDetail.vue')},
      { path: 'notice', name: 'Notice', meta: { name: '公告信息' }, component: () => import('@/views/user/Notice') },
    ],
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/front/Home') },
      { path: 'person', name: 'Person', meta: { name: '个人信息' }, component: () => import('../views/front/Person') },
    ],
  },
  {
    path: '/doctor',
    name: 'Doctor',
    component: () => import('../views/DoctorFront.vue'), // 医生工作台的主组件
    redirect: '/doctor/home', // 默认重定向到今日预约
    children: [
      {
        path: 'home',
        name: 'DoctorHome',
        meta: { name: '医生工作台' },
        component: () => import('@/views/doctor/Home.vue'), // 医生工作台的主组件
      },
      {
        path: 'today-appointments',
        name: 'TodayAppointments',
        meta: { name: '今日预约' },
        component: () => import('@/views/doctor/TodayAppointments.vue'),
      },
      {
        path: 'prescribing-administration',
        name: 'PrescribingAdministration',
        meta: { name: '处方管理' },
        component: () => import('@/views/doctor/PrescribingAdministration.vue'),
      },
      {
        path: 'prescription',
        name: 'Prescription',
        meta: { name: '开处方' },
        component: () => import('@/views/doctor/Prescription.vue'),
      },
      {
        path: 'prescription-detail',
        name: 'PrescribeDetail',
        meta: { name: '处方详情' },
        component: () => import('@/views/doctor/PrescribeDetail.vue'),
      },
      {
        path: 'registration-detail',
        name: 'RegistrationDetail',
        meta: { name: '挂号详情' },
        component: () => import('@/views/doctor/RegistrationDetail.vue'),
      },
      {
        path: 'consultation',
        name: 'Consultation',
        meta: { name: '会诊管理' },
        component: () => import('@/views/doctor/Consultation.vue'),
      },
      {
        path: 'patient-management',
        name: 'PatientManagement',
        meta: { name: '患者管理' },
        component: () => import('@/views/doctor/PatientManagement.vue'),
      },
      {
        path: 'schedule-management',
        name: 'ScheduleManagement',
        meta: { name: '排班管理' },
        component: () => import('@/views/doctor/ScheduleManagement.vue'),
      },
      {
        path: 'medical-records',
        name: 'MedicalRecords',
        meta: { name: '病历管理' },
        component: () => import('@/views/doctor/MedicalRecords.vue'),
      },
      {
        path: 'message-center',
        name: 'MessageCenter',
        meta: { name: '消息中心' },
        component: () => import('@/views/doctor/MessageCenter.vue'),
      },
      {
        path: 'profile',
        name: 'DoctorProfile',
        meta: { name: '个人信息' },
        component: () => import('@/views/doctor/DoctorProfile.vue'),
      },
    ],
  },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: '注册' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问' }, component: () => import('../views/404.vue') },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

export default router;