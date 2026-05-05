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
              :key="type.value"
              :class="['option-tag', { active: preferences.types.includes(type.value) }]"
              @click="togglePreference('types', type.value)"
            >
              {{ type.label }}
            </label>
          </div>
        </div>
        <div class="preference-item">
          <label class="preference-label">分类偏好</label>
          <div class="preference-options">
            <label
              v-for="category in allCategories"
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

    <!-- Recommend List - Grouped by Type -->
    <div class="recommend-sections">
      <div v-for="(items, type) in groupedRecommendations" :key="type" class="recommend-section">
        <div class="section-header">
          <h4 class="section-title">
            <Icon :name="typeIconMap[type]" :size="20" />
            {{ typeLabelMap[type] }}推荐
          </h4>
          <span class="section-count">{{ items.length }} 个内容</span>
        </div>
        <div class="recommend-grid">
          <div class="recommend-card" v-for="item in items" :key="item.id">
            <div class="recommend-cover" :class="typeCoverClassMap[type]">
              <Icon :name="typeIconMap[type]" :size="48" />
            </div>
            <div class="recommend-match">{{ item.matchRate }}% 匹配</div>
            <div class="recommend-info">
              <div class="recommend-title">{{ item.title }}</div>
              <div class="recommend-meta">
                <span v-if="item.genre" class="recommend-genre">{{ item.genre }}</span>
                <span class="recommend-type">{{ typeLabelMap[item.type] }}</span>
              </div>
              <div class="recommend-desc">{{ item.description }}</div>
              <div class="recommend-reason">{{ item.reason }}</div>
              <div class="recommend-actions">
                <div class="recommend-rate">
                  <span @click="rateItem(item, 'like')" :class="{ active: item.isLiked }">
                    <Icon name="like" :size="18" />
                  </span>
                  <span @click="rateItem(item, 'dislike')" :class="{ active: item.isDisliked }">
                    <Icon name="dislike" :size="18" />
                  </span>
                </div>
                <span class="recommend-play" @click="playItem(item)">播放</span>
              </div>
            </div>
          </div>
        </div>
        <div v-if="items.length === 0" class="empty-state">
          <Icon name="empty" :size="48" />
          <p>暂无该类型的推荐内容</p>
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
import { ref, computed, onMounted } from 'vue';
import Icon from '@/components/Icon.vue';
import { getRecommendations, recordClick, recordLike, recordDislike } from '@/api/recommend';
import { getPreferences, setPreference, type UserPreference } from '@/api/preferences';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();

const typeLabelMap: Record<string, string> = {
  MOVIE: '电影',
  MUSIC: '音乐',
  GAME: '游戏',
};

const typeIconMap: Record<string, string> = {
  MOVIE: 'content-movie',
  MUSIC: 'content-music',
  GAME: 'content-game',
};

const typeCoverClassMap: Record<string, string> = {
  MOVIE: 'cover-movie',
  MUSIC: 'cover-music',
  GAME: 'cover-game',
};

const contentTypes = [
  { value: 'MOVIE', label: '电影' },
  { value: 'MUSIC', label: '音乐' },
  { value: 'GAME', label: '游戏' },
];

const allCategories = ['科幻', '动作', '剧情', '喜剧', '动画', '恐怖', '爱情', '流行', '摇滚', '民谣', '电子', '古典', '爵士', 'RPG', 'MOBA', '射击', '沙盒', '模拟'];

const preferences = ref({
  types: [] as string[],
  categories: [] as string[],
});

const recommendations = ref<any[]>([]);
const loading = ref(false);
const groupedData = ref<Record<string, any[]>>({
  MOVIE: [],
  MUSIC: [],
  GAME: [],
});

const groupedRecommendations = computed(() => {
  return groupedData.value;
});

const toast = ref({ show: false, type: 'success', message: '' });

const loadPreferences = async () => {
  try {
    const currentUserId = userStore.userInfo?.id || 1;
    const result = await getPreferences(currentUserId);
    if (result && result.length > 0) {
      const types: string[] = [];
      const categories: string[] = [];

      result.forEach((pref: UserPreference) => {
        if (['MOVIE', 'MUSIC', 'GAME'].includes(pref.contentType || '')) {
          types.push(pref.contentType!);
        } else if (pref.contentType) {
          if (!categories.includes(pref.contentType)) {
            categories.push(pref.contentType);
          }
        }
      });

      preferences.value.types = types.length > 0 ? types : ['MOVIE', 'MUSIC'];
      preferences.value.categories = categories.length > 0 ? categories : ['科幻', '流行'];
    } else {
      preferences.value.types = ['MOVIE', 'MUSIC'];
      preferences.value.categories = ['科幻', '流行'];
    }
  } catch (error) {
    console.error('加载偏好失败:', error);
    preferences.value.types = ['MOVIE', 'MUSIC'];
    preferences.value.categories = ['科幻', '流行'];
  }
};

const loadRecommendations = async () => {
  try {
    loading.value = true;
    const currentUserId = userStore.userInfo?.id || 1;
    const result: any = await getRecommendations({ userId: currentUserId, page: 1, size: 20 });
    
    // 处理分组响应格式
    groupedData.value = {
      MOVIE: [],
      MUSIC: [],
      GAME: [],
    };
    
    if (result.groups) {
      Object.entries(result.groups).forEach(([type, group]: [string, any]) => {
        if (group.items) {
          groupedData.value[type] = group.items.map((item: any) => ({
            id: item.id,
            contentId: item.contentId,
            title: item.contentTitle,
            type: item.contentType,
            genre: item.contentGenre,
            description: item.description || '精彩内容',
            reason: item.reason || '猜你喜欢',
            matchRate: item.score || Math.floor(Math.random() * 20 + 70),
            isClicked: item.isClicked || 0,
            isLiked: item.isLiked || 0,
            isDisliked: item.isDisliked || 0,
          }));
        }
      });
    }
  } catch (error) {
    console.error('加载推荐失败:', error);
  } finally {
    loading.value = false;
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

    for (const type of preferences.value.types) {
      await setPreference(currentUserId, {
        contentType: type,
        preferenceScore: 10,
      });
    }

    for (const category of preferences.value.categories) {
      await setPreference(currentUserId, {
        contentType: category,
        preferenceScore: 8,
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
      item.isLiked = 1;
      item.isDisliked = 0;
      showToast('success', '已标记喜欢');
    } else {
      await recordDislike(currentUserId, contentId);
      item.isLiked = 0;
      item.isDisliked = 1;
      showToast('success', '已标记不喜欢');
    }
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
    item.isClicked = 1;
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

.recommend-sections {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.recommend-section {
  background: var(--bg-card);
  border-radius: 16px;
  padding: 24px;
  box-shadow: var(--shadow-sm);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  color: var(--text-primary);
  margin: 0;
}

.section-count {
  font-size: 13px;
  color: var(--text-muted);
  background: var(--bg-secondary);
  padding: 4px 12px;
  border-radius: 12px;
}

.recommend-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
}

.recommend-card {
  background: var(--bg-secondary);
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
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.cover-movie {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.cover-music {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.cover-game {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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
  font-weight: 600;
  font-size: 15px;
  margin-bottom: 6px;
  color: var(--text-primary);
}

.recommend-meta {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

.recommend-genre {
  display: inline-block;
  padding: 2px 8px;
  background: rgba(90, 93, 67, 0.15);
  color: var(--primary-color);
  border-radius: 6px;
  font-size: 11px;
  font-weight: 500;
}

.recommend-type {
  display: inline-block;
  padding: 2px 8px;
  background: var(--bg-secondary);
  color: var(--text-muted);
  border-radius: 6px;
  font-size: 11px;
}

.recommend-desc {
  font-size: 13px;
  color: var(--text-muted);
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.recommend-reason {
  font-size: 12px;
  color: var(--primary-color);
  margin-bottom: 12px;
}

.recommend-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.recommend-rate {
  display: flex;
  gap: 12px;
  color: var(--text-muted);
  cursor: pointer;
  font-size: 18px;
}

.recommend-rate span {
  transition: all 0.2s;
}

.recommend-rate span:hover {
  transform: scale(1.1);
}

.recommend-rate span.active {
  color: var(--primary-color);
}

.recommend-play {
  color: var(--primary-color);
  font-size: 13px;
  cursor: pointer;
  font-weight: 500;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px;
  color: var(--text-muted);
}

.empty-state p {
  margin-top: 12px;
  font-size: 14px;
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
