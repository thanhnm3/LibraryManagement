<script setup>
import { ref, onMounted, watch } from 'vue'
import { getAllBooks, getAllCategories, getAllAuthors } from '../api'
import BookCard from '../components/BookCard.vue'
import Pagination from '../components/Pagination.vue'

const books = ref([])
const categories = ref([])
const authors = ref([])
const page = ref(0)
const totalPages = ref(0)
const totalElements = ref(0)
const pageSize = 20
const isLoading = ref(true)
const errorMessage = ref('')

const loadBooks = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    const res = await getAllBooks(page.value, pageSize)
    books.value = res.content || []
    totalPages.value = res.totalPages ?? 0
    totalElements.value = res.totalElements ?? 0
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load books.'
    console.error(err)
    books.value = []
  } finally {
    isLoading.value = false
  }
}

const loadCategories = async () => {
  try {
    const res = await getAllCategories(0, 100)
    categories.value = res.content || []
  } catch (err) {
    console.error(err)
    categories.value = []
  }
}

const loadAuthors = async () => {
  try {
    const res = await getAllAuthors(0, 100)
    authors.value = res.content || []
  } catch (err) {
    console.error(err)
    authors.value = []
  }
}

const loadData = async () => {
  await Promise.all([loadBooks(), loadCategories(), loadAuthors()])
}

const onPageChange = (newPage) => {
  page.value = newPage
}

onMounted(loadData)
watch(page, loadBooks)
</script>

<template>
  <main>
    <section id="catalog" class="px-4 py-16 md:px-6 md:py-24" aria-labelledby="catalog-heading">
      <div class="mx-auto max-w-6xl">
        <h1 id="catalog-heading" class="font-heading text-3xl font-bold text-slate-900 md:text-4xl">
          Book catalog
        </h1>
        <p class="mt-2 max-w-2xl font-body text-slate-600">
          Discover books, authors, and categories. Our catalog is updated regularly.
        </p>

        <p v-if="errorMessage" class="mt-4 font-body text-sm text-red-600">
          {{ errorMessage }}
        </p>
        <p v-if="isLoading" class="mt-6 font-body text-slate-600">Loading…</p>
        <template v-else>
          <ul class="mt-10 grid gap-4 sm:grid-cols-2 lg:grid-cols-4" role="list">
            <li v-for="book in books" :key="book.id">
              <BookCard :book="book" />
            </li>
          </ul>
          <Pagination
            :page="page"
            :total-pages="totalPages"
            :disabled="isLoading"
            @change="onPageChange"
          />
          <p v-if="books.length === 0" class="mt-8 font-body text-slate-600">No books in catalog.</p>
        </template>
      </div>
    </section>
  </main>
</template>
