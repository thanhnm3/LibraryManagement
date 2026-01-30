<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardStatistics, getLoanReport, getReviewReport } from '../../api'

const dashboard = ref(null)
const loanReport = ref(null)
const reviewReport = ref(null)
const startDate = ref('')
const endDate = ref('')
const reportBookId = ref('')
const isLoading = ref(true)
const errorMessage = ref('')

const loadDashboard = async () => {
  try {
    dashboard.value = await getDashboardStatistics()
  } catch (err) {
    console.error(err)
    dashboard.value = null
  }
}

const loadLoanReport = async () => {
  try {
    loanReport.value = await getLoanReport(
      startDate.value.trim() || undefined,
      endDate.value.trim() || undefined
    )
  } catch (err) {
    console.error(err)
    loanReport.value = null
  }
}

const loadReviewReport = async () => {
  try {
    reviewReport.value = await getReviewReport(
      reportBookId.value ? Number(reportBookId.value) : undefined
    )
  } catch (err) {
    console.error(err)
    reviewReport.value = null
  }
}

const loadData = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    await Promise.all([loadDashboard(), loadLoanReport(), loadReviewReport()])
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load reports.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

const refreshLoanReport = () => {
  loadLoanReport()
}

const refreshReviewReport = () => {
  loadReviewReport()
}

onMounted(loadData)
</script>

<template>
  <div class="admin-reports-view">
    <h1 class="font-heading text-2xl font-bold text-slate-900">
      Báo cáo
    </h1>
    <p class="mt-2 font-body text-slate-600">
      Tóm tắt dashboard, báo cáo mượn theo ngày, báo cáo đánh giá theo sách.
    </p>

    <p v-if="errorMessage" class="mt-4 font-body text-sm text-red-600">
      {{ errorMessage }}
    </p>
    <p v-if="isLoading" class="mt-6 font-body text-slate-600">Loading…</p>
    <template v-else>
      <section v-if="dashboard" class="mt-6 clay-card p-6" aria-labelledby="dashboard-heading">
        <h2 id="dashboard-heading" class="font-heading text-lg font-semibold text-slate-900">
          Dashboard summary
        </h2>
        <div class="mt-4 grid gap-4 sm:grid-cols-4">
          <div>
            <p class="font-body text-sm text-slate-600">Total books</p>
            <p class="font-heading text-xl font-bold text-slate-900">{{ dashboard.totalBooks ?? 0 }}</p>
          </div>
          <div>
            <p class="font-body text-sm text-slate-600">Total users</p>
            <p class="font-heading text-xl font-bold text-slate-900">{{ dashboard.totalUsers ?? 0 }}</p>
          </div>
          <div>
            <p class="font-body text-sm text-slate-600">Active loans</p>
            <p class="font-heading text-xl font-bold text-slate-900">{{ dashboard.activeLoans ?? 0 }}</p>
          </div>
          <div>
            <p class="font-body text-sm text-slate-600">Overdue loans</p>
            <p class="font-heading text-xl font-bold text-slate-900">{{ dashboard.overdueLoans ?? 0 }}</p>
          </div>
        </div>
      </section>

      <section class="mt-8 clay-card p-6" aria-labelledby="loans-report-heading">
        <h2 id="loans-report-heading" class="font-heading text-lg font-semibold text-slate-900">
          Báo cáo mượn trả (theo ngày)
        </h2>
        <div class="mt-4 flex flex-wrap items-end gap-4">
          <div>
            <label for="report-startDate" class="block font-body text-sm text-slate-700">Start date</label>
            <input
              id="report-startDate"
              v-model="startDate"
              type="date"
              class="mt-1 rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
            />
          </div>
          <div>
            <label for="report-endDate" class="block font-body text-sm text-slate-700">End date</label>
            <input
              id="report-endDate"
              v-model="endDate"
              type="date"
              class="mt-1 rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
            />
          </div>
          <button
            type="button"
            class="clay-btn bg-primary px-4 py-2 font-body font-medium text-white"
            @click="refreshLoanReport"
          >
            Refresh
          </button>
        </div>
        <div v-if="loanReport" class="mt-4 font-body text-slate-700">
          <p>Total borrows: {{ loanReport.totalBorrows ?? 0 }}</p>
          <p>Total returns: {{ loanReport.totalReturns ?? 0 }}</p>
          <p v-if="loanReport.borrowsByDate && Object.keys(loanReport.borrowsByDate).length" class="mt-2">
            Borrows by date: {{ JSON.stringify(loanReport.borrowsByDate) }}
          </p>
          <p v-if="loanReport.returnsByDate && Object.keys(loanReport.returnsByDate).length" class="mt-1">
            Returns by date: {{ JSON.stringify(loanReport.returnsByDate) }}
          </p>
        </div>
      </section>

      <section class="mt-8 clay-card p-6" aria-labelledby="reviews-report-heading">
        <h2 id="reviews-report-heading" class="font-heading text-lg font-semibold text-slate-900">
          Báo cáo đánh giá (theo sách)
        </h2>
        <div class="mt-4 flex flex-wrap items-end gap-4">
          <div>
            <label for="report-bookId" class="block font-body text-sm text-slate-700">Book ID (optional)</label>
            <input
              id="report-bookId"
              v-model="reportBookId"
              type="number"
              min="0"
              placeholder="All books"
              class="mt-1 w-24 rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
            />
          </div>
          <button
            type="button"
            class="clay-btn bg-primary px-4 py-2 font-body font-medium text-white"
            @click="refreshReviewReport"
          >
            Refresh
          </button>
        </div>
        <div v-if="reviewReport" class="mt-4 font-body text-slate-700">
          <p>Book: {{ reviewReport.bookTitle ?? '—' }} (ID: {{ reviewReport.bookId }})</p>
          <p>Average rating: {{ reviewReport.averageRating ?? '—' }}</p>
          <p>Total reviews: {{ reviewReport.totalReviews ?? 0 }}</p>
          <p v-if="reviewReport.ratingDistribution && Object.keys(reviewReport.ratingDistribution).length" class="mt-2">
            Rating distribution: {{ JSON.stringify(reviewReport.ratingDistribution) }}
          </p>
        </div>
      </section>
    </template>
  </div>
</template>
