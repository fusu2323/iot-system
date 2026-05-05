<template>
  <div class="recommend-view">
    <!-- Preference Settings -->
    <div class="card">
      <div class="card-header">
        <h3 class="card-title">我的偏好</h3>
        <button class="btn btn-primary btn-sm" @click="savePreferences">保存偏好</button>
      </div>
      <div class="preference-grid">
        <div class="preference-item">
          <label class="preference-label">内容类型偏好</label>
          <div class="preference-options">
            <label
              v-for="type in contentTypes"
              :key="type"
              :class="['option-tag', { active: preferences.types.includes(type) }]"
              @click="togglePreference('types', type)"
            >
              {{ type }}
            </label>
          </div>
        </div>
        <div class="preference-item">
          <label class="preference-label">分类偏好</label>
          <div class="preference-options">
            <label
              v-for="category in categories"
              :key="category"
              :class="['option-tag', { active: preferences.categories.includes(category) }]"
              @click="togglePreference('categories', category)"
            >
              {{ category }}
            </label>
          </div>
        </div>
      </div>
    </div>

    <!-- Recommend List -->
    <div class="recommend-grid">
      <div class="recommend-card" v-for="item in recommendations" :key="item.id">
        <div class="recommend-cover">
          <Icon :name="item.cover" :size="48" />
        </div>
        <div class="recommend-match">{{ item.matchRate }}% 匹配</div>
        <div class="recommend-info">
          <div class="recommend-title">{{ item.title }}</div>
          <span class="recommend-type">{{ item.type }}</span>
          <div class="recommend-desc">{{ item.description }}</div>
          <div class="recommend-actions">
            <div class="recommend-rate">
              <span @click="rateItem(item, 'like')">
                <Icon name="like" :size="18" />
              </span>
              <span @click="rateItem(item, 'dislike')">
                <Icon name="dislike" :size="18" />
              </span>
            </div>
            <span class="recommend-play" @click="playItem(item)">播放</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Toast -->
    <div v-if="toast.show" :class="['toast', toast.type]">
      {{ toast.message }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import Icon from '@/components/Icon.vue';
import { getRecommendations, recordClick, recordLike, recordDislike } from '@/api/recommend';
import { getPreferences, setPreference, type UserPreference } from '@/api/preferences';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();

// 内容类型与图标映射
const contentIconMap: Record<string, string> = {
  MOVIE: 'content-movie',
  GAME: 'content-drama',
  MUSIC: 'content-music',
};

// 类型映射
const typeTextMap: Record<string, string> = {
  MOVIE: '电影',
  GAME: '电视剧',
  MUSIC: '音乐',
};

const typeValueMap: Record<string, string> = {
  '电影': 'MOVIE',
  '电视剧': 'GAME',
  '音乐': 'MUSIC',
  '课程': 'GAME',
};

const categoryValueMap: Record<string, string> = {
  '科幻': '科幻',
  '动作': '动作',
  '剧情': '剧情',
  '喜剧': '喜剧',
  '音乐': '音乐',
  '教育': '教育',
};

const preferences = ref({
  types: [] as string[],
  categories: [] as string[],
});

const contentTypes = ['电影', '电视剧', '音乐', '课程'];
const categories = ['科幻', '动作', '剧情', '喜剧', '音乐', '教育'];

const recommendations = ref<any[]>([]);

const toast = ref({ show: false, type: 'success', message: '' });

// 加载偏好设置
const loadPreferences = async () => {
  try {
    const currentUserId = userStore.userInfo?.id || 1;
    const result = await getPreferences(currentUserId);
    if (result && result.length > 0) {
      // 从偏好中提取类型和分类
      const types: string[] = [];
      const categories: string[] = [];

      result.forEach((pref: UserPreference) => {
        const typeText = typeTextMap[pref.contentType];
        if (typeText) {
          types.push(typeText);
        } else if (!categories.includes(pref.contentType)) {
          // 假设不是预定义类型，则是分类
          categories.push(pref.contentType);
        }
      });

      preferences.value.types = types.length > 0 ? types : ['电影', '音乐'];
      preferences.value.categories = categories.length > 0 ? categories : ['科幻', '音乐'];
    } else {
      // 默认偏好
      preferences.value.types = ['电影', '音乐'];
      preferences.value.categories = ['科幻', '音乐'];
    }
  } catch (error) {
    console.error('加载偏好失败:', error);
    preferences.value.types = ['电影', '音乐'];
    preferences.value.categories = ['科幻', '音乐'];
  }
};

// 加载推荐列表
const loadRecommendations = async () => {
  try {
    const currentUserId = userStore.userInfo?.id || 1;
    const result = await getRecommendations({ userId: currentUserId, page: 1, size: 8 });
    recommendations.value = result.records.map((item: any) => ({
      id: item.id,
      cover: contentIconMap[item.contentType] || 'content-movie',
      title: item.contentTitle,
      type: typeTextMap[item.contentType] || item.contentType,
      description: item.reason || '根据您的偏好推荐',
      matchRate: item.matchScore || Math.floor(Math.random() * 20 + 75),
      contentId: item.contentId,
    }));
  } catch (error) {
    console.error('加载推荐失败:', error);
  }
};

const togglePreference = (type: 'types' | 'categories', value: string) => {
  const index = preferences.value[type].indexOf(value);
  if (index === -1) {
    preferences.value[type].push(value);
  } else {
    preferences.value[type].splice(index, 1);
  }
};

const savePreferences = async () => {
  try {
    const currentUserId = userStore.userInfo?.id || 1;

    // 保存类型偏好
    for (const type of preferences.value.types) {
      await setPreference(currentUserId, {
        contentType: typeValueMap[type] || type,
        preferenceScore: 1.0,
      });
    }

    // 保存分类偏好
    for (const category of preferences.value.categories) {
      await setPreference(currentUserId, {
        contentType: category,
        preferenceScore: 1.0,
      });
    }

    showToast('success', '偏好已保存');
    await loadRecommendations();
  } catch (error: any) {
    console.error('保存偏好失败:', error);
    showToast('error', error.response?.data?.message || '保存偏好失败');
  }
};

const rateItem = async (item: any, type: string) => {
  try {
    const currentUserId = userStore.userInfo?.id || 1;
    const contentId = item.contentId || item.id;

    if (type === 'like') {
      await recordLike(currentUserId, contentId);
      showToast('success', '已标记喜欢');
    } else {
      await recordDislike(currentUserId, contentId);
      showToast('success', '已标记不喜欢');
    }
    await loadRecommendations();
  } catch (error: any) {
    console.error('评分失败:', error);
    showToast('error', error.response?.data?.message || '评分失败');
  }
};

const playItem = async (item: any) => {
  try {
    const currentUserId = userStore.userInfo?.id || 1;
    const contentId = item.contentId || item.id;
    await recordClick(currentUserId, contentId);
    showToast('success', `正在播放 ${item.title}`);
  } catch (error: any) {
    console.error('记录播放失败:', error);
    showToast('error', '播放失败');
  }
};

const showToast = (type: string, message: string) => {
  toast.value = { show: true, type, message };
  setTimeout(() => {
    toast.value.show = false;
  }, 3000);
};

onMounted(() => {
  loadPreferences();
  loadRecommendations();
});
</script>

<style scoped>
.recommend-view {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

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

.preference-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.preference-item {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.preference-label {
  font-size: 14px;
  color: var(--text-secondary);
  font-weight: 500;
}

.preference-options {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.option-tag {
  padding: 8px 16px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.option-tag:hover {
  background: var(--bg-hover);
}

.option-tag.active {
  background: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.recommend-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.recommend-card {
  background: var(--bg-card);
  border-radius: 14px;
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: all 0.3s;
  position: relative;
}

.recommend-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.recommend-cover {
  height: 160px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.recommend-match {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 4px 10px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 500;
}

.recommend-info {
  padding: 16px;
}

.recommend-title {
  font-weight: 500;
  margin-bottom: 6px;
}

.recommend-type {
  display: inline-block;
  padding: 2px 8px;
  background: var(--bg-secondary);
  border-radius: 6px;
  font-size: 11px;
  color: var(--text-muted);
  margin-bottom: 8px;
}

.recommend-desc {
  font-size: 13px;
  color: var(--text-muted);
  margin-bottom: 12px;
}

.recommend-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.recommend-rate {
  display: flex;
  gap: 8px;
  color: var(--text-muted);
  cursor: pointer;
  font-size: 18px;
}

.recommend-rate span:hover {
  transform: scale(1.1);
}

.recommend-play {
  color: var(--primary-color);
  font-size: 13px;
  cursor: pointer;
  font-weight: 500;
}

.btn {
  padding: 10px 20px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.btn-primary {
  background: var(--primary-color);
  color: white;
}

.btn-primary:hover {
  background: var(--primary-dark);
}

.btn-sm {
  padding: 8px 16px;
  font-size: 13px;
}

/* Toast */
.toast {
  position: fixed;
  bottom: 32px;
  right: 32px;
  padding: 14px 24px;
  background: var(--bg-card);
  border-radius: 12px;
  box-shadow: var(--shadow-lg);
  z-index: 1001;
}

.toast.success {
  border-left: 3px solid var(--success-text);
}
</style>
