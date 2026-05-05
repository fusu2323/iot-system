<template>
  <div class="users-view">
    <div class="table-container">
      <div class="table-header">
        <h3 class="table-title">家庭成员</h3>
        <div class="table-tools">
          <div class="table-search">
            <Icon name="search" :size="16" />
            <input type="text" v-model="searchQuery" placeholder="搜索成员..." @change="loadUsers" />
          </div>
          <button class="btn btn-primary" @click="showAddModal = true">
            <Icon name="add" :size="16" /> 添加成员
          </button>
        </div>
      </div>

      <table class="data-table">
        <thead>
          <tr>
            <th>成员</th>
            <th>角色</th>
            <th>邮箱</th>
            <th>手机</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in filteredUsers" :key="user.id">
            <td>
              <div class="user-cell">
                <div class="user-avatar">{{ user.avatar }}</div>
                <div>
                  <div class="user-name">{{ user.nickname }}</div>
                  <div class="user-username">{{ user.username }}</div>
                </div>
              </div>
            </td>
            <td>
              <span :class="['tag', getRoleClass(user.role)]">{{ user.role }}</span>
            </td>
            <td>{{ user.email }}</td>
            <td>{{ user.phone || '-' }}</td>
            <td>
              <span :class="['tag', user.status ? 'tag-green' : 'tag-red']">
                {{ user.status ? '活跃' : '禁用' }}
              </span>
            </td>
            <td>
              <span class="action-link" @click="editUser(user)">编辑</span>
              <span class="action-link delete" @click="deleteUser(user)">删除</span>
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
        <h3 class="modal-title">{{ isEditing ? '编辑成员' : '添加成员' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label class="form-label">用户名</label>
            <input v-model="formData.username" type="text" class="form-input" required />
          </div>
          <div class="form-group">
            <label class="form-label">昵称</label>
            <input v-model="formData.nickname" type="text" class="form-input" />
          </div>
          <div class="form-group">
            <label class="form-label">邮箱</label>
            <input v-model="formData.email" type="email" class="form-input" required />
          </div>
          <div class="form-group">
            <label class="form-label">手机</label>
            <input v-model="formData.phone" type="tel" class="form-input" />
          </div>
          <div class="form-group">
            <label class="form-label">角色</label>
            <select v-model="formData.role" class="form-select">
              <option value="管理员">管理员</option>
              <option value="普通成员">普通成员</option>
              <option value="访客">访客</option>
            </select>
          </div>
          <div class="form-group" v-if="!isEditing">
            <label class="form-label">密码</label>
            <input v-model="formData.password" type="password" class="form-input" />
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
import { getUserList, createUser, updateUser, deleteUser, type UserInfo } from '@/api/user';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();

const searchQuery = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const showAddModal = ref(false);
const isEditing = ref(false);
const loading = ref(false);

const formData = ref<any>({
  id: null,
  username: '',
  nickname: '',
  email: '',
  phone: '',
  role: 'USER',
  password: '',
  status: true,
});

const users = ref<UserInfo[]>([]);
const total = ref(0);

// 角色映射
const roleTextMap: Record<string, string> = {
  ADMIN: '管理员',
  USER: '普通成员',
  GUEST: '访客',
};

const roleValueMap: Record<string, string> = {
  '管理员': 'ADMIN',
  '普通成员': 'USER',
  '访客': 'GUEST',
};

// 加载用户列表
const loadUsers = async () => {
  try {
    loading.value = true;
    const result = await getUserList({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchQuery.value,
    });
    users.value = result.records;
    total.value = result.total;
  } catch (error) {
    showToast('error', '加载用户列表失败');
  } finally {
    loading.value = false;
  }
};

const filteredUsers = computed(() => {
  return users.value.map((user) => ({
    ...user,
    role: roleTextMap[user.role] || user.role,
    avatar: user.nickname?.charAt(0) || user.username?.charAt(0) || 'U',
    status: user.status === 1,
  }));
});

const totalPages = computed(() => Math.ceil(total.value / pageSize.value));

const toast = ref({ show: false, type: 'success', message: '' });

const getRoleClass = (role: string) => {
  const map: Record<string, string> = {
    '管理员': 'tag-blue',
    '普通成员': 'tag-green',
    '访客': 'tag-yellow',
  };
  return map[role] || 'tag-blue';
};

const editUser = (user: any) => {
  isEditing.value = true;
  formData.value = {
    id: user.id,
    username: user.username,
    nickname: user.nickname,
    email: user.email,
    phone: user.phone || '',
    role: roleValueMap[user.role] || user.role,
    status: user.status,
  };
  showAddModal.value = true;
};

const deleteUser = async (user: any) => {
  if (confirm(`确定要删除成员 "${user.nickname}" 吗？`)) {
    try {
      await deleteUser(user.id);
      showToast('success', '成员已删除');
      await loadUsers();
    } catch (error: any) {
      showToast('error', error.response?.data?.message || '删除失败');
    }
  }
};

const handleSubmit = async () => {
  try {
    const submitData = {
      username: formData.value.username,
      password: formData.value.password,
      nickname: formData.value.nickname || formData.value.username,
      email: formData.value.email,
      phone: formData.value.phone,
      role: formData.value.role,
    };

    if (isEditing.value && formData.value.id) {
      await updateUser(formData.value.id, submitData);
      showToast('success', '成员信息已更新');
    } else {
      await createUser(submitData);
      showToast('success', '成员已添加');
    }
    showAddModal.value = false;
    await loadUsers();
  } catch (error: any) {
    console.error('操作失败:', error);
    showToast('error', error.response?.data?.message || '操作失败');
  }
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  loadUsers();
};

const showToast = (type: string, message: string) => {
  toast.value = { show: true, type, message };
  setTimeout(() => {
    toast.value.show = false;
  }, 3000);
};

onMounted(() => {
  loadUsers();
});
</script>

<style scoped>
.users-view {
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

.user-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  background: var(--primary-light);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
  font-size: 14px;
}

.user-name {
  font-weight: 500;
}

.user-username {
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
.tag-yellow {
  background: var(--warning-bg);
  color: var(--warning-text);
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
</style>
