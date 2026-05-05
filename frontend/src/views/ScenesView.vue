<template>
  <div class="scenes-view">
    <div class="scenes-grid">
      <div
        v-for="scene in scenes"
        :key="scene.id"
        :class="['scene-card', { active: scene.isEnabled === 1 }]"
      >
        <div class="scene-cover">
          <Icon :name="scene.icon || 'scene-home'" :size="32" />
        </div>
        <div class="scene-info">
          <h3 class="scene-name">{{ scene.name }}</h3>
          <p class="scene-desc">{{ scene.description }}</p>
          <div class="scene-devices" v-if="scene.devices && scene.devices.length > 0">
            <span v-for="device in scene.devices" :key="device.deviceId" class="device-tag">
              {{ device.deviceName }}
            </span>
          </div>
        </div>
        <div class="scene-actions">
          <button class="btn-toggle" :class="{ active: scene.isEnabled === 1 }" @click="toggleScene(scene)">
            {{ scene.isEnabled === 1 ? '已启用' : '启用' }}
          </button>
          <button class="btn-edit" @click="editScene(scene)">
            <Icon name="edit" :size="16" />
          </button>
          <button class="btn-delete" @click="deleteSceneAction(scene)">
            <Icon name="delete" :size="16" />
          </button>
        </div>
      </div>

      <!-- Add Scene Card -->
      <div class="scene-card add-card" @click="showAddModal = true">
        <div class="add-icon">
          <Icon name="add" :size="32" />
        </div>
        <div class="add-text">添加场景</div>
      </div>
    </div>

    <!-- Add/Edit Modal -->
    <div v-if="showAddModal" class="modal-overlay" @click.self="showAddModal = false">
      <div class="modal">
        <h3 class="modal-title">{{ isEditing ? '编辑场景' : '添加场景' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label class="form-label">场景名称</label>
            <input v-model="formData.name" type="text" class="form-input" required />
          </div>
          <div class="form-group">
            <label class="form-label">场景描述</label>
            <input v-model="formData.description" type="text" class="form-input" />
          </div>
          <div class="form-group">
            <label class="form-label">场景图标</label>
            <select v-model="formData.icon" class="form-select">
              <option value="scene-cinema">观影</option>
              <option value="scene-home">回家</option>
              <option value="scene-sleep">睡眠</option>
              <option value="scene-leave">离家</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-label">关联设备</label>
            <div class="device-select">
              <label v-for="device in allDevices" :key="device" class="device-checkbox">
                <input
                  type="checkbox"
                  :value="device"
                  v-model="formData.devices"
                />
                <span>{{ device }}</span>
              </label>
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
import { ref, onMounted } from 'vue';
import Icon from '@/components/Icon.vue';
import {
  getSceneList,
  createScene,
  updateScene,
  deleteScene,
  toggleScene as apiToggleScene,
  triggerScene as apiTriggerScene,
  type SceneInfo,
} from '@/api/scene';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();

const showAddModal = ref(false);
const isEditing = ref(false);
const loading = ref(false);

const formData = ref<any>({
  id: null,
  name: '',
  description: '',
  icon: 'scene-home',
  devices: [],
});

const scenes = ref<SceneInfo[]>([]);

const allDevices = ['灯光', '空调', '电视', '音响', '窗帘', '门锁', '摄像头', '传感器'];

const toast = ref({ show: false, type: 'success', message: '' });

// 加载场景列表
const loadScenes = async () => {
  try {
    loading.value = true;
    const result = await getSceneList({ page: 1, size: 50 });
    scenes.value = result.records;
  } catch (error) {
    console.error('加载场景列表失败:', error);
    showToast('error', '加载场景列表失败');
  } finally {
    loading.value = false;
  }
};

const toggleScene = async (scene: any) => {
  try {
    await apiToggleScene(scene.id);
    scene.isEnabled = scene.isEnabled === 1 ? 0 : 1;
    showToast('success', `${scene.name}已${scene.isEnabled === 1 ? '启用' : '关闭'}`);
  } catch (error: any) {
    console.error('切换场景失败:', error);
    showToast('error', error.response?.data?.message || '切换场景失败');
  }
};

const triggerSceneAction = async (scene: any) => {
  try {
    await apiTriggerScene(scene.id);
    showToast('success', `场景 "${scene.name}" 已触发`);
  } catch (error: any) {
    console.error('触发场景失败:', error);
    showToast('error', error.response?.data?.message || '触发场景失败');
  }
};

const editScene = (scene: any) => {
  isEditing.value = true;
  formData.value = {
    id: scene.id,
    name: scene.name,
    description: scene.description,
    icon: scene.icon || 'scene-home',
    devices: scene.devices?.map((d: any) => d.deviceName) || [],
  };
  showAddModal.value = true;
};

const deleteSceneAction = async (scene: any) => {
  if (!confirm(`确定要删除场景 "${scene.name}" 吗？`)) {
    return;
  }

  try {
    await deleteScene(scene.id);
    showToast('success', '场景已删除');
    await loadScenes();
  } catch (error: any) {
    console.error('删除场景失败:', error);
    showToast('error', error.response?.data?.message || '删除场景失败');
  }
};

const handleSubmit = async () => {
  try {
    const currentUserId = userStore.userInfo?.id || 1;
    const submitData = {
      name: formData.value.name,
      description: formData.value.description,
      icon: formData.value.icon,
    };

    if (isEditing.value && formData.value.id) {
      await updateScene(formData.value.id, submitData);
      showToast('success', '场景已更新');
    } else {
      await createScene(currentUserId, submitData);
      showToast('success', '场景已添加');
    }

    showAddModal.value = false;
    await loadScenes();
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

onMounted(() => {
  loadScenes();
});
</script>

<style scoped>
.scenes-view {
  display: flex;
  flex-direction: column;
}

.scenes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.scene-card {
  background: var(--bg-card);
  border-radius: 16px;
  padding: 24px;
  box-shadow: var(--shadow-sm);
  transition: all 0.3s;
  border: 2px solid transparent;
}

.scene-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.scene-card.active {
  border-color: var(--primary-color);
  background: rgba(90, 93, 67, 0.05);
}

.scene-cover {
  width: 60px;
  height: 60px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(145deg, #1e3a5f 0%, #3d5a80 100%);
  color: white;
  margin-bottom: 16px;
  box-shadow: 0 4px 16px rgba(30, 58, 95, 0.3);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.scene-card:hover .scene-cover {
  transform: scale(1.05);
  box-shadow: 0 6px 20px rgba(30, 58, 95, 0.4);
}

.scene-card.active .scene-cover {
  background: linear-gradient(145deg, #2d5a3d 0%, #3d7a52 100%);
  box-shadow: 0 4px 16px rgba(45, 90, 61, 0.3);
}

.scene-info {
  margin-bottom: 16px;
}

.scene-name {
  font-size: 18px;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.scene-desc {
  font-size: 14px;
  color: var(--text-muted);
  margin-bottom: 12px;
}

.scene-devices {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.device-tag {
  padding: 4px 10px;
  background: var(--bg-secondary);
  border-radius: 8px;
  font-size: 12px;
  color: var(--text-secondary);
}

.scene-actions {
  display: flex;
  gap: 8px;
}

.btn-toggle {
  flex: 1;
  padding: 8px 16px;
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-toggle:not(.active) {
  background: var(--bg-secondary);
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
}

.btn-edit,
.btn-delete {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  border: 1px solid var(--border-color);
  background: var(--bg-card);
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
}

.btn-edit:hover,
.btn-delete:hover {
  background: var(--bg-hover);
}

/* Add Card */
.add-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  cursor: pointer;
  border: 2px dashed var(--border-color);
  background: transparent;
}

.add-card:hover {
  border-color: var(--primary-color);
  background: rgba(90, 93, 67, 0.05);
}

.add-icon {
  font-size: 48px;
  color: var(--text-muted);
  margin-bottom: 8px;
}

.add-text {
  font-size: 14px;
  color: var(--text-muted);
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

.device-select {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
}

.device-checkbox {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: var(--bg-secondary);
  border-radius: 8px;
  cursor: pointer;
  font-size: 13px;
}

.device-checkbox input {
  width: 16px;
  height: 16px;
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
