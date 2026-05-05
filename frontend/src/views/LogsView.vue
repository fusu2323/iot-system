<template>
  <div class="logs-view">
    <div class="table-container">
      <div class="table-header">
        <h3 class="table-title">操作日志</h3>
        <div class="table-tools">
          <div class="table-search">
            <Icon name="search" :size="16" />
            <input type="text" v-model="searchQuery" placeholder="搜索日志..." @change="handlePageChange(1)" />
          </div>
          <select v-model="filterType" class="filter-select" @change="handleFilterChange">
            <option value="">全部类型</option>
            <option value="创建">创建</option>
            <option value="更新">更新</option>
            <option value="删除">删除</option>
            <option value="登录">登录</option>
          </select>
          <button class="btn btn-secondary" @click="exportLogs">
            <Icon name="download" :size="16" /> 导出日志
          </button>
        </div>
      </div>

      <table class="data-table">
        <thead>
          <tr>
            <th>操作类型</th>
            <th>操作内容</th>
            <th>操作人</th>
            <th>IP 地址</th>
            <th>操作时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="log in filteredLogs" :key="log.id">
            <td>
              <span :class="['log-type', getLogTypeClass(log.type)]">
                {{ log.type }}
              </span>
            </td>
            <td>{{ log.content }}</td>
            <td>{{ log.user }}</td>
            <td>{{ log.ip }}</td>
            <td>{{ log.time }}</td>
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
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import Icon from '@/components/Icon.vue';
import { getLogList, type OperationLogInfo } from '@/api/log';

const searchQuery = ref('');
const filterType = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const loading = ref(false);

const logs = ref<OperationLogInfo[]>([]);
const total = ref(0);

// 操作类型映射
const operationTextMap: Record<string, string> = {
  CREATE: '创建',
  UPDATE: '更新',
  DELETE: '删除',
  LOGIN: '登录',
  LOGOUT: '退出',
};

// 加载日志列表
const loadLogs = async () => {
  try {
    loading.value = true;
    const result = await getLogList({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchQuery.value,
      operation: filterType.value ? Object.keys(operationTextMap).find(key => operationTextMap[key] === filterType.value) : undefined,
    });
    logs.value = result.records;
    total.value = result.total;
  } catch (error) {
    console.error('加载日志失败:', error);
  } finally {
    loading.value = false;
  }
};

const filteredLogs = computed(() => {
  return logs.value.map((log) => ({
    ...log,
    type: operationTextMap[log.operation] || log.operation,
    user: log.username,
    content: getLogContent(log.operation, log.targetType, log.targetName),
    time: formatTime(log.createTime),
  }));
});

// 生成日志内容
const getLogContent = (operation: string, targetType?: string, targetName?: string) => {
  const typeMap: Record<string, string> = {
    DEVICE: '设备',
    SCENE: '场景',
    USER: '用户',
    CONTENT: '内容',
  };
  const typeText = typeMap[targetType || ''] || '资源';
  const name = targetName ? `"${targetName}"` : '';

  switch (operation) {
    case 'CREATE': return `添加了新${typeText} ${name}`;
    case 'UPDATE': return `更新了${typeText} ${name}`;
    case 'DELETE': return `删除了${typeText} ${name}`;
    case 'LOGIN': return '用户登录系统';
    case 'LOGOUT': return '用户退出系统';
    default: return `进行了操作 ${name}`;
  }
};

// 格式化时间
const formatTime = (timeStr?: string) => {
  if (!timeStr) return '';
  const date = new Date(timeStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

const totalPages = computed(() => Math.ceil(total.value / pageSize.value));

const getLogTypeClass = (type: string) => {
  const map: Record<string, string> = {
    '创建': 'log-create',
    '更新': 'log-update',
    '删除': 'log-delete',
    '登录': 'log-login',
  };
  return map[type] || '';
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  loadLogs();
};

const handleFilterChange = () => {
  currentPage.value = 1;
  loadLogs();
};

const exportLogs = () => {
  console.log('导出日志');
  alert('日志导出功能开发中...');
};

onMounted(() => {
  loadLogs();
});
</script>

<style scoped>
.logs-view {
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

.log-type {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 500;
}

.log-create {
  background: var(--success-bg);
  color: var(--success-text);
}

.log-update {
  background: var(--info-bg);
  color: var(--info-text);
}

.log-delete {
  background: var(--error-bg);
  color: var(--error-text);
}

.log-login {
  background: var(--bg-secondary);
  color: var(--text-secondary);
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

.btn-secondary {
  background: var(--bg-secondary);
  color: var(--text-primary);
  border: 1px solid var(--border-color);
}

.btn-secondary:hover {
  background: var(--bg-hover);
}
</style>
