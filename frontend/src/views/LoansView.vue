<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import { RouterLink } from 'vue-router'
import { getActiveLoansByUserId, getLoanHistoryByUserId } from '../api'
import { ROUTES } from '../constants/routes'

const route = useRoute()
const activeTab = ref('active')
const activeLoans = ref([])
const historyLoans = ref([])
const isLoading = ref(false)
const errorMessage = ref('')

const userId = computed(() => {
  const id = route.query.userId
  if (id) {
    const num = Number(id)
    return Number.isFinite(num) ? num : null
  }
  return null
})

const loadActive = async () => {
  if (userId.value == null) return
  isLoading.value = true
  errorMessage.value = ''
  try {
    activeLoans.value = await getActiveLoansByUserId(userId.value)
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load active loans.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

const loadHistory = async () => {
  if (userId.value == null) return
  isLoading.value = true
  errorMessage.value = ''
  try {
    historyLoans.value = await getLoanHistoryByUserId(userId.value)
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load history.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

const loadData = async () => {
  if (userId.value == null) return
  await Promise.all([loadActive(), loadHistory()])
}

const getStatusLabel = (status) => {
  if (status === 'OVERDUE') return 'Overdue'
  if (status === 'RETURNED') return 'Returned'
  return 'On loan'
}

const getStatusClass = (status) => {
  if (status === 'OVERDUE') return 'bg-red-100 text-red-800'
  if (status === 'RETURNED') return 'bg-slate-100 text-slate-700'
  return 'bg-clay-mint/60 text-slate-700'
}

onMounted(loadData)
watch(userId, loadData)
</script>

<template>
  <main>
    <section id="my-loans" class="px-4 py-16 md:px-6 md:py-24" aria-labelledby="my-loans-heading">
      <div class="mx-auto max-w-6xl">
        <h1 id="my-loans-heading" class="font-heading text-3xl font-bold text-slate-900 md:text-4xl">
          My Loans
        </h1>
        <p class="mt-2 max-w-2xl font-body text-slate-600">
          View your active borrows and history. Use <code class="rounded bg-slate-100 px-1">?userId=1</code> to select a user (no auth yet).
        </p>

        <div v-if="userId == null" class="mt-10 clay-card max-w-2xl p-6">
          <p class="font-body text-slate-600">
            Add <code class="rounded bg-slate-100 px-1">?userId=1</code> to the URL to see loans for that user, or
            <RouterLink :to="ROUTES.LOAN_NEW" class="font-medium text-primary hover:underline">
              borrow a book
            </RouterLink>.
          </p>
        </div>

        <template v-else>
          <div class="mt-6 flex gap-2 border-b border-slate-200">
            <button
              type="button"
              :class="[
                'px-4 py-2 font-body font-medium transition-colors',
                activeTab === 'active'
                  ? 'border-b-2 border-primary text-primary'
                  : 'text-slate-600 hover:text-slate-900',
              ]"
              @click="activeTab = 'active'"
            >
              Đang mượn
            </button>
            <button
              type="button"
              :class="[
                'px-4 py-2 font-body font-medium transition-colors',
                activeTab === 'history'
                  ? 'border-b-2 border-primary text-primary'
                  : 'text-slate-600 hover:text-slate-900',
              ]"
              @click="activeTab = 'history'"
            >
              Lịch sử
            </button>
          </div>
          <p v-if="errorMessage" class="mt-4 font-body text-sm text-red-600">
            {{ errorMessage }}
          </p>
          <p v-if="isLoading" class="mt-4 font-body text-slate-600">Loading…</p>
          <div v-else class="clay-card mt-6 max-w-4xl overflow-hidden p-0">
            <ul class="divide-y divide-slate-200" role="list">
              <li
                v-for="loan in (activeTab === 'active' ? activeLoans : historyLoans)"
                :key="loan.id"
                class="flex flex-col gap-2 px-4 py-4 md:flex-row md:items-center md:justify-between md:px-6"
              >
                <div class="flex items-center gap-3">
                  <div class="flex h-10 w-10 shrink-0 items-center justify-center rounded-clay bg-clay-blue/50">
                    <svg class="h-5 w-5 text-primary" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
                      <path d="M18 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zM6 4h5v8l-2.5-1.5L6 12V4z" />
                    </svg>
                  </div>
                  <div>
                    <RouterLink
                      :to="{ name: 'loanDetail', params: { id: loan.id } }"
                      class="font-body font-semibold text-slate-900 hover:text-primary hover:underline"
                    >
                      {{ loan.bookTitle }}
                    </RouterLink>
                    <p class="font-body text-sm text-slate-600">Due {{ loan.dueDate }}</p>
                  </div>
                </div>
                <div class="flex items-center gap-2">
                  <span
                    :class="['inline-flex w-fit rounded-full px-3 py-1 font-body text-sm font-medium', getStatusClass(loan.status)]"
                  >
                    {{ getStatusLabel(loan.status) }}
                  </span>
                  <RouterLink
                    :to="{ name: 'loanDetail', params: { id: loan.id } }"
                    class="clay-btn border border-slate-300 bg-white px-3 py-1.5 font-body text-sm text-slate-700"
                  >
                    Detail
                  </RouterLink>
                </div>
              </li>
            </ul>
            <div
              v-if="(activeTab === 'active' ? activeLoans : historyLoans).length === 0"
              class="px-4 py-8 text-center font-body text-slate-600 md:px-6"
            >
              No loans in this tab.
            </div>
          </div>
          <p class="mt-4">
            <RouterLink :to="ROUTES.LOAN_NEW" class="font-body text-sm font-medium text-primary hover:underline">
              Borrow a book
            </RouterLink>
          </p>
        </template>
      </div>
    </section>
  </main>
</template>
