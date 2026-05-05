import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/stores/user';
import Layout from '@/layouts/Layout.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
      meta: { requiresAuth: false },
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/RegisterView.vue'),
      meta: { requiresAuth: false },
    },
    {
      path: '/',
      name: 'layout',
      component: Layout,
      redirect: '/dashboard',
      meta: { requiresAuth: true },
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('@/views/DashboardView.vue'),
          meta: { title: '温馨家' },
        },
        {
          path: 'devices',
          name: 'devices',
          component: () => import('@/views/DevicesView.vue'),
          meta: { title: '设备管理' },
        },
        {
          path: 'scenes',
          name: 'scenes',
          component: () => import('@/views/ScenesView.vue'),
          meta: { title: '场景管理' },
        },
        {
          path: 'content',
          name: 'content',
          component: () => import('@/views/ContentView.vue'),
          meta: { title: '内容管理' },
        },
        {
          path: 'recommend',
          name: 'recommend',
          component: () => import('@/views/RecommendView.vue'),
          meta: { title: '推荐管理' },
        },
        {
          path: 'users',
          name: 'users',
          component: () => import('@/views/UsersView.vue'),
          meta: { title: '家庭成员' },
        },
        {
          path: 'logs',
          name: 'logs',
          component: () => import('@/views/LogsView.vue'),
          meta: { title: '生活轨迹' },
        },
      ],
    },
  ],
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();

  // 检查是否需要登录
  if (to.meta.requiresAuth !== false) {
    if (!userStore.token) {
      next('/login');
      return;
    }
  }

  // 已登录用户访问登录页，重定向到首页
  if ((to.path === '/login' || to.path === '/register') && userStore.token) {
    next('/dashboard');
    return;
  }

  next();
});

export default router;
