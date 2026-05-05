<template>
  <!-- 点击烟花效果 -->
  <div class="click-fx-container">
    <div
      v-for="firework in fireworks"
      :key="firework.id"
      class="firework"
      :style="{
        left: firework.x + 'px',
        top: firework.y + 'px',
      }"
    >
      <div
        v-for="n in 12"
        :key="n"
        class="firework-particle"
        :style="{
          transform: `rotate(${(n - 1) * 30}deg) translateY(${firework.progress * 50}px)`,
          opacity: 1 - firework.progress,
          background: (firework as any).color || 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        }"
      ></div>
    </div>
  </div>

  <!-- 彩蛋弹窗 -->
  <transition name="bounce">
    <div v-if="showEgg" class="easter-egg" @click="closeEgg">
      <div class="egg-content">
        <div class="egg-icon">
          <Icon :name="eggData.icon" :size="48" />
        </div>
        <div class="egg-text">{{ eggData.text }}</div>
      </div>
    </div>
  </transition>

  <!-- 加载进度条 -->
  <transition name="slide">
    <div v-if="showLoading" class="loading-bar">
      <div class="loading-bar-fill" :style="{ width: loadingProgress + '%' }"></div>
    </div>
  </transition>

  <!-- 每日问候 -->
  <transition name="fade">
    <div v-if="showGreeting" class="daily-greeting" @click="showGreeting = false">
      <div class="greeting-content">
        <span class="greeting-icon">
          <Icon :name="greeting.icon" :size="24" />
        </span>
        <span class="greeting-text">{{ greeting.text }}</span>
      </div>
    </div>
  </transition>

  <!-- 页面切换覆盖层 -->
  <div class="page-overlay" :class="{ active: showOverlay }">
    <div class="overlay-spinner"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import Icon from '@/components/Icon.vue';

interface Firework {
  id: number;
  x: number;
  y: number;
  progress: number;
  color?: string;
}

const fireworks = ref<Firework[]>([]);
const fireworkId = ref(0);

const showEgg = ref(false);
const eggData = ref({ icon: 'stat-scenes', text: '' });

const showLoading = ref(false);
const loadingProgress = ref(0);

const showGreeting = ref(false);
const greeting = ref({ icon: 'stat-online', text: '' });

const showOverlay = ref(false);

const route = useRoute();

// 烟花效果
const createFirework = (x: number, y: number) => {
  const id = fireworkId.value++;
  // 随机颜色
  const colors = [
    'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
    'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
  ];
  const color = colors[Math.floor(Math.random() * colors.length)];

  fireworks.value.push({ id, x, y, progress: 0, color: color as any });

  let progress = 0;
  const animate = () => {
    progress += 0.02;
    const fw = fireworks.value.find((f) => f.id === id);
    if (fw) {
      (fw as any).progress = progress;
      if (progress < 1) {
        requestAnimationFrame(animate);
      } else {
        fireworks.value = fireworks.value.filter((f) => f.id !== id);
      }
    }
  };
  requestAnimationFrame(animate);
};

// 监听触发烟花事件
const handleTriggerFirework = (e: Event) => {
  const customEvent = e as CustomEvent;
  createFirework(customEvent.detail.x, customEvent.detail.y);
};

// 显示彩蛋
const showEasterEgg = (icon: string, text: string) => {
  eggData.value = { icon, text };
  showEgg.value = true;
  setTimeout(() => {
    showEgg.value = false;
  }, 3000);
};

// 显示加载进度
const showProgress = () => {
  showLoading.value = true;
  loadingProgress.value = 0;
  const interval = setInterval(() => {
    loadingProgress.value += Math.random() * 20;
    if (loadingProgress.value >= 100) {
      loadingProgress.value = 100;
      clearInterval(interval);
      setTimeout(() => {
        showLoading.value = false;
      }, 300);
    }
  }, 100);
};

// 显示每日问候
const showDailyGreeting = () => {
  const hour = new Date().getHours();
  const greetings: Record<string, { icon: string; text: string }> = {
    morning: { icon: 'scene-home', text: '早安！美好的一天开始了～' },
    noon: { icon: 'stat-online', text: '午安！记得休息一下哦～' },
    afternoon: { icon: 'stat-devices', text: '下午好！加油，坚持就是胜利！' },
    evening: { icon: 'scene-sleep', text: '晚上好！辛苦了一天，休息一下吧～' },
    night: { icon: 'scene-sleep', text: '夜深了，早点休息，晚安～' },
  };

  let period = 'afternoon';
  if (hour >= 5 && hour < 9) period = 'morning';
  else if (hour >= 9 && hour < 13) period = 'morning';
  else if (hour >= 13 && hour < 14) period = 'noon';
  else if (hour >= 14 && hour < 18) period = 'afternoon';
  else if (hour >= 18 && hour < 21) period = 'evening';
  else period = 'night';

  greeting.value = greetings[period] || { icon: 'stat-online', text: '你好！' };
  showGreeting.value = true;

  // 5 秒后自动消失
  setTimeout(() => {
    showGreeting.value = false;
  }, 5000);
};

// 页面切换动画
watch(() => route.path, () => {
  showOverlay.value = true;
  setTimeout(() => {
    showOverlay.value = false;
  }, 300);
});

// 彩蛋触发 - 连续点击
let clickCount = 0;
let clickTimer: number | null = null;
let logoClickCount = 0;

const handleDocumentClick = (e: MouseEvent) => {
  const target = e.target as HTMLElement;

  // 头像点击彩蛋
  if (target.closest('.nav-user-avatar')) {
    clickCount++;
    clearTimeout(clickTimer as number);
    clickTimer = window.setTimeout(() => {
      if (clickCount >= 3) {
        const messages: string[] = [
          '别戳啦，再戳要害羞了',
          '管理员大人，我错了还不行嘛～',
          '您今天真好看！',
          '戳戳戳～ 戳出好运来！',
        ];
        const randomMsg = messages[Math.floor(Math.random() * messages.length)];
        if (randomMsg) {
          showEasterEgg('stat-users', randomMsg);
        }
      }
      clickCount = 0;
    }, 500);
  }

  // 统计卡片点击彩蛋
  if (target.closest('.stat-card')) {
    const statCards = document.querySelectorAll('.stat-card');
    statCards.forEach((card, index) => {
      setTimeout(() => {
        const rect = card.getBoundingClientRect();
        createFirework(rect.left + rect.width / 2, rect.top + rect.height / 2);
      }, index * 100);
    });
  }
};

// 按键盘彩蛋
let keySequence: string[] = [];
const handleKeydown = (e: KeyboardEvent) => {
  keySequence.push(e.key);
  if (keySequence.length > 10) {
    keySequence.shift();
  }

  const konamiCode = ['ArrowUp', 'ArrowUp', 'ArrowDown', 'ArrowDown', 'ArrowLeft', 'ArrowRight', 'ArrowLeft', 'ArrowRight', 'b', 'a'];
  if (keySequence.join(',') === konamiCode.join(',')) {
    showEasterEgg('content-drama', 'Konami Code！你是个真正的玩家！');
    keySequence = [];
  }

  // 空格键彩蛋
  if (e.key === ' ') {
    e.preventDefault();
    const rect = document.querySelector('.nav-user-avatar')?.getBoundingClientRect();
    if (rect) {
      createFirework(rect.left + rect.width / 2, rect.top + rect.height / 2);
    }
  }
};

// 监听自定义彩蛋事件
const handleShowEgg = (e: Event) => {
  const customEvent = e as CustomEvent;
  showEasterEgg(customEvent.detail.icon, customEvent.detail.text);
};

onMounted(() => {
  document.addEventListener('click', handleDocumentClick);
  document.addEventListener('keydown', handleKeydown);
  window.addEventListener('showEasterEgg', handleShowEgg);
  window.addEventListener('triggerFirework', handleTriggerFirework);

  // 页面加载时显示问候
  setTimeout(() => {
    showDailyGreeting();
  }, 500);

  // 监听页面切换
  window.addEventListener('beforeunload', () => {
    showProgress();
  });
});

onUnmounted(() => {
  document.removeEventListener('click', handleDocumentClick);
  document.removeEventListener('keydown', handleKeydown);
  window.removeEventListener('showEasterEgg', handleShowEgg);
  window.removeEventListener('triggerFirework', handleTriggerFirework);
});

const closeEgg = () => {
  showEgg.value = false;
};
</script>

<style scoped>
/* 烟花容器 */
.click-fx-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 9998;
}

.firework {
  position: absolute;
  width: 0;
  height: 0;
}

.firework-particle {
  position: absolute;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  pointer-events: none;
  box-shadow: 0 0 10px rgba(102, 126, 234, 0.5);
  animation: particle-fade 0.8s ease-out forwards;
}

@keyframes particle-fade {
  0% {
    opacity: 1;
    transform: scale(1);
  }
  100% {
    opacity: 0;
    transform: scale(0);
  }
}

/* 彩蛋弹窗 */
.easter-egg {
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translateX(-50%);
  z-index: 9999;
  cursor: pointer;
}

.egg-content {
  background: var(--bg-card);
  border-radius: 20px;
  padding: 24px 32px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  text-align: center;
  border: 2px solid var(--primary-color);
}

.egg-icon {
  width: 56px;
  height: 56px;
  margin-bottom: 12px;
  color: var(--primary-color);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  animation: egg-bounce 0.6s ease infinite;
}

@keyframes egg-bounce {
  0%, 100% { transform: translateY(0) scale(1); }
  50% { transform: translateY(-15px) scale(1.05); }
}

.egg-text {
  font-size: 16px;
  color: var(--text-primary);
  font-weight: 500;
}

/* 弹跳进入动画 */
.bounce-enter-active {
  animation: bounce-in 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.bounce-leave-active {
  animation: bounce-out 0.3s ease;
}

@keyframes bounce-in {
  0% {
    transform: translateX(-50%) scale(0) rotate(-10deg);
    opacity: 0;
  }
  50% {
    transform: translateX(-50%) scale(1.2) rotate(5deg);
  }
  100% {
    transform: translateX(-50%) scale(1) rotate(0);
    opacity: 1;
  }
}

@keyframes bounce-out {
  0% {
    transform: translateX(-50%) scale(1);
    opacity: 1;
  }
  100% {
    transform: translateX(-50%) scale(0.3) rotate(20deg);
    opacity: 0;
  }
}

/* 加载进度条 */
.loading-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background: var(--bg-secondary);
  z-index: 10000;
}

.loading-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--primary-color), var(--primary-light));
  transition: width 0.1s ease;
  box-shadow: 0 0 10px var(--primary-color);
}

/* 幻灯片进出动画 */
.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
}

.slide-enter-from {
  transform: translateY(-100%);
}

.slide-leave-to {
  transform: translateY(-100%);
}

/* 每日问候 */
.daily-greeting {
  position: fixed;
  top: 80px;
  right: 32px;
  z-index: 9997;
  cursor: pointer;
}

.greeting-content {
  background: var(--bg-card);
  border-radius: 16px;
  padding: 16px 24px;
  box-shadow: var(--shadow-lg);
  display: flex;
  align-items: center;
  gap: 12px;
  border: 1px solid var(--border-color);
}

.greeting-icon {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-color);
}

.greeting-text {
  font-size: 14px;
  color: var(--text-primary);
  font-weight: 500;
}

/* 淡入淡出动画 */
.fade-enter-active,
.fade-leave-active {
  transition: all 0.5s ease;
}

.fade-enter-from {
  opacity: 0;
  transform: translateY(-20px);
}

.fade-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

/* 页面切换覆盖层 */
.page-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: var(--bg-primary);
  z-index: 9996;
  pointer-events: none;
  opacity: 0;
  transition: opacity 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.page-overlay.active {
  opacity: 1;
}

.overlay-spinner {
  width: 50px;
  height: 50px;
  border: 3px solid var(--border-color);
  border-top-color: var(--primary-color);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
