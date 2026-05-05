<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-logo">
        <div class="logo-icon">
          <Icon name="home" :size="28" />
        </div>
        <h1 class="logo-text">智能家</h1>
      </div>

      <h2 class="login-title">登录</h2>
      <p class="login-desc">欢迎回到智能家居娱乐管理系统</p>

      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label class="form-label">用户名</label>
          <input
            v-model="formData.username"
            type="text"
            class="form-input"
            placeholder="请输入用户名"
            required
          />
        </div>

        <div class="form-group">
          <label class="form-label">密码</label>
          <input
            v-model="formData.password"
            type="password"
            class="form-input"
            placeholder="请输入密码"
            required
          />
        </div>

        <button type="submit" class="btn-login" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>

      <div class="login-footer">
        <span>还没有账号？</span>
        <router-link to="/register" class="login-link">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import Icon from '@/components/Icon.vue';
import { login } from '@/api/auth';

const router = useRouter();
const userStore = useUserStore();

const formData = ref({
  username: '',
  password: '',
});

const loading = ref(false);

const handleLogin = async () => {
  if (loading.value) return;

  loading.value = true;

  try {
    // 调用登录 API
    const data = await login(formData.value);

    // 保存 token 和用户信息
    userStore.setToken(data.token);
    userStore.setUserInfo({
      id: data.userId,
      username: data.username,
      nickname: data.nickname,
      avatar: data.avatar,
      role: data.role,
      email: '',
      phone: '',
    });

    // 跳转到首页
    router.push('/dashboard');
  } catch (error: any) {
    console.error('登录失败:', error);
    alert(error.response?.data?.message || '登录失败，请检查用户名和密码');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-primary);
}

.login-card {
  width: 420px;
  padding: 48px;
  background: var(--bg-card);
  border-radius: 24px;
  box-shadow: var(--shadow-lg);
}

.login-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 32px;
}

.logo-icon {
  width: 48px;
  height: 48px;
  background: var(--primary-color);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 22px;
}

.logo-text {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
  font-family: 'Noto Serif SC', serif;
}

.login-title {
  font-size: 24px;
  color: var(--text-primary);
  margin-bottom: 8px;
  text-align: center;
}

.login-desc {
  font-size: 14px;
  color: var(--text-muted);
  text-align: center;
  margin-bottom: 32px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-label {
  font-size: 14px;
  color: var(--text-secondary);
  font-weight: 500;
}

.form-input {
  padding: 12px 16px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s;
}

.form-input:focus {
  border-color: var(--primary-color);
}

.btn-login {
  padding: 14px;
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  margin-top: 8px;
}

.btn-login:hover {
  background: var(--primary-dark);
}

.btn-login:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.login-footer {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 24px;
  font-size: 14px;
  color: var(--text-secondary);
}

.login-link {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 500;
}

.login-link:hover {
  text-decoration: underline;
}
</style>
