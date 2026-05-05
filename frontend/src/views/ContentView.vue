<template>
  <div class="content-view">
    <div class="table-container">
      <div class="table-header">
        <h3 class="table-title">内容管理</h3>
        <div class="table-tools">
          <div class="table-search">
            <Icon name="search" :size="16" />
            <input type="text" v-model="searchQuery" placeholder="搜索内容..." />
          </div>
          <select v-model="filterType" class="filter-select" @change="loadContents">
            <option value="">全部类型</option>
            <option value="MOVIE">电影</option>
            <option value="GAME">电视剧</option>
            <option value="MUSIC">音乐</option>
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
            <th>时长</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="content in filteredContents" :key="content.id">
            <td>
              <div class="cover-small">
                <Icon :name="content.cover" :size="24" />
              </div>
            </td>
            <td>
              <div class="content-title">{{ content.title }}</div>
              <div class="content-desc">{{ content.description }}</div>
            </td>
            <td><span class="tag tag-blue">{{ content.typeText }}</span></td>
            <td>{{ content.category }}</td>
            <td>{{ content.duration }}</td>
            <td>
              <span :class="['tag', content.status ? 'tag-green' : 'tag-red']">
                {{ content.status ? '已发布' : '未发布' }}
              </span>
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
          <div class="form-group">
            <label class="form-label">描述</label>
            <input v-model="formData.description" type="text" class="form-input" />
          </div>
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">类型</label>
              <select v-model="formData.type" class="form-select">
                <option value="电影">电影</option>
                <option value="电视剧">电视剧</option>
                <option value="音乐">音乐</option>
                <option value="课程">课程</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">分类</label>
              <select v-model="formData.category" class="form-select">
                <option value="科幻">科幻</option>
                <option value="动作">动作</option>
                <option value="剧情">剧情</option>
                <option value="喜剧">喜剧</option>
                <option value="音乐">音乐</option>
                <option value="教育">教育</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">时长</label>
              <input v-model="formData.duration" type="text" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">状态</label>
              <select v-model="formData.status" class="form-select">
                <option :value="true">已发布</option>
                <option :value="false">未发布</option>
              </select>
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

// 内容类型映射
const typeTextMap: Record<string, string> = {
  MOVIE: '电影',
  MUSIC: '音乐',
  GAME: '游戏',
};

const typeValueMap: Record<string, string> = {
  '电影': 'MOVIE',
  '电视剧': 'GAME',
  '音乐': 'MUSIC',
  '课程': 'GAME',
};

const coverIconMap: Record<string, string> = {
  MOVIE: 'content-movie',
  MUSIC: 'content-music',
  GAME: 'content-game',
};

const searchQuery = ref('');
const filterType = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const showAddModal = ref(false);
const isEditing = ref(false);
const loading = ref(false);

const formData = ref<any>({
  id: null,
  title: '',
  description: '',
  type: 'MOVIE',
  rating: 4.5,
  cover: '',
});

const contents = ref<ContentInfo[]>([]);
const total = ref(0);

// 加载内容列表
const loadContents = async () => {
  try {
    loading.value = true;
    const result = await getContentList({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchQuery.value,
      type: filterType.value ? typeValueMap[filterType.value] : undefined,
    });
    contents.value = result.records;
    total.value = result.total;
  } catch (error) {
    console.error('加载内容列表失败:', error);
  } finally {
    loading.value = false;
  }
};

const filteredContents = computed(() => {
  return contents.value.map((content) => ({
    ...content,
    typeText: typeTextMap[content.type] || content.type,
    cover: content.cover || coverIconMap[content.type] || 'content-movie',
    category: content.type === 'MOVIE' ? '科幻' : content.type === 'MUSIC' ? '音乐' : '其他',
    duration: content.rating ? `${content.rating}分` : '-',
    status: content.rating !== undefined && content.rating > 0,
  }));
});

const totalPages = computed(() => Math.ceil(total.value / pageSize.value));

const toast = ref({ show: false, type: 'success', message: '' });

const editContent = (content: any) => {
  isEditing.value = true;
  formData.value = {
    id: content.id,
    title: content.title,
    description: content.description,
    type: content.type,
    rating: content.rating,
    cover: content.cover,
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
      description: formData.value.description,
      rating: formData.value.rating,
      cover: formData.value.cover,
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
}

.table-title {
  font-size: 18px;
  color: var(--text-primary);
}

.table-tools {
  display: flex;
  gap: 12px;
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
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

tr:hover .cover-small {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(30, 58, 95, 0.3);
}

.content-title {
  font-weight: 500;
  margin-bottom: 4px;
}

.content-desc {
  font-size: 12px;
  color: var(--text-muted);
}

.tag {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.tag-blue {
  background: var(--info-bg);
  color: var(--info-text);
}
.tag-green {
  background: var(--success-bg);
  color: var(--success-text);
}
.tag-red {
  background: var(--error-bg);
  color: var(--error-text);
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

/* Pagination */
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

.pagination-btn:hover {
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

/* Modal */
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
.form-select:focus {
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
