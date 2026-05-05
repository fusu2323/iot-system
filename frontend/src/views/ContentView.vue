<template>
  <div class="content-view">
    <div class="table-container">
      <div class="table-header">
        <h3 class="table-title">内容管理</h3>
        <div class="table-tools">
          <div class="table-search">
            <Icon name="search" :size="16" />
            <input type="text" v-model="searchQuery" placeholder="搜索内容..." @change="loadContents" />
          </div>
          <select v-model="filterType" class="filter-select" @change="loadContents">
            <option value="">全部类型</option>
            <option value="MOVIE">电影</option>
            <option value="MUSIC">音乐</option>
            <option value="GAME">游戏</option>
          </select>
          <select v-model="filterGenre" class="filter-select" @change="loadContents">
            <option value="">全部分类</option>
            <option v-for="genre in availableGenres" :key="genre" :value="genre">{{ genre }}</option>
          </select>
          <button class="btn btn-primary" @click="showAddModal = true">
            <Icon name="add" :size="16" /> 添加内容
          </button>
        </div>
      </div>

      <table class="data-table">
        <thead>
          <tr>
            <th>封面</th>
            <th>标题</th>
            <th>类型</th>
            <th>分类</th>
            <th>评分</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="content in filteredContents" :key="content.id">
            <td>
              <div class="cover-small">
                <Icon :name="content.coverIcon" :size="24" />
              </div>
            </td>
            <td>
              <div class="content-title">{{ content.title }}</div>
              <div class="content-desc">{{ content.description }}</div>
            </td>
            <td><span :class="['tag', content.typeTagClass]">{{ content.typeText }}</span></td>
            <td><span class="genre-tag">{{ content.genre || '-' }}</span></td>
            <td>
              <div class="rating">
                <Icon name="star" :size="14" />
                <span>{{ content.rating?.toFixed(1) || '-' }}</span>
              </div>
            </td>
            <td>
              <span class="action-link" @click="editContent(content)">编辑</span>
              <span class="action-link delete" @click="deleteContentAction(content)">删除</span>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- Pagination -->
      <div class="pagination">
        <button class="pagination-btn" :disabled="currentPage === 1" @click="handlePageChange(currentPage - 1)">上一页</button>
        <button
          v-for="page in totalPages"
          :key="page"
          :class="['pagination-btn', { active: currentPage === page }]"
          @click="handlePageChange(page)"
        >
          {{ page }}
        </button>
        <button class="pagination-btn" :disabled="currentPage === totalPages" @click="handlePageChange(currentPage + 1)">下一页</button>
      </div>
    </div>

    <!-- Add/Edit Modal -->
    <div v-if="showAddModal" class="modal-overlay" @click.self="showAddModal = false">
      <div class="modal">
        <h3 class="modal-title">{{ isEditing ? '编辑内容' : '添加内容' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label class="form-label">标题</label>
            <input v-model="formData.title" type="text" class="form-input" required />
          </div>
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">类型</label>
              <select v-model="formData.type" class="form-select" @change="updateGenreOptions">
                <option value="MOVIE">电影</option>
                <option value="MUSIC">音乐</option>
                <option value="GAME">游戏</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">分类</label>
              <select v-model="formData.genre" class="form-select">
                <option value="">请选择分类</option>
                <option v-for="genre in currentGenreOptions" :key="genre" :value="genre">{{ genre }}</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">描述</label>
            <textarea v-model="formData.description" class="form-textarea" rows="3"></textarea>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">评分</label>
              <input v-model.number="formData.rating" type="number" step="0.1" min="0" max="5" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">封面</label>
              <input v-model="formData.cover" type="text" class="form-input" placeholder="封面URL" />
            </div>
          </div>
          <div class="modal-actions">
            <button type="button" class="btn btn-secondary" @click="showAddModal = false">
              取消
            </button>
            <button type="submit" class="btn btn-primary">
              {{ isEditing ? '保存' : '添加' }}
            </button>
          </div>
        </form>
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
import {
  getContentList,
  createContent,
  updateContent,
  deleteContent,
  type ContentInfo,
} from '@/api/content';

const typeTextMap: Record<string, string> = {
  MOVIE: '电影',
  MUSIC: '音乐',
  GAME: '游戏',
};

const typeTagClassMap: Record<string, string> = {
  MOVIE: 'tag-purple',
  MUSIC: 'tag-pink',
  GAME: 'tag-cyan',
};

const coverIconMap: Record<string, string> = {
  MOVIE: 'content-movie',
  MUSIC: 'content-music',
  GAME: 'content-game',
};

const genreOptions: Record<string, string[]> = {
  MOVIE: ['科幻', '动作', '剧情', '喜剧', '动画', '恐怖', '爱情'],
  MUSIC: ['流行', '摇滚', '民谣', '电子', '古典', '爵士'],
  GAME: ['RPG', '动作', 'MOBA', '射击', '沙盒', '模拟'],
};

const searchQuery = ref('');
const filterType = ref('');
const filterGenre = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const showAddModal = ref(false);
const isEditing = ref(false);
const loading = ref(false);

const formData = ref<any>({
  id: null,
  title: '',
  type: 'MOVIE',
  genre: '',
  description: '',
  rating: 4.0,
  cover: '',
});

const contents = ref<ContentInfo[]>([]);
const total = ref(0);

const availableGenres = computed(() => {
  const genres = new Set<string>();
  contents.value.forEach(c => {
    if (c.genre) {
      genres.add(c.genre);
    }
  });
  return Array.from(genres);
});

const currentGenreOptions = computed(() => {
  return genreOptions[formData.value.type] || [];
});

const filteredContents = computed(() => {
  let filtered = contents.value;
  
  if (filterGenre.value) {
    filtered = filtered.filter(c => c.genre === filterGenre.value);
  }
  
  return filtered.map((content) => ({
    ...content,
    typeText: typeTextMap[content.type] || content.type,
    typeTagClass: typeTagClassMap[content.type] || 'tag-blue',
    coverIcon: content.cover ? undefined : (coverIconMap[content.type] || 'content-movie'),
  }));
});

const totalPages = computed(() => Math.ceil(total.value / pageSize.value));

const toast = ref({ show: false, type: 'success', message: '' });

const loadContents = async () => {
  try {
    loading.value = true;
    const result = await getContentList({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchQuery.value,
      type: filterType.value || undefined,
    });
    contents.value = result.records;
    total.value = result.total;
  } catch (error) {
    console.error('加载内容列表失败:', error);
  } finally {
    loading.value = false;
  }
};

const updateGenreOptions = () => {
  formData.value.genre = '';
};

const editContent = (content: any) => {
  isEditing.value = true;
  formData.value = {
    id: content.id,
    title: content.title,
    type: content.type,
    genre: content.genre || '',
    description: content.description || '',
    rating: content.rating || 4.0,
    cover: content.cover || '',
  };
  showAddModal.value = true;
};

const deleteContentAction = async (content: any) => {
  if (!confirm(`确定要删除内容 "${content.title}" 吗？`)) {
    return;
  }

  try {
    await deleteContent(content.id);
    showToast('success', '内容已删除');
    await loadContents();
  } catch (error: any) {
    console.error('删除内容失败:', error);
    showToast('error', error.response?.data?.message || '删除内容失败');
  }
};

const handleSubmit = async () => {
  try {
    const submitData = {
      title: formData.value.title,
      type: formData.value.type,
      genre: formData.value.genre || undefined,
      description: formData.value.description || undefined,
      rating: formData.value.rating,
      cover: formData.value.cover || undefined,
    };

    if (isEditing.value && formData.value.id) {
      await updateContent(formData.value.id, submitData);
      showToast('success', '内容已更新');
    } else {
      await createContent(submitData);
      showToast('success', '内容已添加');
    }

    showAddModal.value = false;
    await loadContents();
  } catch (error: any) {
    console.error('操作失败:', error);
    showToast('error', error.response?.data?.message || '操作失败');
  }
};

const showToast = (type: string, message: string) => {
  toast.value = { show: true, type, message };
  setTimeout(() => {
    toast.value.show = false;
  }, 3000);
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  loadContents();
};

onMounted(() => {
  loadContents();
});
</script>

<style scoped>
.content-view {
  display: flex;
  flex-direction: column;
}

.table-container {
  background: var(--bg-card);
  border-radius: 16px;
  padding: 24px;
  box-shadow: var(--shadow-sm);
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 12px;
}

.table-title {
  font-size: 18px;
  color: var(--text-primary);
}

.table-tools {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}

.table-search {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 10px;
  width: 240px;
}

.table-search input {
  flex: 1;
  background: none;
  border: none;
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
}

.filter-select {
  padding: 8px 16px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 10px;
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th {
  text-align: left;
  padding: 14px 16px;
  font-size: 13px;
  color: var(--text-muted);
  font-weight: 500;
  border-bottom: 1px solid var(--border-color);
}

.data-table td {
  padding: 16px;
  font-size: 14px;
  border-bottom: 1px solid var(--border-light);
}

.cover-small {
  width: 50px;
  height: 50px;
  background: linear-gradient(145deg, #1e3a5f 0%, #3d5a80 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 2px 8px rgba(30, 58, 95, 0.2);
}

.content-title {
  font-weight: 500;
  margin-bottom: 4px;
}

.content-desc {
  font-size: 12px;
  color: var(--text-muted);
}

.rating {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #f5a623;
  font-weight: 500;
}

.tag {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.tag-purple {
  background: rgba(155, 89, 182, 0.15);
  color: #9b59b6;
}

.tag-pink {
  background: rgba(233, 30, 99, 0.15);
  color: #e91e63;
}

.tag-cyan {
  background: rgba(0, 188, 212, 0.15);
  color: #00bcd4;
}

.genre-tag {
  display: inline-block;
  padding: 3px 10px;
  background: var(--bg-secondary);
  border-radius: 8px;
  font-size: 12px;
  color: var(--text-secondary);
}

.action-link {
  color: var(--primary-color);
  cursor: pointer;
  margin-right: 12px;
  font-size: 13px;
}

.action-link:hover {
  text-decoration: underline;
}

.action-link.delete {
  color: #e74c3c;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-textarea {
  width: 100%;
  padding: 10px 14px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 10px;
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
  resize: vertical;
  font-family: inherit;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 8px;
  margin-top: 20px;
}

.pagination-btn {
  padding: 6px 12px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  cursor: pointer;
  font-size: 13px;
  transition: all 0.2s;
}

.pagination-btn:hover:not(:disabled) {
  background: var(--bg-hover);
}

.pagination-btn.active {
  background: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: var(--bg-card);
  border-radius: 20px;
  padding: 32px;
  width: 560px;
  max-width: 90%;
  box-shadow: var(--shadow-lg);
  max-height: 90vh;
  overflow-y: auto;
}

.modal-title {
  font-size: 20px;
  margin-bottom: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: var(--text-secondary);
}

.form-input,
.form-select {
  width: 100%;
  padding: 10px 14px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 10px;
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
}

.form-input:focus,
.form-select:focus,
.form-textarea:focus {
  border-color: var(--primary-color);
}

.modal-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 24px;
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

.btn-secondary {
  background: var(--bg-secondary);
  color: var(--text-primary);
  border: 1px solid var(--border-color);
}

.btn-secondary:hover {
  background: var(--bg-hover);
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
