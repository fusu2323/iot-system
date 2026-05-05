<template>
  <div class="usage-view">

    <!-- Tab 切换 -->
    <div class="tab-bar">
      <button
        v-for="tab in tabs"
        :key="tab.key"
        :class="['tab-btn', { active: activeTab === tab.key }]"
        @click="switchTab(tab.key)"
      >
        <Icon :name="tab.icon" :size="16" />
        {{ tab.label }}
      </button>
    </div>

    <!-- ==================== 我的使用统计 ==================== -->
    <div v-if="activeTab === 'my'" class="panel">
      <!-- 控制栏 -->
      <div class="panel-toolbar">
        <div class="dimension-group">
          <span class="toolbar-label">统计维度</span>
          <button
            v-for="dim in dimensions"
            :key="dim.value"
            :class="['dim-btn', { active: currentDim === dim.value }]"
            @click="changeDimension(dim.value)"
          >
            {{ dim.label }}
          </button>
        </div>
        <div class="target-group">
          <span class="toolbar-label">类型筛选</span>
          <select v-model="filterType" class="filter-select" @change="loadMyStats">
            <option value="">全部</option>
            <option value="DEVICE">设备</option>
            <option value="SCENE">场景</option>
          </select>
        </div>
        <div class="date-range">
          <span class="toolbar-label">时间范围</span>
          <input v-model="startDate" type="date" class="date-input" @change="loadMyStats" />
          <span class="date-sep">至</span>
          <input v-model="endDate" type="date" class="date-input" @change="loadMyStats" />
        </div>
        <button class="btn btn-primary" @click="loadMyStats">
          <Icon name="search" :size="16" /> 查询
        </button>
      </div>

      <!-- 统计卡片概览 -->
      <div v-if="myStats.statistics && myStats.statistics.length > 0" class="overview-cards">
        <div class="overview-card green">
          <div class="overview-icon"><Icon name="stat-devices" :size="24" /></div>
          <div class="overview-info">
            <div class="overview-value">{{ getTotalCount(myStats.statistics) }}</div>
            <div class="overview-label">总使用次数</div>
          </div>
        </div>
        <div class="overview-card blue">
          <div class="overview-icon"><Icon name="stat-scenes" :size="24" /></div>
          <div class="overview-info">
            <div class="overview-value">{{ getDeviceCount }}</div>
            <div class="overview-label">设备激活次数</div>
          </div>
        </div>
        <div class="overview-card pink">
          <div class="overview-icon"><Icon name="scenes" :size="24" /></div>
          <div class="overview-info">
            <div class="overview-value">{{ getSceneCount }}</div>
            <div class="overview-label">场景触发次数</div>
          </div>
        </div>
        <div class="overview-card purple">
          <div class="overview-icon"><Icon name="stat-content" :size="24" /></div>
          <div class="overview-info">
            <div class="overview-value">{{ myStats.statistics.length }}</div>
            <div class="overview-label">统计天数</div>
          </div>
        </div>
      </div>

      <!-- 统计图表（列表形式） -->
      <div class="card chart-card">
        <div class="card-header">
          <h3 class="card-title">
            {{ currentDimLabel }} 维度使用趋势
          </h3>
          <span class="card-subtitle">
            共 {{ myStats.statistics ? myStats.statistics.length : 0 }} 条记录
          </span>
        </div>
        <div v-if="myStats.statistics && myStats.statistics.length > 0" class="stat-bars">
          <div
            v-for="(item, index) in myStats.statistics"
            :key="index"
            class="stat-bar-row"
            :style="{ animationDelay: index * 0.05 + 's' }"
          >
            <div class="stat-bar-label">{{ item.statKey }}</div>
            <div class="stat-bar-track">
              <div
                class="stat-bar-fill"
                :class="filterType ? 'single' : getBarColor(item.statKey)"
                :style="{ width: getBarWidth(item.count) + '%' }"
              ></div>
            </div>
            <div class="stat-bar-value">{{ item.count }} 次</div>
          </div>
        </div>
        <div v-else class="empty-state">
          <Icon name="stat-devices" :size="48" />
          <p>暂无统计数据</p>
          <p class="empty-hint">触发设备或场景后将自动记录</p>
        </div>
      </div>
    </div>

    <!-- ==================== 全局使用排行 ==================== -->
    <div v-if="activeTab === 'ranking'" class="panel">
      <div class="panel-toolbar">
        <div class="target-group">
          <span class="toolbar-label">排行类型</span>
          <select v-model="rankingType" class="filter-select" @change="loadRanking">
            <option value="">全部</option>
            <option value="DEVICE">设备排行</option>
            <option value="SCENE">场景排行</option>
          </select>
        </div>
        <button class="btn btn-primary" @click="loadRanking">
          <Icon name="search" :size="16" /> 刷新排行
        </button>
      </div>

      <!-- 排行榜 -->
      <div class="card ranking-card">
        <div class="card-header">
          <h3 class="card-title">全网使用排行 TOP 10</h3>
          <span class="card-subtitle">管理员专属视图</span>
        </div>
        <div v-if="ranking.rankings && ranking.rankings.length > 0" class="ranking-list">
          <div
            v-for="(item, index) in ranking.rankings"
            :key="item.targetId"
            class="ranking-item"
            :class="getRankClass(index + 1)"
            :style="{ animationDelay: index * 0.06 + 's' }"
          >
            <div class="rank-badge" :class="getRankBadgeClass(index + 1)">
              <span v-if="index < 3">{{ getRankMedal(index + 1) }}</span>
              <span v-else>{{ index + 1 }}</span>
            </div>
            <div class="rank-icon" :class="item.targetType === 'DEVICE' ? 'device-color' : 'scene-color'">
              <Icon :name="item.targetType === 'DEVICE' ? 'devices' : 'scenes'" :size="18" />
            </div>
            <div class="rank-info">
              <div class="rank-name">{{ item.targetName }}</div>
              <div class="rank-meta">{{ item.targetType === 'DEVICE' ? '设备' : '场景' }} · ID: {{ item.targetId }}</div>
            </div>
            <div class="rank-count">
              <div class="rank-count-num">{{ item.count }}</div>
              <div class="rank-count-label">次</div>
            </div>
          </div>
        </div>
        <div v-else class="empty-state">
          <Icon name="stat-users" :size="48" />
          <p>暂无排行数据</p>
          <p class="empty-hint">等待用户使用数据积累</p>
        </div>
      </div>
    </div>

    <!-- ==================== 历史时间线 ==================== -->
    <div v-if="activeTab === 'timeline'" class="panel">
      <div class="panel-toolbar">
        <div class="target-group">
          <span class="toolbar-label">目标类型</span>
          <select v-model="timelineTargetType" class="filter-select">
            <option value="DEVICE">设备</option>
            <option value="SCENE">场景</option>
          </select>
        </div>
        <div class="target-id-group">
          <span class="toolbar-label">{{ timelineTargetType === 'DEVICE' ? '设备' : '场景' }} ID</span>
          <input v-model.number="timelineTargetId" type="number" class="id-input" placeholder="输入 ID" min="1" />
        </div>
        <button class="btn btn-primary" @click="loadTimeline">
          <Icon name="search" :size="16" /> 查询时间线
        </button>
      </div>

      <!-- 时间线 -->
      <div class="card timeline-card">
        <div class="card-header">
          <h3 class="card-title">
            {{ timelineTargetType === 'DEVICE' ? '设备' : '场景' }} {{ timelineTargetId }} 历史记录
          </h3>
          <span class="card-subtitle" v-if="timeline.timeline">{{ timeline.timeline.length }} 条记录</span>
        </div>

        <div v-if="timeline.timeline && timeline.timeline.length > 0" class="timeline-list">
          <div
            v-for="(item, index) in timeline.timeline"
            :key="index"
            class="timeline-item"
            :style="{ animationDelay: index * 0.04 + 's' }"
          >
            <div class="timeline-dot" :class="item.action === 'ACTIVATE' ? 'activate' : 'trigger'"></div>
            <div class="timeline-connector" v-if="index < timeline.timeline.length - 1"></div>
            <div class="timeline-content">
              <div class="timeline-action">
                <span class="action-badge" :class="item.action === 'ACTIVATE' ? 'device-badge' : 'scene-badge'">
                  {{ item.action === 'ACTIVATE' ? '设备激活' : '场景触发' }}
                </span>
              </div>
              <div class="timeline-time">{{ formatTimelineTime(item.activateTime) }}</div>
            </div>
          </div>
        </div>
        <div v-else class="empty-state">
          <Icon name="logs" :size="48" />
          <p>暂无历史记录</p>
          <p class="empty-hint">输入设备或场景 ID 后查询</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import Icon from '@/components/Icon.vue';
import {
  getMyStatistics,
  getGlobalRanking,
  getTimeline,
  type UsageStats,
  type UsageRanking,
  type UsageTimeline,
} from '@/api/usage';

// ==================== Tab ====================
const activeTab = ref('my');
const tabs = [
  { key: 'my', label: '我的使用统计', icon: 'stats' },
  { key: 'ranking', label: '全局使用排行', icon: 'stat-users' },
  { key: 'timeline', label: '历史时间线', icon: 'logs' },
];
const switchTab = (key: string) => {
  activeTab.value = key;
  if (key === 'my') loadMyStats();
  else if (key === 'ranking') loadRanking();
  else if (key === 'timeline') {
    if (!timeline.targetId) loadTimeline();
  }
};

// ==================== 我的统计 ====================
const currentDim = ref('DAY');
const filterType = ref('');
const startDate = ref('');
const endDate = ref('');
const dimensions = [
  { value: 'DAY', label: '按日' },
  { value: 'WEEK', label: '按周' },
  { value: 'MONTH', label: '按月' },
];
const currentDimLabel = computed(() => {
  return dimensions.find(d => d.value === currentDim.value)?.label || '日';
});

const myStats = ref<UsageStats>({ targetType: '', dimension: '', statistics: [] });

const changeDimension = (dim: string) => {
  currentDim.value = dim;
  loadMyStats();
};

const getTotalCount = (stats: { count: number }[]) => {
  return stats.reduce((sum, s) => sum + (s.count || 0), 0);
};
const getDeviceCount = computed(() => {
  if (!myStats.value.statistics) return 0;
  return myStats.value.statistics.reduce((sum) => sum + 1, 0);
});
const getSceneCount = computed(() => getDeviceCount.value);

const maxCount = computed(() => {
  if (!myStats.value.statistics || myStats.value.statistics.length === 0) return 1;
  return Math.max(...myStats.value.statistics.map(s => s.count || 0), 1);
});
const getBarWidth = (count: number) => Math.max(Math.round((count / maxCount.value) * 100), 2);
const getBarColor = (key: string) => {
  if (key.includes('-') && !key.includes('W')) return 'blue';
  if (key.includes('W')) return 'purple';
  return 'green';
};

const loadMyStats = async () => {
  try {
    myStats.value = await getMyStatistics({
      dimension: currentDim.value,
      targetType: filterType.value || undefined,
      startDate: startDate.value || undefined,
      endDate: endDate.value || undefined,
    });
  } catch (e) {
    console.error('加载统计数据失败', e);
    myStats.value = { targetType: '', dimension: '', statistics: [] };
  }
};

// ==================== 全局排行 ====================
const rankingType = ref('');
const ranking = ref<UsageRanking>({ rankings: [] });

const getRankClass = (rank: number) => ({ 'rank-top': rank <= 3 });
const getRankBadgeClass = (rank: number) => ({
  'badge-gold': rank === 1,
  'badge-silver': rank === 2,
  'badge-bronze': rank === 3,
});
const getRankMedal = (rank: number) => ['🥇', '🥈', '🥉'][rank - 1] || rank;

const loadRanking = async () => {
  try {
    ranking.value = await getGlobalRanking({
      targetType: rankingType.value || undefined,
      limit: 10,
    });
  } catch (e) {
    console.error('加载排行失败', e);
    ranking.value = { rankings: [] };
  }
};

// ==================== 历史时间线 ====================
const timelineTargetType = ref('DEVICE');
const timelineTargetId = ref(0);
const timeline = ref<UsageTimeline>({ targetType: '', targetId: 0, targetName: '', timeline: [] });

const formatTimelineTime = (timeStr?: string) => {
  if (!timeStr) return '';
  const d = new Date(timeStr);
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}:${String(d.getSeconds()).padStart(2, '0')}`;
};

const loadTimeline = async () => {
  if (!timelineTargetId.value) return;
  try {
    timeline.value = await getTimeline(timelineTargetType.value, timelineTargetId.value);
  } catch (e) {
    console.error('加载时间线失败', e);
    timeline.value = { targetType: timelineTargetType.value, targetId: timelineTargetId.value, targetName: '', timeline: [] };
  }
};

onMounted(() => {
  loadMyStats();
});
</script>

<style scoped>
.usage-view {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* Tab Bar */
.tab-bar {
  display: flex;
  gap: 8px;
  background: var(--bg-card);
  padding: 6px;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  width: fit-content;
}

.tab-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  border: none;
  background: transparent;
  border-radius: var(--radius-md);
  cursor: pointer;
  font-size: 14px;
  color: var(--text-secondary);
  transition: all 0.2s ease;
  font-weight: 500;
}

.tab-btn:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.tab-btn.active {
  background: var(--primary-color);
  color: white;
}

/* Panel */
.panel {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* Toolbar */
.panel-toolbar {
  display: flex;
  align-items: center;
  gap: 16px;
  background: var(--bg-card);
  padding: 16px 20px;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  flex-wrap: wrap;
}

.toolbar-label {
  font-size: 13px;
  color: var(--text-muted);
  white-space: nowrap;
}

.dimension-group,
.target-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.dim-btn {
  padding: 6px 14px;
  border: 1px solid var(--border-color);
  background: var(--bg-card);
  border-radius: 20px;
  cursor: pointer;
  font-size: 13px;
  color: var(--text-secondary);
  transition: all 0.2s;
}

.dim-btn:hover {
  border-color: var(--primary-color);
  color: var(--primary-color);
}

.dim-btn.active {
  background: var(--primary-color);
  border-color: var(--primary-color);
  color: white;
}

.filter-select {
  padding: 7px 12px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 13px;
  color: var(--text-primary);
  cursor: pointer;
  outline: none;
}

.filter-select:focus {
  border-color: var(--primary-color);
}

.date-range {
  display: flex;
  align-items: center;
  gap: 8px;
}

.date-input {
  padding: 7px 12px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 13px;
  color: var(--text-primary);
  outline: none;
}

.date-input:focus {
  border-color: var(--primary-color);
}

.date-sep {
  color: var(--text-muted);
  font-size: 13px;
}

.target-id-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.id-input {
  padding: 7px 12px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 13px;
  color: var(--text-primary);
  width: 100px;
  outline: none;
}

.id-input:focus {
  border-color: var(--primary-color);
}

/* Btn */
.btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
}

.btn-primary {
  background: var(--primary-color);
  color: white;
}

.btn-primary:hover {
  background: var(--primary-dark);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(90, 93, 67, 0.25);
}

.btn-secondary {
  background: var(--bg-secondary);
  color: var(--text-primary);
  border: 1px solid var(--border-color);
}

.btn-secondary:hover {
  background: var(--bg-hover);
}

/* Overview Cards */
.overview-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.overview-card {
  background: var(--bg-card);
  border-radius: var(--radius-lg);
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: var(--shadow-sm);
  animation: slideInUp 0.5s ease both;
}

@keyframes slideInUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.overview-icon {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.overview-card.green .overview-icon { background: linear-gradient(145deg, #10b981, #059669); }
.overview-card.blue .overview-icon { background: linear-gradient(145deg, #3b82f6, #2563eb); }
.overview-card.pink .overview-icon { background: linear-gradient(145deg, #ec4899, #db2777); }
.overview-card.purple .overview-icon { background: linear-gradient(145deg, #8b5cf6, #7c3aed); }

.overview-value {
  font-size: 26px;
  font-weight: 700;
  color: var(--text-primary);
  font-family: 'Noto Serif SC', serif;
}

.overview-label {
  font-size: 12px;
  color: var(--text-muted);
  margin-top: 2px;
}

/* Card */
.card {
  background: var(--bg-card);
  border-radius: var(--radius-lg);
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
  font-size: 17px;
  color: var(--text-primary);
}

.card-subtitle {
  font-size: 13px;
  color: var(--text-muted);
}

/* Stat Bars */
.stat-bars {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.stat-bar-row {
  display: flex;
  align-items: center;
  gap: 12px;
  animation: slideInUp 0.4s ease both;
}

.stat-bar-label {
  font-size: 13px;
  color: var(--text-secondary);
  width: 100px;
  text-align: right;
  flex-shrink: 0;
  font-family: monospace;
}

.stat-bar-track {
  flex: 1;
  height: 20px;
  background: var(--bg-secondary);
  border-radius: 10px;
  overflow: hidden;
}

.stat-bar-fill {
  height: 100%;
  border-radius: 10px;
  transition: width 0.6s ease;
}

.stat-bar-fill.green { background: linear-gradient(90deg, #10b981, #059669); }
.stat-bar-fill.blue { background: linear-gradient(90deg, #3b82f6, #2563eb); }
.stat-bar-fill.purple { background: linear-gradient(90deg, #8b5cf6, #7c3aed); }
.stat-bar-fill.single { background: linear-gradient(90deg, var(--primary-color), var(--primary-dark)); }

.stat-bar-value {
  font-size: 13px;
  color: var(--text-primary);
  font-weight: 500;
  width: 60px;
  flex-shrink: 0;
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 48px 0;
  color: var(--text-muted);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.empty-state p {
  font-size: 15px;
  color: var(--text-secondary);
}

.empty-hint {
  font-size: 12px !important;
  color: var(--text-muted) !important;
}

/* Ranking */
.ranking-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.ranking-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 18px;
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  transition: all 0.2s;
  animation: slideInUp 0.4s ease both;
}

.ranking-item:hover {
  background: var(--bg-hover);
  transform: translateX(4px);
}

.ranking-item.rank-top {
  border: 1px solid var(--border-color);
}

.rank-badge {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  color: var(--text-secondary);
  flex-shrink: 0;
}

.badge-gold { background: linear-gradient(145deg, #f59e0b, #d97706); color: white; border: none; }
.badge-silver { background: linear-gradient(145deg, #94a3b8, #64748b); color: white; border: none; }
.badge-bronze { background: linear-gradient(145deg, #b45309, #92400e); color: white; border: none; }

.rank-icon {
  width: 38px;
  height: 38px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.rank-icon.device-color { background: rgba(59, 130, 246, 0.1); color: #3b82f6; }
.rank-icon.scene-color { background: rgba(236, 72, 153, 0.1); color: #ec4899; }

.rank-info {
  flex: 1;
  min-width: 0;
}

.rank-name {
  font-weight: 500;
  font-size: 14px;
  color: var(--text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.rank-meta {
  font-size: 12px;
  color: var(--text-muted);
  margin-top: 2px;
}

.rank-count {
  display: flex;
  align-items: baseline;
  gap: 2px;
  flex-shrink: 0;
}

.rank-count-num {
  font-size: 20px;
  font-weight: 700;
  color: var(--primary-color);
  font-family: 'Noto Serif SC', serif;
}

.rank-count-label {
  font-size: 12px;
  color: var(--text-muted);
}

/* Timeline */
.timeline-list {
  display: flex;
  flex-direction: column;
  gap: 0;
  padding-left: 20px;
  border-left: 2px solid var(--border-color);
  margin-left: 12px;
}

.timeline-item {
  position: relative;
  padding: 12px 0 12px 24px;
  animation: slideInUp 0.4s ease both;
}

.timeline-dot {
  position: absolute;
  left: -8px;
  top: 14px;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  border: 3px solid var(--bg-card);
  z-index: 1;
}

.timeline-dot.activate { background: #10b981; }
.timeline-dot.trigger { background: #ec4899; }

.timeline-connector {
  position: absolute;
  left: -2px;
  top: 28px;
  bottom: -12px;
  width: 2px;
  background: var(--border-color);
}

.timeline-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.action-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.device-badge { background: rgba(16, 185, 129, 0.1); color: #10b981; }
.scene-badge { background: rgba(236, 72, 153, 0.1); color: #ec4899; }

.timeline-time {
  font-size: 13px;
  color: var(--text-muted);
  font-family: monospace;
}
</style>
