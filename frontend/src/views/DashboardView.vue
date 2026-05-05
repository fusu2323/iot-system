<template>
  <div class="dashboard">
    <!-- Stats Cards -->
    <div class="stats-grid">
      <div
        class="stat-card"
        v-for="(stat, index) in stats"
        :key="stat.label"
        :style="{ animationDelay: index * 0.1 + 's' }"
        @click="handleStatClick"
      >
        <div class="stat-icon" :class="stat.iconClass">
          <Icon :name="stat.icon" :size="22" />
        </div>
        <div class="stat-value">{{ stat.value }}</div>
        <div class="stat-label">{{ stat.label }}</div>
      </div>
    </div>

    <!-- Content Layout -->
    <div class="content-layout">
      <!-- Scenes -->
      <div class="card">
        <div class="card-header">
          <h3 class="card-title">常用场景</h3>
          <span class="card-link" @click="navigateTo('scenes')">查看全部 <Icon name="chevron-right" :size="16" /></span>
        </div>
        <div class="scene-list">
          <div class="scene-item" v-for="scene in scenes" :key="scene.id">
            <div class="scene-header">
              <div class="scene-icon" :class="scene.iconClass">
                <Icon :name="scene.icon" :size="18" />
              </div>
              <div>
                <div class="scene-name">{{ scene.name }}</div>
                <div class="scene-desc">{{ scene.description }}</div>
              </div>
            </div>
            <div
              class="toggle-switch"
              :class="{ active: scene.active }"
              @click="toggleSceneAction(scene)"
            ></div>
          </div>
        </div>
      </div>

      <!-- Activity -->
      <div class="card">
        <div class="card-header">
          <h3 class="card-title">家庭动态</h3>
          <span class="card-link" @click="navigateTo('logs')">查看全部 <Icon name="chevron-right" :size="16" /></span>
        </div>
        <div class="activity-list">
          <div class="activity-item" v-for="item in activities" :key="item.id">
            <div class="activity-icon">
              <Icon :name="item.icon" :size="20" />
            </div>
            <div class="activity-info">
              <div class="activity-text">{{ item.text }}</div>
              <div class="activity-time">{{ item.time }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Recommend Preview -->
    <div class="card" style="margin-top: 24px">
      <div class="card-header">
        <h3 class="card-title">为您推荐</h3>
        <span class="card-link" @click="navigateTo('recommend')">查看全部 <Icon name="chevron-right" :size="16" /></span>
      </div>
      <div class="recommend-grid">
        <div class="recommend-card" v-for="item in recommendations" :key="item.id">
          <div class="recommend-cover">
            <Icon :name="item.cover" :size="48" />
          </div>
          <div class="recommend-info">
            <div class="recommend-title">{{ item.title }}</div>
            <div class="recommend-desc">{{ item.description }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import Icon from '@/components/Icon.vue';
import { getOverviewStats } from '@/api/dashboard';
import { getSceneList, toggleScene, triggerScene } from '@/api/scene';
import { getRecommendations } from '@/api/recommend';
import { getLogList } from '@/api/log';
import { useUserStore } from '@/stores/user';

const router = useRouter();
const userStore = useUserStore();

const navigateTo = (path: string) => {
  router.push(path);
};

// 统计数据
const stats = ref([
  { icon: 'stat-devices', iconClass: 'green', value: '0', label: '家庭设备' },
  { icon: 'stat-online', iconClass: 'blue', value: '0', label: '在线设备' },
  { icon: 'stat-scenes', iconClass: 'pink', value: '0', label: '生活场景' },
  { icon: 'stat-content', iconClass: 'purple', value: '0', label: '娱乐内容' },
  { icon: 'stat-users', iconClass: 'orange', value: '0', label: '家庭成员' },
]);

// 场景列表
const scenes = ref<any[]>([]);

// 家庭动态
const activities = ref<any[]>([]);

// 推荐列表
const recommendations = ref<any[]>([]);

// 加载数据
const loadData = async () => {
  try {
    // 加载统计数据
    const overview = await getOverviewStats();
    stats.value = [
      { icon: 'stat-devices', iconClass: 'green', value: overview.totalDevices.toString(), label: '家庭设备' },
      { icon: 'stat-online', iconClass: 'blue', value: overview.onlineDevices.toString(), label: '在线设备' },
      { icon: 'stat-scenes', iconClass: 'pink', value: overview.totalScenes.toString(), label: '生活场景' },
      { icon: 'stat-content', iconClass: 'purple', value: overview.totalContents.toString(), label: '娱乐内容' },
      { icon: 'stat-users', iconClass: 'orange', value: overview.totalUsers.toString(), label: '家庭成员' },
    ];

    // 加载场景列表
    const sceneResult = await getSceneList({ page: 1, size: 10 });
    scenes.value = sceneResult.records.map((scene) => ({
      id: scene.id,
      name: scene.name,
      description: scene.description,
      icon: scene.icon || 'scene-home',
      iconClass: getSceneIconClass(scene.icon),
      active: scene.isEnabled === 1,
    }));

    // 加载推荐列表
    const currentUserId = userStore.userInfo?.id || 1;
    const recommendResult = await getRecommendations({ userId: currentUserId, page: 1, size: 4 });
    recommendations.value = recommendResult.records.map((item) => ({
      id: item.id,
      cover: getContentCover(item.contentType),
      title: item.contentTitle,
      description: item.reason || '根据您的偏好推荐',
      contentId: item.contentId,
    }));

    // 加载家庭动态（最近日志）
    const logResult = await getLogList({ page: 1, size: 4 });
    activities.value = logResult.records.map((log) => ({
      id: log.id,
      icon: getOperationIcon(log.operation),
      text: `${log.username} ${getOperationText(log.operation)}`,
      time: formatTime(log.createTime),
    }));
  } catch (error) {
    console.error('加载数据失败:', error);
  }
};

// 获取场景图标类名
const getSceneIconClass = (icon: string) => {
  const map: Record<string, string> = {
    cinema: 'cinema',
    movie: 'cinema',
    home: 'home',
    sleep: 'sleep',
    leave: 'leave',
  };
  return map[icon] || 'home';
};

// 获取内容封面图标
const getContentCover = (type: string) => {
  const map: Record<string, string> = {
    MOVIE: 'content-movie',
    MUSIC: 'content-music',
    GAME: 'content-game',
  };
  return map[type] || 'content-movie';
};

// 获取操作图标
const getOperationIcon = (operation: string) => {
  const map: Record<string, string> = {
    LOGIN: 'user',
    LOGOUT: 'user',
    CREATE: 'add',
    UPDATE: 'edit',
    DELETE: 'delete',
  };
  return map[operation] || 'logs';
};

// 获取操作文本
const getOperationText = (operation: string) => {
  const map: Record<string, string> = {
    LOGIN: '登录了系统',
    LOGOUT: '退出了系统',
    CREATE: '创建了资源',
    UPDATE: '更新了资源',
    DELETE: '删除了资源',
  };
  return map[operation] || '进行了操作';
};

// 格式化时间
const formatTime = (timeStr?: string) => {
  if (!timeStr) return '';
  const time = new Date(timeStr).getTime();
  const now = Date.now();
  const diff = now - time;
  const minutes = Math.floor(diff / 60000);
  const hours = Math.floor(diff / 3600000);
  const days = Math.floor(diff / 86400000);

  if (minutes < 60) return `${minutes}分钟前`;
  if (hours < 24) return `${hours}小时前`;
  return `${days}天前`;
};

const handleStatClick = (e: MouseEvent) => {
  // 触发烟花效果
  const rect = (e.target as HTMLElement).getBoundingClientRect();
  window.dispatchEvent(new CustomEvent('triggerFirework', {
    detail: { x: rect.left + rect.width / 2, y: rect.top + rect.height / 2 }
  }));
};

const toggleSceneAction = async (scene: any) => {
  try {
    await toggleScene(scene.id);
    scene.active = !scene.active;
  } catch (error) {
    console.error('切换场景失败:', error);
  }
};

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* Stats Grid */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
}

.stat-card {
  background: var(--bg-card);
  border-radius: 16px;
  padding: 24px 20px;
  text-align: center;
  box-shadow: var(--shadow-sm);
  transition: all 0.3s ease;
  cursor: pointer;
  animation: slideInUp 0.6s ease both;
  opacity: 0;
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.stat-card:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: var(--shadow-md);
}

.stat-card:active {
  transform: translateY(-4px) scale(0.98);
}

.stat-icon {
  width: 48px;
  height: 48px;
  margin: 0 auto 12px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: currentColor;
  transition: transform 0.3s ease;
}

.stat-card:hover .stat-icon {
  transform: scale(1.1);
}

.stat-icon.green {
  background: linear-gradient(145deg, #10b981 0%, #059669 100%);
  color: #ecfdf5;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.25);
}
.stat-icon.blue {
  background: linear-gradient(145deg, #3b82f6 0%, #2563eb 100%);
  color: #eff6ff;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.25);
}
.stat-icon.pink {
  background: linear-gradient(145deg, #ec4899 0%, #db2777 100%);
  color: #fdf2f8;
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.25);
}
.stat-icon.purple {
  background: linear-gradient(145deg, #8b5cf6 0%, #7c3aed 100%);
  color: #f5f3ff;
  box-shadow: 0 4px 12px rgba(139, 92, 246, 0.25);
}
.stat-icon.orange {
  background: linear-gradient(145deg, #f59e0b 0%, #d97706 100%);
  color: #fffbeb;
  box-shadow: 0 4px 12px rgba(245, 158, 11, 0.25);
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: var(--text-primary);
  font-family: 'Noto Serif SC', serif;
}

.stat-label {
  font-size: 13px;
  color: var(--text-muted);
  margin-top: 4px;
}

/* Content Layout */
.content-layout {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
}

/* Card */
.card {
  background: var(--bg-card);
  border-radius: 16px;
  padding: 24px;
  box-shadow: var(--shadow-sm);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-title {
  font-size: 18px;
  color: var(--text-primary);
}

.card-link {
  font-size: 14px;
  color: var(--primary-color);
  cursor: pointer;
  transition: color 0.2s;
}

.card-link:hover {
  color: var(--primary-dark);
}

/* Scene List */
.scene-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.scene-item {
  padding: 18px;
  background: var(--bg-secondary);
  border-radius: 14px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 2px solid transparent;
  display: flex;
  justify-content: space-between;
  align-items: center;
  animation: fadeInLeft 0.5s ease both;
}

@keyframes fadeInLeft {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.scene-item:hover {
  background: var(--bg-hover);
  transform: translateX(5px);
}

.scene-item:nth-child(1) { animation-delay: 0.1s; }
.scene-item:nth-child(2) { animation-delay: 0.2s; }
.scene-item:nth-child(3) { animation-delay: 0.3s; }

.scene-item.active {
  border-color: var(--primary-color);
  background: rgba(90, 93, 67, 0.05);
}

.scene-header {
  display: flex;
  align-items: center;
  gap: 12px;
}

.scene-icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: currentColor;
}

.scene-icon.cinema {
  background: linear-gradient(145deg, #1a1a2e 0%, #2d2d44 100%);
  color: #fff;
  box-shadow: 0 4px 12px rgba(26, 26, 46, 0.3);
}
.scene-icon.home {
  background: linear-gradient(145deg, #2d5a3d 0%, #3d7a52 100%);
  color: #e8f5e9;
  box-shadow: 0 4px 12px rgba(45, 90, 61, 0.3);
}
.scene-icon.sleep {
  background: linear-gradient(145deg, #1e3a5f 0%, #2d4a7f 100%);
  color: #e3f2fd;
  box-shadow: 0 4px 12px rgba(30, 58, 95, 0.3);
}

.scene-name {
  font-weight: 500;
  margin-bottom: 4px;
}

.scene-desc {
  font-size: 13px;
  color: var(--text-muted);
}

/* Toggle Switch */
.toggle-switch {
  width: 44px;
  height: 24px;
  background: var(--border-color);
  border-radius: 12px;
  position: relative;
  cursor: pointer;
  transition: background 0.3s;
}

.toggle-switch.active {
  background: var(--primary-color);
}

.toggle-switch::after {
  content: '';
  position: absolute;
  width: 20px;
  height: 20px;
  background: white;
  border-radius: 50%;
  top: 2px;
  left: 2px;
  transition: transform 0.3s;
  box-shadow: var(--shadow-sm);
}

.toggle-switch.active::after {
  transform: translateX(20px);
}

/* Activity List */
.activity-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px;
  border-radius: 12px;
  transition: background 0.2s;
}

.activity-item:hover {
  background: var(--bg-secondary);
}

.activity-icon {
  font-size: 20px;
}

.activity-info {
  flex: 1;
}

.activity-text {
  font-size: 14px;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.activity-time {
  font-size: 12px;
  color: var(--text-muted);
}

/* Recommend Grid */
.recommend-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.recommend-card {
  background: var(--bg-card);
  border-radius: 14px;
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: all 0.3s;
  cursor: pointer;
}

.recommend-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.recommend-cover {
  height: 120px;
  background: linear-gradient(145deg, #1e3a5f 0%, #3d5a80 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  position: relative;
  overflow: hidden;
}

.recommend-cover::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
  opacity: 0.5;
}

.recommend-info {
  padding: 16px;
}

.recommend-title {
  font-weight: 500;
  margin-bottom: 6px;
  font-size: 14px;
}

.recommend-desc {
  font-size: 12px;
  color: var(--text-muted);
}
</style>
