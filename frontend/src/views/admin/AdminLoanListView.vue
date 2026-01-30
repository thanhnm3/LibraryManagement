<script setup>
import { ref, onMounted, watch } from 'vue'
import { RouterLink } from 'vue-router'
import { getAllLoans, getOverdueLoans, getLoanStatistics } from '../../api'
import { ROUTES } from '../../constants/routes'

const loans = ref([])
const overdueLoans = ref([])
const statistics = ref(null)
const page = ref(0)
const totalPages = ref(0)
const pageSize = 20
const statusFilter = ref('')
const userIdFilter = ref('')
const bookIdFilter = ref('')
const isLoading = ref(true)
const errorMessage = ref('')

const loadLoans = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    const res = await getAllLoans({
      page: page.value,
      size: pageSize,
      status: statusFilter.value || undefined,
      userId: userIdFilter.value ? Number(userIdFilter.value) : undefined,
      bookId: bookIdFilter.value ? Number(bookIdFilter.value) : undefined,
    })
    loans.value = res.content || []
    totalPages.value = res.totalPages ?? 0
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load loans.'
    console.error(err)
    loans.value = []
  } finally {
    isLoading.value = false
  }
}

const loadOverdue = async () => {
  try {
    overdueLoans.value = await getOverdueLoans(
      userIdFilter.value ? Number(userIdFilter.value) : undefined
    )
  } catch (err) {
    console.error(err)
    overdueLoans.value = []
  }
}

const loadStatistics = async () => {
  try {
    statistics.value = await getLoanStatistics()
  } catch (err) {
    console.error(err)
    statistics.value = null
  }
}

const loadData = async () => {
  await Promise.all([loadLoans(), loadOverdue(), loadStatistics()])
}

const getDetailPath = (id) => `/admin/loans/${id}`
const getStatusClass = (status) => {
  if (status === 'OVERDUE') return 'bg-red-100 text-red-800'
  if (status === 'RETURNED') return 'bg-slate-100 text-slate-700'
  return 'bg-clay-mint/60 text-slate-700'
}

const applyFilters = () => {
  page.value = 0
  loadData()
}

onMounted(loadData)
watch(page, loadLoans)
</script>

<template>
  <div class="admin-loan-list-view">
    <h1 class="font-heading text-2xl font-bold text-slate-900">
      Quản lý mượn trả
    </h1>
    <p class="mt-2 font-body text-slate-600">
      Danh sách phiếu mượn, filter, quá hạn, thống kê.
    </p>

    <div v-if="statistics" class="mt-6 grid gap-4 sm:grid-cols-4">
      <div class="clay-card p-4">
        <p class="font-body text-sm text-slate-600">Total borrowed</p>
        <p class="font-heading text-xl font-bold text-slate-900">{{ statistics.totalBorrowed ?? 0 }}</p>
      </div>
      <div class="clay-card p-4">
        <p class="font-body text-sm text-slate-600">Total returned</p>
        <p class="font-heading text-xl font-bold text-slate-900">{{ statistics.totalReturned ?? 0 }}</p>
      </div>
      <div class="clay-card p-4">
        <p class="font-body text-sm text-slate-600">Total overdue</p>
        <p class="font-heading text-xl font-bold text-slate-900">{{ statistics.totalOverdue ?? 0 }}</p>
      </div>
    </div>

    <div class="mt-6 flex flex-wrap items-end gap-4">
      <div>
        <label for="filter-status" class="block font-body text-sm text-slate-700">Status</label>
        <select
          id="filter-status"
          v-model="statusFilter"
          class="mt-1 rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
        >
          <option value="">All</option>
          <option value="BORROWED">BORROWED</option>
          <option value="RETURNED">RETURNED</option>
          <option value="OVERDUE">OVERDUE</option>
        </select>
      </div>
      <div>
        <label for="filter-userId" class="block font-body text-sm text-slate-700">User ID</label>
        <input
          id="filter-userId"
          v-model="userIdFilter"
          type="number"
          min="0"
          placeholder="Optional"
          class="mt-1 w-24 rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
        />
      </div>
      <div>
        <label for="filter-bookId" class="block font-body text-sm text-slate-700">Book ID</label>
        <input
          id="filter-bookId"
          v-model="bookIdFilter"
          type="number"
          min="0"
          placeholder="Optional"
          class="mt-1 w-24 rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
        />
      </div>
      <button
        type="button"
        class="clay-btn bg-primary px-4 py-2 font-body font-medium text-white"
        @click="applyFilters"
      >
        Apply
      </button>
    </div>

    <div v-if="overdueLoans.length > 0" class="mt-6">
      <h2 class="font-heading text-lg font-semibold text-slate-900">
        Quá hạn ({{ overdueLoans.length }})
      </h2>
      <ul class="mt-2 space-y-2" role="list">
        <li
          v-for="loan in overdueLoans"
          :key="loan.id"
          class="clay-card flex items-center justify-between p-3"
        >
          <span class="font-body text-slate-900">{{ loan.bookTitle }} — {{ loan.userFullName }}</span>
          <RouterLink
            :to="getDetailPath(loan.id)"
            class="font-body text-sm font-medium text-primary hover:underline"
          >
            Detail
          </RouterLink>
        </li>
      </ul>
    </div>

    <p v-if="errorMessage" class="mt-4 font-body text-sm text-red-600">
      {{ errorMessage }}
    </p>
    <p v-if="isLoading" class="mt-4 font-body text-slate-600">Loading…</p>
    <div v-else class="mt-6 overflow-x-auto">
      <table class="min-w-full divide-y divide-slate-200 rounded-clay border border-slate-200">
        <thead class="bg-slate-50">
          <tr>
            <th scope="col" class="admin-loan-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              ID
            </th>
            <th scope="col" class="admin-loan-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              User
            </th>
            <th scope="col" class="admin-loan-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              Book
            </th>
            <th scope="col" class="admin-loan-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              Due date
            </th>
            <th scope="col" class="admin-loan-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              Status
            </th>
            <th scope="col" class="admin-loan-list-view_th px-4 py-3 text-right font-body text-sm font-medium text-slate-700">
              Actions
            </th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-200 bg-white">
          <tr v-for="loan in loans" :key="loan.id" class="admin-loan-list-view_row">
            <td class="admin-loan-list-view_td px-4 py-3 font-body text-sm text-slate-900">
              {{ loan.id }}
            </td>
            <td class="admin-loan-list-view_td px-4 py-3 font-body text-sm text-slate-900">
              {{ loan.userFullName }} ({{ loan.userId }})
            </td>
            <td class="admin-loan-list-view_td max-w-xs truncate px-4 py-3 font-body text-sm text-slate-900">
              {{ loan.bookTitle }}
            </td>
            <td class="admin-loan-list-view_td px-4 py-3 font-body text-sm text-slate-600">
              {{ loan.dueDate }}
            </td>
            <td class="admin-loan-list-view_td px-4 py-3">
              <span
                :class="['inline-flex rounded-full px-2 py-1 font-body text-xs font-medium', getStatusClass(loan.status)]"
              >
                {{ loan.status }}
              </span>
            </td>
            <td class="admin-loan-list-view_td px-4 py-3 text-right">
              <RouterLink
                :to="getDetailPath(loan.id)"
                class="font-body text-sm font-medium text-primary hover:underline"
              >
                Detail
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
