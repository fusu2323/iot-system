<!-- 专业图标组件 - 精质 SVG 图标库 -->

<template>
  <svg
    v-if="name"
    :class="className"
    :width="size"
    :height="size"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    stroke-width="1.5"
    stroke-linecap="round"
    stroke-linejoin="round"
  >
    <!-- 首页/温馨家 - 精修版 -->
    <g v-if="name === 'home'">
      <path d="M3 10.5L12 3l9 7.5V20a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-9.5z"/>
      <path d="M9 22V12h6v10"/>
      <circle cx="12" cy="7" r="1" fill="currentColor"/>
    </g>

    <!-- 设备管理 - 精修版 -->
    <g v-if="name === 'devices'">
      <rect x="4" y="3" width="16" height="18" rx="2.5"/>
      <circle cx="12" cy="17" r="1.5" fill="currentColor"/>
      <line x1="8" y1="7" x2="16" y2="7"/>
      <line x1="8" y1="10" x2="16" y2="10"/>
    </g>

    <!-- 场景管理 - 精修版 -->
    <g v-if="name === 'scenes'">
      <path d="M4 4l11 8-11 8V4z"/>
      <path d="M15 4l5 4-5 4V4z"/>
    </g>

    <!-- 内容管理 - 精修版 -->
    <g v-if="name === 'content'">
      <rect x="3" y="4" width="18" height="16" rx="2"/>
      <polygon points="10,9 10,15 15,12 10,9"/>
      <circle cx="12" cy="12" r="0.5" fill="currentColor"/>
    </g>

    <!-- 推荐管理 - 精修版 -->
    <g v-if="name === 'recommend'">
      <path d="M12 2l2.4 7.2h7.6l-6 4.8 2.4 7.2-6.4-4.8-6.4 4.8 2.4-7.2-6-4.8h7.6z"/>
      <circle cx="12" cy="12" r="2" fill="currentColor"/>
    </g>

    <!-- 用户管理 - 精修版 -->
    <g v-if="name === 'users'">
      <circle cx="9" cy="8" r="3.5"/>
      <path d="M3 20v-1.5c0-2.5 2.5-4.5 6-4.5s6 2 6 4.5V20"/>
      <circle cx="17" cy="7" r="2.5"/>
      <path d="M21 20v-1c0-1.5-1.5-2.5-3.5-2.5"/>
    </g>

    <!-- 日志管理 - 精修版 -->
    <g v-if="name === 'logs'">
      <rect x="4" y="3" width="16" height="18" rx="1.5"/>
      <line x1="8" y1="8" x2="16" y2="8"/>
      <line x1="8" y1="12" x2="14" y2="12"/>
      <line x1="8" y1="16" x2="13" y2="16"/>
      <path d="M12 3v4h4"/>
    </g>

    <!-- 搜索 - 精修版 -->
    <g v-if="name === 'search'">
      <circle cx="10" cy="10" r="6"/>
      <line x1="14.5" y1="14.5" x2="19" y2="19"/>
    </g>

    <!-- 通知 - 精修版 -->
    <g v-if="name === 'notification'">
      <path d="M12 22a2.5 2.5 0 0 0 2.5-2.5h-5A2.5 2.5 0 0 0 12 22z"/>
      <path d="M18 10.5c0-3.5-2.5-6.5-6-7v-1 1c-3.5.5-6 3.5-6 7v3l-2 2v1h16v-1l-2-2v-3z"/>
    </g>

    <!-- 用户头像 - 精修版 -->
    <g v-if="name === 'user'">
      <circle cx="12" cy="8" r="4"/>
      <path d="M5 21v-2a7 7 0 0 1 14 0v2"/>
    </g>

    <!-- 添加 - 精修版 -->
    <g v-if="name === 'add'">
      <circle cx="12" cy="12" r="9"/>
      <line x1="12" y1="7" x2="12" y2="17"/>
      <line x1="7" y1="12" x2="17" y2="12"/>
    </g>

    <!-- 编辑 - 精修版 -->
    <g v-if="name === 'edit'">
      <path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25z"/>
      <path d="M14.06 4.94l3.75 3.75"/>
    </g>

    <!-- 删除 - 精修版 -->
    <g v-if="name === 'delete'">
      <path d="M4 7h16"/>
      <path d="M9 7V5a2 2 0 0 1 2-2h2a2 2 0 0 1 2 2v2"/>
      <rect x="6" y="7" width="12" height="14" rx="1.5"/>
      <line x1="10" y1="11" x2="10" y2="17"/>
      <line x1="14" y1="11" x2="14" y2="17"/>
    </g>

    <!-- 开关 - 精修版 -->
    <g v-if="name === 'toggle'">
      <rect x="2" y="6" width="20" height="12" rx="6"/>
      <circle cx="8" cy="12" r="3.5" fill="currentColor"/>
    </g>

    <!-- 开关开启 -->
    <g v-if="name === 'toggle-on'">
      <rect x="2" y="6" width="20" height="12" rx="6"/>
      <circle cx="16" cy="12" r="3.5" fill="currentColor"/>
    </g>

    <!-- 播放 - 精修版 -->
    <g v-if="name === 'play'">
      <polygon points="7 4 19 12 7 20 7 4"/>
    </g>

    <!-- 点赞 - 精修版 -->
    <g v-if="name === 'like'">
      <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3H14z"/>
      <path d="M7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3"/>
    </g>

    <!-- 不喜欢 - 精修版 -->
    <g v-if="name === 'dislike'">
      <path d="M10 15v4a3 3 0 0 0 3 3l4-9V2H5.72a2 2 0 0 0-2 1.7l-1.38 9a2 2 0 0 0 2 2.3H10z"/>
      <path d="M17 2h2.67a2.31 2.31 0 0 1 2 2v7a2.31 2.31 0 0 1-2 2H17"/>
    </g>

    <!-- 设备 - 灯光 -->
    <g v-if="name === 'device-light'">
      <path d="M9 19h6a1 1 0 0 0 1-1v-4.5a4 4 0 0 0-8 0V18a1 1 0 0 0 1 1z"/>
      <path d="M12 2v3M12 5a3 3 0 0 1 3 3h-6a3 3 0 0 1 3-3z"/>
      <line x1="5" y1="9" x2="5" y2="9"/>
      <line x1="19" y1="9" x2="19" y2="9"/>
    </g>

    <!-- 设备 - 空调 -->
    <g v-if="name === 'device-air'">
      <rect x="3" y="5" width="18" height="14" rx="2"/>
      <line x1="6" y1="9" x2="18" y2="9"/>
      <circle cx="8" cy="14" r="1.5" fill="currentColor"/>
      <circle cx="12" cy="14" r="1.5" fill="currentColor"/>
      <circle cx="16" cy="14" r="1.5" fill="currentColor"/>
    </g>

    <!-- 设备 - 电视 -->
    <g v-if="name === 'device-tv'">
      <rect x="2" y="6" width="20" height="12" rx="1.5"/>
      <path d="M8 20h8"/>
      <rect x="5" y="8" width="14" height="8" fill="currentColor" opacity="0.3"/>
    </g>

    <!-- 设备 - 音响 -->
    <g v-if="name === 'device-speaker'">
      <rect x="5" y="3" width="14" height="18" rx="2"/>
      <circle cx="12" cy="9" r="3"/>
      <circle cx="12" cy="16" r="2"/>
    </g>

    <!-- 设备 - 窗帘 -->
    <g v-if="name === 'device-curtain'">
      <rect x="3" y="4" width="18" height="16" rx="1"/>
      <line x1="3" y1="8" x2="21" y2="8"/>
      <path d="M8 8v10M16 8v10"/>
    </g>

    <!-- 设备 - 门锁 -->
    <g v-if="name === 'device-lock'">
      <rect x="5" y="9" width="14" height="12" rx="1.5"/>
      <path d="M8 9V7a4 4 0 0 1 8 0v2"/>
      <circle cx="12" cy="14" r="1.5" fill="currentColor"/>
      <line x1="12" y1="15.5" x2="12" y2="17"/>
    </g>

    <!-- 设备 - 摄像头 -->
    <g v-if="name === 'device-camera'">
      <rect x="3" y="7" width="14" height="12" rx="2"/>
      <circle cx="9" cy="13" r="3"/>
      <path d="M17 7l4 4-4 4V7z"/>
    </g>

    <!-- 设备 - 传感器 -->
    <g v-if="name === 'device-sensor'">
      <rect x="4" y="4" width="16" height="16" rx="3"/>
      <circle cx="12" cy="12" r="3" fill="currentColor"/>
      <line x1="12" y1="2" x2="12" y2="4"/>
      <line x1="12" y1="20" x2="12" y2="22"/>
      <line x1="2" y1="12" x2="4" y2="12"/>
      <line x1="20" y1="12" x2="22" y2="12"/>
    </g>

    <!-- 场景 - 观影 -->
    <g v-if="name === 'scene-cinema'">
      <rect x="3" y="5" width="18" height="14" rx="2"/>
      <polygon points="10,9 10,15 15,12 10,9" fill="currentColor"/>
      <line x1="5" y1="8" x2="19" y2="8" opacity="0.3"/>
    </g>

    <!-- 场景 - 回家 -->
    <g v-if="name === 'scene-home'">
      <path d="M3 11l9-8 9 8v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-9z"/>
      <rect x="9" y="13" width="6" height="8" fill="currentColor" opacity="0.3"/>
    </g>

    <!-- 场景 - 睡眠 -->
    <g v-if="name === 'scene-sleep'">
      <path d="M12 3a7 7 0 0 0-7 7c0 5 3 7 3 9h8c0-2 3-4 3-9a7 7 0 0 0-7-7z"/>
      <circle cx="15" cy="8" r="1" fill="currentColor"/>
      <circle cx="17" cy="6" r="0.5" fill="currentColor"/>
    </g>

    <!-- 场景 - 离家 -->
    <g v-if="name === 'scene-leave'">
      <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
      <polyline points="16 17 21 12 16 7"/>
      <line x1="21" y1="12" x2="9" y2="12"/>
    </g>

    <!-- 内容 - 电影 -->
    <g v-if="name === 'content-movie'">
      <rect x="3" y="5" width="18" height="14" rx="2"/>
      <polygon points="11,9 11,15 16,12 11,9" fill="currentColor"/>
    </g>

    <!-- 内容 - 电视剧 -->
    <g v-if="name === 'content-drama'">
      <rect x="3" y="4" width="18" height="16" rx="2"/>
      <line x1="3" y1="9" x2="21" y2="9"/>
      <line x1="3" y1="15" x2="21" y2="15"/>
      <line x1="9" y1="4" x2="9" y2="20"/>
      <line x1="15" y1="4" x2="15" y2="20"/>
    </g>

    <!-- 内容 - 音乐 -->
    <g v-if="name === 'content-music'">
      <path d="M9 18V7l8-2v11"/>
      <circle cx="7" cy="18" r="3" fill="currentColor"/>
      <circle cx="15" cy="16" r="3" fill="currentColor"/>
    </g>

    <!-- 内容 - 课程 -->
    <g v-if="name === 'content-course'">
      <path d="M4 6l8-4 8 4-8 4-8-4z"/>
      <path d="M4 6v10l8 4 8-4V6"/>
      <line x1="12" y1="14" x2="12" y2="20"/>
    </g>

    <!-- 统计 - 设备 -->
    <g v-if="name === 'stat-devices'">
      <rect x="4" y="4" width="16" height="16" rx="2"/>
      <circle cx="12" cy="10" r="2.5"/>
      <path d="M8 16h8"/>
    </g>

    <!-- 统计 - 在线 -->
    <g v-if="name === 'stat-online'">
      <path d="M12 2a10 10 0 1 0 10 10"/>
      <path d="M12 6v6l4 3"/>
      <circle cx="12" cy="12" r="1.5" fill="currentColor"/>
    </g>

    <!-- 统计 - 场景 -->
    <g v-if="name === 'stat-scenes'">
      <polygon points="4 4 20 12 4 20 4 4"/>
      <circle cx="10" cy="12" r="2" fill="currentColor"/>
    </g>

    <!-- 统计 - 内容 -->
    <g v-if="name === 'stat-content'">
      <rect x="4" y="6" width="16" height="12" rx="1.5"/>
      <polygon points="10,10 10,14 14,12 10,10" fill="currentColor"/>
    </g>

    <!-- 统计 - 用户 -->
    <g v-if="name === 'stat-users'">
      <circle cx="9" cy="7" r="3.5"/>
      <path d="M3 20v-2a6 6 0 0 1 12 0v2"/>
      <circle cx="16" cy="6" r="2.5"/>
      <path d="M18 20v-1a4 4 0 0 0-4-4"/>
    </g>

    <!-- 通用 - 确认 -->
    <g v-if="name === 'check'">
      <polyline points="4 12 9 17 20 6"/>
    </g>

    <!-- 通用 - 关闭 -->
    <g v-if="name === 'close'">
      <line x1="5" y1="5" x2="19" y2="19"/>
      <line x1="19" y1="5" x2="5" y2="19"/>
    </g>

    <!-- 通用 - 右箭头 -->
    <g v-if="name === 'chevron-right'">
      <polyline points="8 4 16 12 8 20"/>
    </g>

    <!-- 通用 - 下载 -->
    <g v-if="name === 'download'">
      <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
      <polyline points="7 10 12 15 17 10"/>
      <line x1="12" y1="15" x2="12" y2="3"/>
    </g>

    <!-- 统计 - 家庭 -->
    <g v-if="name === 'stat-family'">
      <path d="M3 10l9-7 9 7v10a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V10z"/>
      <rect x="9" y="12" width="6" height="8" fill="currentColor" opacity="0.3"/>
    </g>

    <!-- 使用统计 -->
    <g v-if="name === 'stats'">
      <line x1="6" y1="4" x2="6" y2="20"/>
      <line x1="12" y1="8" x2="12" y2="20"/>
      <line x1="18" y1="12" x2="18" y2="20"/>
      <line x1="3" y1="4" x2="21" y2="4"/>
    </g>

    <!-- 更多图标可扩展 -->
  </svg>
</template>

<script setup lang="ts">
import { computed } from 'vue';

interface Props {
  name: string;
  size?: number;
  class?: string;
}

const props = withDefaults(defineProps<Props>(), {
  size: 24,
  class: '',
});

const className = computed(() => {
  return `icon-${props.name} ${props.class}`;
});
</script>

<style scoped>
svg {
  display: block;
  flex-shrink: 0;
}

/* 优化图标渲染 */
.icon-home,
.icon-scenes,
.icon-recommend,
.icon-like,
.icon-dislike {
  filter: drop-shadow(0 0.5px 0.5px rgba(0, 0, 0, 0.05));
}

/* 设备图标特殊优化 */
.icon-device-light,
.icon-device-air,
.icon-device-tv,
.icon-device-speaker {
  filter: drop-shadow(0 0.5px 0.5px rgba(0, 0, 0, 0.08));
}
</style>
