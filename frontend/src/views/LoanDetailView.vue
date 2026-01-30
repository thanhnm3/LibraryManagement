<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getLoanById, returnBook, renewLoan } from '../api'
import { ROUTES } from '../constants/routes'

const route = useRoute()
const router = useRouter()
const loanId = computed(() => Number(route.params.id))
const loan = ref(null)
const isLoading = ref(true)
const errorMessage = ref('')
const isReturning = ref(false)
const isRenewing = ref(false)
const showRenewForm = ref(false)
const newDueDate = ref('')

const loadLoan = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    loan.value = await getLoanById(loanId.value)
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load loan.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

const handleReturn = async () => {
  if (isReturning.value) return
  isReturning.value = true
  errorMessage.value = ''
  try {
    await returnBook(loanId.value)
    await loadLoan()
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to return.'
    console.error(err)
  } finally {
    isReturning.value = false
  }
}

const handleRenew = async () => {
  if (isRenewing.value || !newDueDate.value.trim()) return
  isRenewing.value = true
  errorMessage.value = ''
  try {
    await renewLoan(loanId.value, { newDueDate: newDueDate.value.trim() })
    showRenewForm.value = false
    newDueDate.value = ''
    await loadLoan()
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to renew.'
    console.error(err)
  } finally {
    isRenewing.value = false
  }
}

const canReturn = computed(() => {
  return loan.value && (loan.value.status === 'BORROWED' || loan.value.status === 'OVERDUE')
})

const canRenew = computed(() => {
  return loan.value && loan.value.status === 'BORROWED'
})

onMounted(loadLoan)
</script>

<template>
  <div class="loan-detail-view px-4 py-16 md:px-6 md:py-24">
    <div class="mx-auto max-w-2xl">
      <h1 class="font-heading text-3xl font-bold text-slate-900">
        Loan Detail
      </h1>
      <p v-if="isLoading" class="mt-4 font-body text-slate-600">Loading…</p>
      <p v-else-if="errorMessage && !loan" class="mt-4 font-body text-red-600">
        {{ errorMessage }}
      </p>
      <template v-else-if="loan">
        <div class="mt-6 space-y-4">
          <div class="clay-card p-4">
            <p class="font-body text-sm text-slate-600">User</p>
            <p class="font-body font-semibold text-slate-900">{{ loan.user?.fullName ?? loan.userFullName }} ({{ loan.user?.email }})</p>
          </div>
          <div class="clay-card p-4">
            <p class="font-body text-sm text-slate-600">Book</p>
            <p class="font-body font-semibold text-slate-900">{{ loan.book?.title ?? loan.bookTitle }}</p>
          </div>
          <div class="clay-card p-4">
            <p class="font-body text-sm text-slate-600">Borrow date</p>
            <p class="font-body text-slate-900">{{ loan.borrowDate }}</p>
          </div>
          <div class="clay-card p-4">
            <p class="font-body text-sm text-slate-600">Due date</p>
            <p class="font-body text-slate-900">{{ loan.dueDate }}</p>
          </div>
          <div class="clay-card p-4">
            <p class="font-body text-sm text-slate-600">Return date</p>
            <p class="font-body text-slate-900">{{ loan.returnDate ?? '—' }}</p>
          </div>
          <div class="clay-card p-4">
            <p class="font-body text-sm text-slate-600">Status</p>
            <span
              :class="[
                'inline-flex rounded-full px-3 py-1 font-body text-sm font-medium',
                loan.status === 'OVERDUE' ? 'bg-red-100 text-red-800' : loan.status === 'RETURNED' ? 'bg-slate-100 text-slate-700' : 'bg-clay-mint/60 text-slate-700',
              ]"
            >
              {{ loan.status }}
            </span>
          </div>
        </div>
        <p v-if="errorMessage" class="mt-4 font-body text-sm text-red-600">{{ errorMessage }}</p>
        <div class="mt-6 flex flex-wrap gap-3">
          <button
            v-if="canReturn"
            type="button"
            class="clay-btn bg-primary px-4 py-2 font-body font-medium text-white"
            :disabled="isReturning"
            @click="handleReturn"
          >
            {{ isReturning ? 'Returning…' : 'Return book' }}
          </button>
          <template v-if="canRenew">
            <button
              v-if="!showRenewForm"
              type="button"
              class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body font-medium text-slate-700"
              @click="showRenewForm = true"
            >
              Renew
            </button>
            <div v-else class="flex flex-wrap items-end gap-2">
              <div>
                <label for="loan-newDueDate" class="sr-only">New due date</label>
                <input
                  id="loan-newDueDate"
                  v-model="newDueDate"
                  type="datetime-local"
                  class="rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
                />
              </div>
              <button
                type="button"
                class="clay-btn bg-primary px-4 py-2 font-body font-medium text-white"
                :disabled="isRenewing || !newDueDate.trim()"
                @click="handleRenew"
              >
                {{ isRenewing ? 'Renewing…' : 'Confirm renew' }}
              </button>
              <button
                type="button"
                class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body font-medium text-slate-700"
                @click="showRenewForm = false; newDueDate = ''"
              >
                Cancel
              </button>
            </div>
          </template>
        </div>
        <div class="mt-6">
          <router-link
            :to="ROUTES.LOANS"
            class="font-body text-sm font-medium text-primary hover:underline"
          >
            Back to My Loans
          </router-link>
        </div>
      </template>
    </div>
  </div>
</template>
