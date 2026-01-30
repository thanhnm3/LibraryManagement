<script setup>
import { ref } from 'vue'
import { RouterLink } from 'vue-router'
import { advancedSearch } from '../api'

const categoryName = ref('')
const authorName = ref('')
const title = ref('')
const results = ref([])
const isLoading = ref(false)
const errorMessage = ref('')
const hasSearched = ref(false)

const handleSearch = async () => {
  isLoading.value = true
  errorMessage.value = ''
  hasSearched.value = true
  try {
    results.value = await advancedSearch({
      categoryName: categoryName.value.trim() || undefined,
      authorName: authorName.value.trim() || undefined,
      title: title.value.trim() || undefined,
    })
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Search failed.'
    console.error(err)
    results.value = []
  } finally {
    isLoading.value = false
  }
}

const authorNames = (book) => {
  if (!book.authors || book.authors.length === 0) return '—'
  return book.authors.map((a) => a.fullName).join(', ')
}
</script>

<template>
  <div class="search-view px-4 py-16 md:px-6 md:py-24">
    <div class="mx-auto max-w-4xl">
      <h1 class="font-heading text-3xl font-bold text-slate-900">
        Search
      </h1>
      <p class="mt-2 font-body text-slate-600">
        Advanced search by category, author, or title.
      </p>

      <form class="mt-8 space-y-4 rounded-clay border border-slate-200 bg-slate-50/50 p-6" @submit.prevent="handleSearch">
        <div class="grid gap-4 sm:grid-cols-3">
          <div>
            <label for="search-category" class="block font-body text-sm font-medium text-slate-700">
              Category name
            </label>
            <input
              id="search-category"
              v-model="categoryName"
              type="text"
              class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
            />
          </div>
          <div>
            <label for="search-author" class="block font-body text-sm font-medium text-slate-700">
              Author name
            </label>
            <input
              id="search-author"
              v-model="authorName"
              type="text"
              class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
            />
          </div>
          <div>
            <label for="search-title" class="block font-body text-sm font-medium text-slate-700">
              Title
            </label>
            <input
              id="search-title"
              v-model="title"
              type="text"
              class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
            />
          </div>
        </div>
        <button
          type="submit"
          class="clay-btn bg-primary px-4 py-2 font-body font-medium text-white"
          :disabled="isLoading"
        >
          {{ isLoading ? 'Searching…' : 'Search' }}
        </button>
      </form>

      <p v-if="errorMessage" class="mt-4 font-body text-sm text-red-600">
        {{ errorMessage }}
      </p>
      <p v-else-if="isLoading" class="mt-6 font-body text-slate-600">Loading…</p>
      <div v-else-if="hasSearched" class="mt-8">
        <h2 class="font-heading text-xl font-semibold text-slate-900">
          Results ({{ results.length }})
        </h2>
        <ul v-if="results.length > 0" class="mt-4 space-y-3" role="list">
          <li v-for="book in results" :key="book.id" class="clay-card p-4">
            <RouterLink
              :to="{ name: 'bookDetail', params: { id: book.id } }"
              class="font-body font-semibold text-slate-900 hover:text-primary hover:underline"
            >
              {{ book.title }}
            </RouterLink>
            <p class="mt-1 font-body text-sm text-slate-600">
              {{ book.isbn }} · {{ authorNames(book) }}
            </p>
          </li>
        </ul>
        <p v-else class="mt-4 font-body text-slate-600">No books found.</p>
      </div>
    </div>
  </div>
</template>
