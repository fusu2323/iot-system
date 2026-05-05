<template>
  <div class="register-container">
    <div class="register-card">
      <div class="register-logo">
        <div class="logo-icon">
          <Icon name="home" :size="28" />
        </div>
        <h1 class="logo-text">智能家</h1>
      </div>

      <h2 class="register-title">注册</h2>
      <p class="register-desc">创建您的智能家居娱乐管理系统账号</p>

      <form class="register-form" @submit.prevent="handleRegister">
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
          <label class="form-label">昵称</label>
          <input
            v-model="formData.nickname"
            type="text"
            class="form-input"
            placeholder="请输入昵称"
          />
        </div>

        <div class="form-group">
          <label class="form-label">邮箱</label>
          <input
            v-model="formData.email"
            type="email"
            class="form-input"
            placeholder="请输入邮箱"
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

        <div class="form-group">
          <label class="form-label">确认密码</label>
          <input
            v-model="formData.confirmPassword"
            type="password"
            class="form-input"
            placeholder="请再次输入密码"
            required
          />
        </div>

        <button type="submit" class="btn-register" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>

      <div class="register-footer">
        <span>已有账号？</span>
        <router-link to="/login" class="register-link">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Icon from '@/components/Icon.vue';
import { register } from '@/api/auth';

const router = useRouter();

const formData = ref({
  username: '',
  nickname: '',
  email: '',
  password: '',
  confirmPassword: '',
});

const loading = ref(false);

const handleRegister = async () => {
  if (loading.value) return;

  // 验证密码
  if (formData.value.password !== formData.value.confirmPassword) {
    alert('两次输入的密码不一致');
    return;
  }

  if (formData.value.password.length < 6) {
    alert('密码长度不能少于 6 位');
    return;
  }

  loading.value = true;

  try {
    // 调用注册接口
    await register({
      username: formData.value.username,
      password: formData.value.password,
      nickname: formData.value.nickname || formData.value.username,
    });

    alert('注册成功，请登录');
    router.push('/login');
  } catch (error: any) {
    console.error('注册失败:', error);
    alert(error.response?.data?.message || '注册失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-primary);
}

.register-card {
  width: 420px;
  padding: 48px;
  background: var(--bg-card);
  border-radius: 24px;
  box-shadow: var(--shadow-lg);
}

.register-logo {
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

.register-title {
  font-size: 24px;
  color: var(--text-primary);
  margin-bottom: 8px;
  text-align: center;
}

.register-desc {
  font-size: 14px;
  color: var(--text-muted);
  text-align: center;
  margin-bottom: 32px;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
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

.btn-register {
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

.btn-register:hover {
  background: var(--primary-dark);
}

.btn-register:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.register-footer {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 24px;
  font-size: 14px;
  color: var(--text-secondary);
}

.register-link {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 500;
}

.register-link:hover {
  text-decoration: underline;
}
</style>
