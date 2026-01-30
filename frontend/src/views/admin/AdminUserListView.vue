<script setup>
import { ref, onMounted, watch } from 'vue'
import { RouterLink } from 'vue-router'
import { getAllUsers } from '../../api'
import { ROUTES } from '../../constants/routes'

const users = ref([])
const page = ref(0)
const totalPages = ref(0)
const pageSize = 20
const statusFilter = ref('')
const roleFilter = ref('')
const isLoading = ref(true)
const errorMessage = ref('')

const loadUsers = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    const res = await getAllUsers({
      page: page.value,
      size: pageSize,
      status: statusFilter.value || undefined,
      role: roleFilter.value || undefined,
    })
    users.value = res.content || []
    totalPages.value = res.totalPages ?? 0
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load users.'
    console.error(err)
    users.value = []
  } finally {
    isLoading.value = false
  }
}

const getEditPath = (id) => `/admin/users/${id}/edit`

const applyFilters = () => {
  page.value = 0
  loadUsers()
}

onMounted(loadUsers)
watch(page, loadUsers)
</script>

<template>
  <div class="admin-user-list-view">
    <h1 class="font-heading text-2xl font-bold text-slate-900">
      Quản lý người dùng
    </h1>
    <p class="mt-2 font-body text-slate-600">
      Danh sách user. Filter theo status, role. Sửa thông tin, mật khẩu, status, role.
    </p>

    <div class="mt-6 flex flex-wrap items-end gap-4">
      <div>
        <label for="filter-status" class="block font-body text-sm text-slate-700">Status</label>
        <select
          id="filter-status"
          v-model="statusFilter"
          class="mt-1 rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
        >
          <option value="">All</option>
          <option value="ACTIVE">ACTIVE</option>
          <option value="INACTIVE">INACTIVE</option>
          <option value="BANNED">BANNED</option>
        </select>
      </div>
      <div>
        <label for="filter-role" class="block font-body text-sm text-slate-700">Role</label>
        <select
          id="filter-role"
          v-model="roleFilter"
          class="mt-1 rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
        >
          <option value="">All</option>
          <option value="ADMIN">ADMIN</option>
          <option value="MEMBER">MEMBER</option>
        </select>
      </div>
      <button
        type="button"
        class="clay-btn bg-primary px-4 py-2 font-body font-medium text-white"
        @click="applyFilters"
      >
        Apply
      </button>
    </div>

    <p v-if="errorMessage" class="mt-4 font-body text-sm text-red-600">
      {{ errorMessage }}
    </p>
    <p v-if="isLoading" class="mt-4 font-body text-slate-600">Loading…</p>
    <div v-else class="mt-6 overflow-x-auto">
      <table class="min-w-full divide-y divide-slate-200 rounded-clay border border-slate-200">
        <thead class="bg-slate-50">
          <tr>
            <th scope="col" class="admin-user-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              ID
            </th>
            <th scope="col" class="admin-user-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              Email
            </th>
            <th scope="col" class="admin-user-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              Full name
            </th>
            <th scope="col" class="admin-user-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              Status
            </th>
            <th scope="col" class="admin-user-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              Role
            </th>
            <th scope="col" class="admin-user-list-view_th px-4 py-3 text-right font-body text-sm font-medium text-slate-700">
              Actions
            </th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-200 bg-white">
          <tr v-for="user in users" :key="user.id" class="admin-user-list-view_row">
            <td class="admin-user-list-view_td px-4 py-3 font-body text-sm text-slate-900">
              {{ user.id }}
            </td>
            <td class="admin-user-list-view_td px-4 py-3 font-body text-sm text-slate-900">
              {{ user.email }}
            </td>
            <td class="admin-user-list-view_td px-4 py-3 font-body text-sm text-slate-900">
              {{ user.fullName }}
            </td>
            <td class="admin-user-list-view_td px-4 py-3 font-body text-sm text-slate-600">
              {{ user.status }}
            </td>
            <td class="admin-user-list-view_td px-4 py-3 font-body text-sm text-slate-600">
              {{ user.role }}
            </td>
            <td class="admin-user-list-view_td px-4 py-3 text-right">
              <RouterLink
                :to="getEditPath(user.id)"
                class="font-body text-sm font-medium text-primary hover:underline"
              >
                Sửa
              </RouterLink>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="totalPages > 1" class="mt-4 flex gap-2">
        <button
          type="button"
          class="clay-btn border border-slate-300 bg-white px-3 py-1.5 font-body text-sm text-slate-700 disabled:opacity-50"
          :disabled="page <= 0"
          @click="page = Math.max(0, page - 1)"
        >
          Previous
        </button>
        <span class="flex items-center font-body text-sm text-slate-600">Page {{ page + 1 }} of {{ totalPages }}</span>
        <button
          type="button"
          class="clay-btn border border-slate-300 bg-white px-3 py-1.5 font-body text-sm text-slate-700 disabled:opacity-50"
          :disabled="page >= totalPages - 1"
          @click="page = Math.min(totalPages - 1, page + 1)"
        >
          Next
        </button>
      </div>
    </div>
  </div>
</template>
