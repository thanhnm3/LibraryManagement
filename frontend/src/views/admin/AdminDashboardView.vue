<script setup>
import { ref, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { getDashboardStatistics } from '../../api'
import { ROUTES } from '../../constants/routes'

const statistics = ref(null)
const isLoading = ref(true)
const errorMessage = ref('')

const loadStatistics = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    statistics.value = await getDashboardStatistics()
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load dashboard.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

const authorNames = (author) => author?.fullName ?? '—'
const getBookDetailPath = (id) => `/books/${id}`

onMounted(loadStatistics)
</script>

<template>
  <div class="admin-dashboard-view">
    <h1 class="font-heading text-2xl font-bold text-slate-900">
      Dashboard
    </h1>
    <p class="mt-2 font-body text-slate-600">
      Thống kê tổng quan: sách, user, mượn đang hoạt động, quá hạn; sách mượn nhiều, tác giả nổi bật.
    </p>

    <p v-if="errorMessage" class="mt-4 font-body text-sm text-red-600">
      {{ errorMessage }}
    </p>
    <p v-if="isLoading" class="mt-6 font-body text-slate-600">Loading…</p>
    <template v-else-if="statistics">
      <div class="mt-6 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <div class="clay-card p-4">
          <p class="font-body text-sm text-slate-600">Total books</p>
          <p class="font-heading text-2xl font-bold text-slate-900">{{ statistics.totalBooks ?? 0 }}</p>
        </div>
        <div class="clay-card p-4">
          <p class="font-body text-sm text-slate-600">Total users</p>
          <p class="font-heading text-2xl font-bold text-slate-900">{{ statistics.totalUsers ?? 0 }}</p>
        </div>
        <div class="clay-card p-4">
          <p class="font-body text-sm text-slate-600">Active loans</p>
          <p class="font-heading text-2xl font-bold text-slate-900">{{ statistics.activeLoans ?? 0 }}</p>
        </div>
        <div class="clay-card p-4">
          <p class="font-body text-sm text-slate-600">Overdue loans</p>
          <p class="font-heading text-2xl font-bold text-slate-900">{{ statistics.overdueLoans ?? 0 }}</p>
        </div>
      </div>

      <div class="mt-8 grid gap-8 lg:grid-cols-2">
        <div class="clay-card p-6">
          <h2 class="font-heading text-lg font-semibold text-slate-900">
            Most borrowed books
          </h2>
          <ul v-if="statistics.mostBorrowedBooks?.length" class="mt-4 space-y-2" role="list">
            <li
              v-for="book in statistics.mostBorrowedBooks"
              :key="book.id"
              class="flex items-center justify-between font-body text-slate-900"
            >
              <RouterLink
                :to="getBookDetailPath(book.id)"
                class="font-medium text-primary hover:underline"
              >
                {{ book.title }}
              </RouterLink>
            </li>
          </ul>
          <p v-else class="mt-4 font-body text-slate-600">No data.</p>
        </div>
        <div class="clay-card p-6">
          <h2 class="font-heading text-lg font-semibold text-slate-900">
            Top authors
          </h2>
          <ul v-if="statistics.topAuthors?.length" class="mt-4 space-y-2" role="list">
            <li
              v-for="author in statistics.topAuthors"
              :key="author.id"
              class="font-body text-slate-900"
            >
              {{ authorNames(author) }}
            </li>
          </ul>
          <p v-else class="mt-4 font-body text-slate-600">No data.</p>
        </div>
      </div>
    </template>
  </div>
</template>
