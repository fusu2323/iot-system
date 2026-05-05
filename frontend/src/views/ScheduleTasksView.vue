<template>
  <div class="schedule-tasks-view">
    <div class="page-header">
      <h2 class="page-title">定时任务管理</h2>
      <button class="btn-primary" @click="showAddModal = true">
        创建任务
      </button>
    </div>

    <!-- Task List -->
    <div class="task-list">
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="tasks.length === 0" class="empty-state">
        暂无定时任务
      </div>
      <div v-else class="task-cards">
        <div
          v-for="task in tasks"
          :key="task.id"
          :class="['task-card', { disabled: task.isEnabled === 0 }]"
        >
          <div class="task-info">
            <h3 class="task-name">{{ task.name }}</h3>
            <div class="task-meta">
              <span class="meta-item">
                <Icon name="scene" :size="14" />
                {{ task.sceneName || '未知场景' }}
              </span>
              <span class="meta-item">
                <Icon name="clock" :size="14" />
                {{ task.scheduleTypeDesc || '未知类型' }}
              </span>
              <span class="meta-item">
                <Icon name="calendar" :size="14" />
                {{ task.cronExpression }}
              </span>
              <span v-if="task.nextFireTime" class="meta-item next-fire">
                下次: {{ formatDateTime(task.nextFireTime) }}
              </span>
            </div>
          </div>
          <div class="task-actions">
            <button
              :class="['btn-toggle', { active: task.isEnabled === 1 }]"
              @click="toggleTask(task)"
            >
              {{ task.isEnabled === 1 ? '已启用' : '已禁用' }}
            </button>
            <button class="btn-icon" @click="editTask(task)">
              <Icon name="edit" :size="16" />
            </button>
            <button class="btn-icon btn-delete" @click="deleteTaskAction(task)">
              <Icon name="delete" :size="16" />
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Pagination -->
    <div v-if="total > 0" class="pagination">
      <button
        :disabled="page <= 1"
        @click="loadTasks(page - 1)"
        class="page-btn"
      >
        上一页
      </button>
      <span class="page-info">{{ page }} / {{ totalPages }} 页</span>
      <button
        :disabled="page >= totalPages"
        @click="loadTasks(page + 1)"
        class="page-btn"
      >
        下一页
      </button>
    </div>

    <!-- Create/Edit Modal -->
    <div v-if="showAddModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <h3 class="modal-title">{{ isEditing ? '编辑任务' : '创建任务' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label class="form-label">任务名称</label>
            <input
              v-model="formData.name"
              type="text"
              class="form-input"
              placeholder="例如：早起提醒"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">关联场景</label>
            <select v-model="formData.sceneId" class="form-select" required>
              <option value="">请选择场景</option>
              <option
                v-for="scene in availableScenes"
                :key="scene.id"
                :value="scene.id"
              >
                {{ scene.name }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">调度类型</label>
            <select
              v-model="formData.scheduleType"
              class="form-select"
              required
              @change="onScheduleTypeChange"
            >
              <option :value="0">每日</option>
              <option :value="1">工作日</option>
              <option :value="2">周末</option>
              <option :value="3">自定义CRON</option>
            </select>
          </div>

          <!-- Dynamic time inputs for DAILY/WORKDAY/WEEKEND -->
          <div v-if="formData.scheduleType < 3" class="form-group">
            <label class="form-label">执行时间</label>
            <div class="time-picker">
              <select v-model="formData.hour" class="form-select time-select">
                <option v-for="h in 24" :key="h-1" :value="h-1">
                  {{ String(h-1).padStart(2, '0') }} 时
                </option>
              </select>
              <span class="time-separator">:</span>
              <select v-model="formData.minute" class="form-select time-select">
                <option v-for="m in 60" :key="m-1" :value="m-1">
                  {{ String(m-1).padStart(2, '0') }} 分
                </option>
              </select>
            </div>
          </div>

          <!-- Custom cron input for CUSTOM -->
          <div v-if="formData.scheduleType === 3" class="form-group">
            <label class="form-label">CRON 表达式</label>
            <input
              v-model="formData.cronExpression"
              type="text"
              class="form-input"
              placeholder="0 30 8 * * ?"
            />
            <p class="form-hint">标准5位CRON表达式，例：0 30 8 * * ? 表示每天8:30</p>
          </div>

          <div class="modal-actions">
            <button type="button" class="btn btn-secondary" @click="closeModal">
              取消
            </button>
            <button type="submit" class="btn btn-primary">
              {{ isEditing ? '保存' : '创建' }}
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
  getScheduledTaskList,
  createScheduledTask,
  updateScheduledTask,
  deleteScheduledTask,
  toggleScheduledTask,
  type ScheduledTaskInfo,
} from '@/api/scheduledTask';
import { getSceneList } from '@/api/scene';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();

const showAddModal = ref(false);
const isEditing = ref(false);
const loading = ref(false);
const editingId = ref<number | null>(null);

const page = ref(1);
const size = ref(10);
const total = ref(0);
const totalPages = computed(() => Math.ceil(total.value / size.value));

const formData = ref<any>({
  name: '',
  sceneId: '',
  scheduleType: 0,
  hour: 8,
  minute: 0,
  cronExpression: '',
});

const tasks = ref<ScheduledTaskInfo[]>([]);
const availableScenes = ref<any[]>([]);

const toast = ref({ show: false, type: 'success', message: '' });

// Load tasks
const loadTasks = async (pageNum: number = 1) => {
  try {
    loading.value = true;
    page.value = pageNum;
    const result = await getScheduledTaskList({
      page: pageNum,
      size: size.value,
    });
    tasks.value = result.records;
    total.value = result.total;
  } catch (error) {
    console.error('加载任务列表失败:', error);
    showToast('error', '加载任务列表失败');
  } finally {
    loading.value = false;
  }
};

// Load available scenes for dropdown
const loadScenes = async () => {
  try {
    const result = await getSceneList({ page: 1, size: 100 });
    availableScenes.value = result.records;
  } catch (error) {
    console.error('加载场景列表失败:', error);
  }
};

// Toggle task enable/disable
const toggleTask = async (task: ScheduledTaskInfo) => {
  try {
    await toggleScheduledTask(task.id);
    await loadTasks(page.value);
    showToast('success', `任务已${task.isEnabled === 1 ? '禁用' : '启用'}`);
  } catch (error: any) {
    console.error('切换状态失败:', error);
    showToast('error', error.response?.data?.message || '切换状态失败');
  }
};

// Edit task
const editTask = (task: ScheduledTaskInfo) => {
  isEditing.value = true;
  editingId.value = task.id;

  // Parse timeConfig JSON
  let hour = 8;
  let minute = 0;
  let cronExpression = '';
  try {
    const config = JSON.parse(task.timeConfig || '{}');
    hour = config.hour ?? 8;
    minute = config.minute ?? 0;
    cronExpression = config.cron ?? '';
  } catch (e) {}

  formData.value = {
    name: task.name,
    sceneId: task.sceneId,
    scheduleType: task.scheduleType,
    hour,
    minute,
    cronExpression,
  };
  showAddModal.value = true;
};

// Delete task
const deleteTaskAction = async (task: ScheduledTaskInfo) => {
  if (!confirm(`确定要删除任务 "${task.name}" 吗？`)) {
    return;
  }

  try {
    await deleteScheduledTask(task.id);
    showToast('success', '任务已删除');
    await loadTasks(page.value);
  } catch (error: any) {
    console.error('删除任务失败:', error);
    showToast('error', error.response?.data?.message || '删除任务失败');
  }
};

// Handle schedule type change
const onScheduleTypeChange = () => {
  // Reset time values when type changes
  formData.value.hour = 8;
  formData.value.minute = 0;
  formData.value.cronExpression = '';
};

// Close modal
const closeModal = () => {
  showAddModal.value = false;
  isEditing.value = false;
  editingId.value = null;
  formData.value = {
    name: '',
    sceneId: '',
    scheduleType: 0,
    hour: 8,
    minute: 0,
    cronExpression: '',
  };
};

// Submit form
const handleSubmit = async () => {
  try {
    const currentUserId = userStore.userInfo?.id || 1;

    // Build timeConfig JSON based on schedule type
    let timeConfig: string;
    if (formData.value.scheduleType < 3) {
      timeConfig = JSON.stringify({
        hour: formData.value.hour,
        minute: formData.value.minute,
      });
    } else {
      timeConfig = JSON.stringify({
        cron: formData.value.cronExpression,
      });
    }

    if (isEditing.value && editingId.value !== null) {
      await updateScheduledTask(editingId.value, {
        name: formData.value.name,
        sceneId: Number(formData.value.sceneId),
        scheduleType: formData.value.scheduleType,
        timeConfig,
      });
      showToast('success', '任务已更新');
    } else {
      await createScheduledTask(currentUserId, {
        name: formData.value.name,
        sceneId: Number(formData.value.sceneId),
        scheduleType: formData.value.scheduleType,
        timeConfig,
      });
      showToast('success', '任务已创建');
    }

    closeModal();
    await loadTasks(page.value);
  } catch (error: any) {
    console.error('操作失败:', error);
    showToast('error', error.response?.data?.message || '操作失败');
  }
};

// Format datetime
const formatDateTime = (dateStr: string) => {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`;
};

// Toast helper
const showToast = (type: string, message: string) => {
  toast.value = { show: true, type, message };
  setTimeout(() => {
    toast.value.show = false;
  }, 3000);
};

onMounted(() => {
  loadTasks();
  loadScenes();
});
</script>

<style scoped>
.schedule-tasks-view {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  font-size: 20px;
  color: var(--text-primary);
  margin: 0;
}

.btn-primary {
  padding: 10px 20px;
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-primary:hover {
  background: var(--primary-dark);
}

.task-list {
  min-height: 200px;
}

.loading,
.empty-state {
  text-align: center;
  color: var(--text-muted);
  padding: 40px;
}

.task-cards {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.task-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: var(--bg-card);
  border-radius: 12px;
  border: 2px solid transparent;
  transition: all 0.2s;
}

.task-card:hover {
  box-shadow: var(--shadow-sm);
}

.task-card.disabled {
  opacity: 0.6;
}

.task-info {
  flex: 1;
}

.task-name {
  font-size: 16px;
  color: var(--text-primary);
  margin: 0 0 8px 0;
}

.task-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--text-muted);
}

.next-fire {
  color: var(--primary-color);
  font-weight: 500;
}

.task-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}

.btn-toggle {
  padding: 6px 16px;
  border-radius: 8px;
  font-size: 13px;
  cursor: pointer;
  border: 1px solid var(--border-color);
  background: var(--bg-secondary);
  color: var(--text-secondary);
  transition: all 0.2s;
}

.btn-toggle.active {
  background: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.btn-icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  border: 1px solid var(--border-color);
  background: var(--bg-card);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.btn-icon:hover {
  background: var(--bg-hover);
}

.btn-icon.btn-delete:hover {
  background: rgba(255, 59, 48, 0.1);
  color: #ff3b30;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  padding: 20px;
}

.page-btn {
  padding: 8px 16px;
  border-radius: 8px;
  border: 1px solid var(--border-color);
  background: var(--bg-card);
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-btn:not(:disabled):hover {
  background: var(--bg-hover);
}

.page-info {
  font-size: 14px;
  color: var(--text-muted);
}

/* Modal styles */
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
  width: 480px;
  max-width: 90%;
  box-shadow: var(--shadow-lg);
  max-height: 80vh;
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
.form-select:focus {
  border-color: var(--primary-color);
}

.form-hint {
  margin-top: 4px;
  font-size: 12px;
  color: var(--text-muted);
}

.time-picker {
  display: flex;
  align-items: center;
  gap: 8px;
}

.time-select {
  width: auto;
  min-width: 100px;
}

.time-separator {
  font-size: 18px;
  color: var(--text-muted);
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

.toast.error {
  border-left: 3px solid #ff3b30;
}
</style>