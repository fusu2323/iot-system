<template>
  <div class="app-container">
    <aside class="sidebar">
      <!-- Logo -->
      <div class="logo" @dblclick="handleLogoDblClick">
        <div class="logo-icon">
          <Icon name="home" :size="22" />
        </div>
        <span class="logo-text">智能家</span>
      </div>

      <!-- Navigation -->
      <ul class="nav-menu">
        <li
          v-for="item in menuItems"
          :key="item.path"
          class="nav-item"
          :class="{ active: isActive(item.path) }"
          @click="navigateTo(item.path)"
        >
          <span class="nav-icon">
            <Icon :name="item.icon" :size="20" />
          </span>
          <span>{{ item.name }}</span>
        </li>
      </ul>

      <!-- User Info -->
      <div class="nav-user">
        <div class="nav-user-avatar">{{ userAvatar }}</div>
        <div class="nav-user-info">
          <div class="nav-user-name">{{ userName }}</div>
          <div class="nav-user-role">家庭管理员</div>
        </div>
      </div>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
      <header class="header">
        <div class="header-left">
          <h1>{{ currentPageTitle }}</h1>
          <p v-if="currentPageDesc">{{ currentPageDesc }}</p>
        </div>
        <div class="header-right">
          <div class="search-bar">
            <span class="search-icon">
              <Icon name="search" :size="18" />
            </span>
            <input type="text" placeholder="搜索设备、场景或内容..." />
          </div>
          <div class="header-icon">
            <Icon name="notification" :size="20" />
          </div>
          <div class="header-icon" @click="logout">
            <Icon name="user" :size="20" />
          </div>
        </div>
      </header>

      <!-- Page Content -->
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>

      <!-- 动画和彩蛋组件 -->
      <Animations />
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useUserStore } from '@/stores/user';
import Animations from '@/components/Animations.vue';
import Icon from '@/components/Icon.vue';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

// 恢复用户信息
onMounted(() => {
  userStore.restore();
});

const menuItems = [
  { path: '/dashboard', name: '温馨家', icon: 'home' },
  { path: '/devices', name: '设备管理', icon: 'devices' },
  { path: '/scenes', name: '场景管理', icon: 'scenes' },
  { path: '/content', name: '内容管理', icon: 'content' },
  { path: '/recommend', name: '推荐管理', icon: 'recommend' },
  { path: '/users', name: '家庭成员', icon: 'users' },
  { path: '/logs', name: '生活轨迹', icon: 'logs' },
  { path: '/usage', name: '使用统计', icon: 'stats' },
];

const isActive = (path: string) => route.path === path;

const navigateTo = (path: string) => {
  router.push(path);
};

const currentPageTitle = computed(() => {
  return (route.meta.title as string) || '温馨家';
});

const currentPageDesc = computed(() => {
  const descs: Record<string, string> = {
    dashboard: '今天天气不错，享受美好的居家时光吧。',
    devices: '管理所有智能家居设备。',
    scenes: '创建和管理生活场景。',
    content: '管理娱乐内容资源。',
    recommend: '个性化内容推荐。',
    users: '管理家庭成员信息。',
    logs: '查看操作日志和生活轨迹。',
    usage: '查看设备和场景的使用统计。',
  };
  // 添加随机彩蛋描述
  const randomDescs: Record<string, string[]> = {
    dashboard: ['今天天气不错，享受美好的居家时光吧。', '家是心的港湾～', '智能家居，让生活更美好'],
    devices: ['管理所有智能家居设备。', '设备们都在等你召唤哦～', '科技感满满的一天！'],
    scenes: ['创建和管理生活场景。', '一键切换生活方式！', '场景是你的魔法咒语'],
    content: ['管理娱乐内容资源。', '精彩内容等你发现！', '生活不只有诗和远方，还有好剧和音乐～'],
    recommend: ['个性化内容推荐。', '懂你，所以推荐给你～', '说不定会发现新大陆哦！'],
    users: ['管理家庭成员信息。', '家人是我们最珍贵的财富', '和谐家庭，美好生活～'],
    logs: ['查看操作日志和生活轨迹。', '每一步都值得记录～', '时光机已启动'],
  };
  const routeDescs = randomDescs[route.name as string];
  if (routeDescs) {
    // 每次随机显示不同的描述
    const hour = new Date().getHours();
    const index = hour % routeDescs.length;
    return routeDescs[index];
  }
  return descs[route.name as string] || '';
});

const userName = computed(() => {
  return userStore.userInfo?.nickname || userStore.userInfo?.username || 'Admin';
});

const userAvatar = computed(() => {
  const name = userName.value;
  return name.charAt(0).toUpperCase();
});

const logout = () => {
  userStore.logout();
  router.push('/login');
};

// Logo 双击彩蛋
const handleLogoDblClick = () => {
  const messages = [
    '欢迎回家！',
    '智能家居，让生活更美好',
    '双击是回家的仪式感～',
    '今天也是元气满满的一天！',
  ];
  const message = messages[Math.floor(Math.random() * messages.length)];
  // 触发全局彩蛋事件
  window.dispatchEvent(new CustomEvent('showEasterEgg', {
    detail: { icon: 'scene-home', text: message }
  }));
};
</script>

<style scoped>
.app-container {
  display: flex;
  min-height: 100vh;
  background: var(--bg-primary);
}

/* Sidebar */
.sidebar {
  width: 240px;
  background: var(--bg-secondary);
  border-right: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  position: fixed;
  height: 100vh;
  overflow-y: auto;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px 16px;
  border-bottom: 1px solid var(--border-color);
}

.logo-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(145deg, #5A5D43 0%, #4A4D33 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  box-shadow: 0 4px 12px rgba(90, 93, 67, 0.3);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.logo:hover .logo-icon {
  transform: scale(1.05);
  box-shadow: 0 6px 16px rgba(90, 93, 67, 0.4);
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  font-family: 'Noto Serif SC', serif;
}

.nav-menu {
  list-style: none;
  padding: 16px 12px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  color: var(--text-secondary);
  font-size: 14px;
  margin-bottom: 4px;
  position: relative;
  overflow: hidden;
}

.nav-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  width: 3px;
  height: 100%;
  background: var(--primary-color);
  transform: scaleY(0);
  transition: transform 0.25s ease;
}

.nav-item:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.nav-item:hover::before {
  transform: scaleY(0.5);
}

.nav-item.active {
  background: var(--primary-color);
  color: white;
}

.nav-item.active::before {
  transform: scaleY(1);
}

.nav-icon {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  transition: transform 0.2s ease;
}

.nav-item:hover .nav-icon {
  transform: scale(1.1);
}

.nav-user {
  margin-top: auto;
  padding: 16px;
  border-top: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  gap: 12px;
}

.nav-user-avatar {
  width: 40px;
  height: 40px;
  background: var(--primary-light);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
}

.nav-user-info {
  flex: 1;
}

.nav-user-name {
  font-weight: 500;
  font-size: 14px;
}

.nav-user-role {
  font-size: 12px;
  color: var(--text-muted);
}

/* Main Content */
.main-content {
  flex: 1;
  margin-left: 240px;
  padding: 24px 32px;
  min-height: 100vh;
}

/* Header */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
}

.header-left h1 {
  font-size: 24px;
  color: var(--text-primary);
  margin-bottom: 6px;
}

.header-left p {
  font-size: 14px;
  color: var(--text-muted);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 20px;
  width: 280px;
  transition: all 0.2s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.02);
}

.search-bar:focus-within {
  border-color: var(--primary-color);
  box-shadow: 0 2px 8px rgba(90, 93, 67, 0.15);
}

.search-bar input {
  flex: 1;
  background: none;
  border: none;
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
}

.search-bar input::placeholder {
  color: var(--text-muted);
}

.search-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-muted);
  transition: color 0.2s ease;
}

.search-bar:focus-within .search-icon {
  color: var(--primary-color);
}

.header-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
  color: var(--text-secondary);
}

.header-icon:hover {
  background: var(--bg-hover);
  color: var(--primary-color);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

/* Page Transition */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
