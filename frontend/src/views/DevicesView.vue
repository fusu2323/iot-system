<template>
  <div class="devices-view">
    <div class="table-container">
      <div class="table-header">
        <h3 class="table-title">设备列表</h3>
        <div class="table-tools">
          <div class="table-search">
            <Icon name="search" :size="16" />
            <input type="text" v-model="searchQuery" placeholder="搜索设备..." @change="loadDevices" />
          </div>
          <button class="btn btn-primary" @click="showAddModal = true">
            <Icon name="add" :size="16" /> 添加设备
          </button>
        </div>
      </div>

      <table class="data-table">
        <thead>
          <tr>
            <th>设备名称</th>
            <th>设备类型</th>
            <th>位置</th>
            <th>状态</th>
            <th>在线状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="device in filteredDevices" :key="device.id">
            <td>
              <div class="device-cell">
                <span class="device-icon-small">
                  <Icon :name="device.icon" :size="18" />
                </span>
                {{ device.name }}
              </div>
            </td>
            <td><span class="tag tag-blue">{{ device.typeText }}</span></td>
            <td>{{ device.room }}</td>
            <td>
              <span :class="['status-dot', device.status ? 'online' : 'offline']"></span>
              {{ device.status ? '已开启' : '已关闭' }}
            </td>
            <td>
              <span :class="['tag', device.online ? 'tag-green' : 'tag-red']">
                {{ device.online ? '在线' : '离线' }}
              </span>
            </td>
            <td>
              <span class="action-link" @click="editDevice(device)">编辑</span>
              <span class="action-link delete" @click="deleteDeviceAction(device)">删除</span>
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
        <h3 class="modal-title">{{ isEditing ? '编辑设备' : '添加设备' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label class="form-label">设备名称</label>
            <input v-model="formData.name" type="text" class="form-input" required />
          </div>
          <div class="form-group">
            <label class="form-label">设备类型</label>
            <select v-model="formData.type" class="form-select">
              <option value="LIGHT">灯光</option>
              <option value="AIR_CONDITIONER">空调</option>
              <option value="TV">电视</option>
              <option value="SPEAKER">音响</option>
              <option value="CURTAIN">窗帘</option>
              <option value="DOOR_LOCK">门锁</option>
              <option value="CAMERA">摄像头</option>
              <option value="SENSOR">传感器</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-label">位置</label>
            <select v-model="formData.room" class="form-select">
              <option value="客厅">客厅</option>
              <option value="卧室">卧室</option>
              <option value="厨房">厨房</option>
              <option value="卫生间">卫生间</option>
              <option value="阳台">阳台</option>
              <option value="书房">书房</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-label">设备状态</label>
            <select v-model="formData.status" class="form-select">
              <option :value="1">启用</option>
              <option :value="0">禁用</option>
            </select>
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
  getDeviceList,
  createDevice,
  updateDevice,
  deleteDevice,
  type DeviceInfo,
} from '@/api/device';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();

// 设备类型与图标映射
const deviceIconMap: Record<string, string> = {
  TV: 'device-tv',
  SPEAKER: 'device-speaker',
  LIGHT: 'device-light',
  AIR_CONDITIONER: 'device-air',
  CURTAIN: 'device-curtain',
  DOOR_LOCK: 'device-lock',
  CAMERA: 'device-camera',
  SENSOR: 'device-sensor',
  OTHER: 'device-sensor',
};

// 中文类型映射
const typeTextMap: Record<string, string> = {
  TV: '电视',
  SPEAKER: '音响',
  LIGHT: '灯光',
  AIR_CONDITIONER: '空调',
  CURTAIN: '窗帘',
  DOOR_LOCK: '门锁',
  CAMERA: '摄像头',
  SENSOR: '传感器',
  OTHER: '其他',
};

const searchQuery = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const showAddModal = ref(false);
const isEditing = ref(false);
const loading = ref(false);

const formData = ref<any>({
  id: null,
  name: '',
  type: 'LIGHT',
  room: '客厅',
  status: 1,
});

const devices = ref<DeviceInfo[]>([]);
const total = ref(0);

// 加载设备列表
const loadDevices = async () => {
  try {
    loading.value = true;
    const result = await getDeviceList({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchQuery.value,
    });
    devices.value = result.records;
    total.value = result.total;
  } catch (error) {
    console.error('加载设备列表失败:', error);
    showToast('error', '加载设备列表失败');
  } finally {
    loading.value = false;
  }
};

const filteredDevices = computed(() => {
  return devices.value.map((device) => ({
    ...device,
    icon: deviceIconMap[device.type] || 'device-sensor',
    typeText: typeTextMap[device.type] || device.type,
    status: device.status === 1,
    online: device.isOnline === 1,
  }));
});

const totalPages = computed(() => Math.ceil(total.value / pageSize.value));

const toast = ref({ show: false, type: 'success', message: '' });

const editDevice = (device: any) => {
  isEditing.value = true;
  formData.value = {
    id: device.id,
    name: device.name,
    type: device.type, // 后端返回的已经是英文值，直接使用
    room: device.room,
    status: device.status === true ? 1 : 0, // 将布尔值转换回 0/1
  };
  showAddModal.value = true;
};

const deleteDeviceAction = async (device: any) => {
  if (!confirm(`确定要删除设备 "${device.name}" 吗？`)) {
    return;
  }

  try {
    await deleteDevice(device.id);
    showToast('success', '设备已删除');
    await loadDevices();
  } catch (error: any) {
    console.error('删除设备失败:', error);
    showToast('error', error.response?.data?.message || '删除设备失败');
  }
};

const handleSubmit = async () => {
  try {
    const currentUserId = userStore.userInfo?.id || 1;
    const submitData = {
      name: formData.value.name,
      type: formData.value.type,
      room: formData.value.room,
      status: formData.value.status,
    };

    if (isEditing.value && formData.value.id) {
      await updateDevice(formData.value.id, submitData);
      showToast('success', '设备已更新');
    } else {
      await createDevice(currentUserId, submitData);
      showToast('success', '设备已添加');
    }

    showAddModal.value = false;
    await loadDevices();
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

// 监听分页变化
const handlePageChange = (page: number) => {
  currentPage.value = page;
  loadDevices();
};

onMounted(() => {
  loadDevices();
});
</script>

<style scoped>
.devices-view {
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

.device-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.device-icon-small {
  width: 36px;
  height: 36px;
  background: linear-gradient(145deg, #f0f0f0 0%, #e0e0e0 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #5A5D43;
  font-size: 18px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s ease;
}

tr:hover .device-icon-small {
  transform: scale(1.08);
  background: linear-gradient(145deg, #e8e8e8 0%, #d8d8d8 100%);
}

.status-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 8px;
}

.status-dot.online {
  background: var(--success-text);
}

.status-dot.offline {
  background: var(--text-muted);
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
  width: 480px;
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

.toast.error {
  border-left: 3px solid var(--error-text);
}
</style>
