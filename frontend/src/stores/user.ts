import { defineStore } from 'pinia';
import { ref } from 'vue';

export interface UserInfo {
  id?: number;
  username?: string;
  nickname?: string;
  avatar?: string;
  role?: string;
  email?: string;
  phone?: string;
}

export const useUserStore = defineStore(
  'user',
  () => {
    const token = ref<string>(localStorage.getItem('token') || '');
    const userInfo = ref<UserInfo | null>(null);

    // 设置 Token
    const setToken = (newToken: string) => {
      token.value = newToken;
      localStorage.setItem('token', newToken);
    };

    // 设置用户信息
    const setUserInfo = (info: UserInfo) => {
      userInfo.value = info;
    };

    // 退出登录
    const logout = () => {
      token.value = '';
      userInfo.value = null;
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
    };

    // 从本地存储恢复
    const restore = () => {
      const savedInfo = localStorage.getItem('userInfo');
      if (savedInfo) {
        try {
          userInfo.value = JSON.parse(savedInfo);
        } catch (e) {
          console.error('Failed to parse userInfo from localStorage');
        }
      }
    };

    return {
      token,
      userInfo,
      setToken,
      setUserInfo,
      logout,
      restore,
    };
  },
  {
    persist: {
      key: 'user-store',
      storage: localStorage,
    },
  }
);
