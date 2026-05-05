import axios from 'axios';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';

// 创建 axios 实例
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 10000,
});

// 请求拦截器 - 注入 Token
request.interceptors.request.use(
  (config) => {
    const userStore = useUserStore();
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器 - 处理错误
request.interceptors.response.use(
  (response) => {
    const { code, data, message } = response.data;

    // 假设后端返回格式：{ code: 200, data: {}, message: 'success' }
    if (code === 200) {
      return data;
    }

    // 业务错误处理
    console.error(message || '请求失败');
    return Promise.reject(new Error(message || '请求失败'));
  },
  (error) => {
    // HTTP 错误处理
    if (error.response) {
      const { status, data } = error.response;

      switch (status) {
        case 401:
          // Token 过期，跳转登录
          const router = useRouter();
          const userStore = useUserStore();
          userStore.logout();
          router.push('/login');
          break;
        case 403:
          console.error('无权限访问');
          break;
        case 404:
          console.error('资源不存在');
          break;
        case 500:
          console.error('服务器错误');
          break;
        default:
          console.error(data?.message || '请求失败');
      }
    }
    return Promise.reject(error);
  }
);

export default request;
