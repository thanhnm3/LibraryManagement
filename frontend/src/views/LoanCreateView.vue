<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getAllBooks, borrowBook } from '../api'
import { ROUTES } from '../constants/routes'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const bookId = ref(null)
const dueDate = ref('')
const books = ref([])
const isLoading = ref(true)
const isSubmitting = ref(false)
const errorMessage = ref('')
const bookPage = ref(0)
const pageSize = 50

const currentUserId = computed(() => authStore.user?.id ?? null)

const preselectedBookId = computed(() => {
  const id = route.query.bookId
  if (id) {
    const num = Number(id)
    return Number.isFinite(num) ? num : null
  }
  return null
})

const loadBooks = async () => {
  const res = await getAllBooks(bookPage.value, pageSize)
  books.value = res.content || []
}

const loadData = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    await loadBooks()
    if (preselectedBookId.value) {
      bookId.value = preselectedBookId.value
    }
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load data.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

const isFormValid = computed(() => {
  return (
    currentUserId.value != null &&
    bookId.value != null &&
    dueDate.value.trim() !== ''
  )
})

const handleSubmit = async () => {
  if (!isFormValid.value || isSubmitting.value) return
  errorMessage.value = ''
  isSubmitting.value = true
  try {
    await borrowBook({
      userId: currentUserId.value,
      bookId: Number(bookId.value),
      dueDate: dueDate.value.trim(),
    })
    router.push(ROUTES.LOANS)
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to create loan.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <div class="loan-create-view px-4 py-16 md:px-6 md:py-24">
    <div class="mx-auto max-w-md">
      <h1 class="font-heading text-3xl font-bold text-slate-900">
        Borrow a Book
      </h1>
      <p class="mt-2 font-body text-slate-600">
        Select book and due date to borrow for your account.
      </p>

      <p v-if="isLoading" class="mt-6 font-body text-slate-600">
        Loading…
      </p>
      <form v-else class="mt-8 space-y-4" @submit.prevent="handleSubmit">
        <div>
          <label for="loan-bookId" class="block font-body text-sm font-medium text-slate-700">
            Book
          </label>
          <select
            id="loan-bookId"
            v-model="bookId"
            required
            class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
          >
            <option :value="null" disabled>Select book</option>
            <option v-for="b in books" :key="b.id" :value="b.id">
              {{ b.title }} ({{ b.isbn }})
            </option>
          </select>
        </div>
        <div>
          <label for="loan-dueDate" class="block font-body text-sm font-medium text-slate-700">
            Due date
          </label>
          <input
            id="loan-dueDate"
            v-model="dueDate"
            type="datetime-local"
            required
            class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
          />
        </div>
        <p v-if="errorMessage" class="font-body text-sm text-red-600">
          {{ errorMessage }}
        </p>
        <button
          type="submit"
          :disabled="!isFormValid || isSubmitting"
          class="clay-btn w-full bg-primary px-4 py-2.5 font-body font-medium text-white disabled:opacity-50"
        >
          {{ isSubmitting ? 'Creating…' : 'Borrow' }}
        </button>
      </form>
    </div>
  </div>
</template>
