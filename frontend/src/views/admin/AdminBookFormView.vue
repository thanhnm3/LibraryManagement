<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getBookById, createBook, updateBook, getAllAuthors, getAllCategories, getAllPublishers } from '../../api'
import { ROUTES } from '../../constants/routes'

const route = useRoute()
const router = useRouter()
const title = ref('')
const isbn = ref('')
const publicationYear = ref(new Date().getFullYear())
const description = ref('')
const coverImageUrl = ref('')
const filePath = ref('')
const publisherId = ref(null)
const authorIds = ref([])
const categoryIds = ref([])
const authors = ref([])
const categories = ref([])
const publishers = ref([])
const isLoading = ref(true)
const isSubmitting = ref(false)
const errorMessage = ref('')

const isEdit = computed(() => Boolean(route.params.id && route.params.id !== 'new'))
const bookId = computed(() => (isEdit.value ? Number(route.params.id) : null))

const loadOptions = async () => {
  try {
    const [authorsRes, categoriesRes, publishersRes] = await Promise.all([
      getAllAuthors(0, 500),
      getAllCategories(0, 500),
      getAllPublishers(0, 500),
    ])
    authors.value = authorsRes.content || []
    categories.value = categoriesRes.content || []
    publishers.value = publishersRes.content || []
  } catch (err) {
    console.error(err)
  }
}

const loadBook = async () => {
  if (!isEdit.value) {
    isLoading.value = false
    return
  }
  isLoading.value = true
  errorMessage.value = ''
  try {
    const book = await getBookById(bookId.value)
    title.value = book.title ?? ''
    isbn.value = book.isbn ?? ''
    publicationYear.value = book.publicationYear ?? new Date().getFullYear()
    description.value = book.description ?? ''
    coverImageUrl.value = book.coverImageUrl ?? ''
    filePath.value = book.filePath ?? ''
    publisherId.value = book.publisher?.id ?? null
    authorIds.value = book.authors?.map((a) => a.id) ?? []
    categoryIds.value = book.categories?.map((c) => c.id) ?? []
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load book.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

const loadData = async () => {
  isLoading.value = true
  await loadOptions()
  if (isEdit.value) {
    await loadBook()
  } else {
    isLoading.value = false
  }
}

const toggleAuthor = (id) => {
  const index = authorIds.value.indexOf(id)
  if (index === -1) {
    authorIds.value = [...authorIds.value, id]
  } else {
    authorIds.value = authorIds.value.filter((_, i) => i !== index)
  }
}

const toggleCategory = (id) => {
  const index = categoryIds.value.indexOf(id)
  if (index === -1) {
    categoryIds.value = [...categoryIds.value, id]
  } else {
    categoryIds.value = categoryIds.value.filter((_, i) => i !== index)
  }
}

const handleSubmit = async () => {
  if (!title.value.trim() || !isbn.value.trim() || !publicationYear.value || publisherId.value == null) return
  isSubmitting.value = true
  errorMessage.value = ''
  const payload = {
    title: title.value.trim(),
    isbn: isbn.value.trim(),
    publicationYear: Number(publicationYear.value),
    description: description.value.trim() || undefined,
    coverImageUrl: coverImageUrl.value.trim() || undefined,
    filePath: filePath.value.trim() || undefined,
    publisherId: Number(publisherId.value),
    authorIds: [...authorIds.value],
    categoryIds: [...categoryIds.value],
  }
  try {
    if (isEdit.value) {
      await updateBook(bookId.value, payload)
    } else {
      await createBook(payload)
    }
    router.push(ROUTES.ADMIN_BOOKS)
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to save.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <div class="admin-book-form-view">
    <h1 class="font-heading text-2xl font-bold text-slate-900">
      {{ isEdit ? 'Sửa sách' : 'Thêm sách' }}
    </h1>
    <p v-if="isLoading" class="mt-4 font-body text-slate-600">Loading…</p>
    <form v-else class="mt-6 max-w-2xl space-y-4" @submit.prevent="handleSubmit">
      <div>
        <label for="book-title" class="block font-body text-sm font-medium text-slate-700">
          Title
        </label>
        <input
          id="book-title"
          v-model="title"
          type="text"
          required
          class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
        />
      </div>
      <div>
        <label for="book-isbn" class="block font-body text-sm font-medium text-slate-700">
          ISBN
        </label>
        <input
          id="book-isbn"
          v-model="isbn"
          type="text"
          required
          class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
        />
      </div>
      <div>
        <label for="book-year" class="block font-body text-sm font-medium text-slate-700">
          Publication year
        </label>
        <input
          id="book-year"
          v-model.number="publicationYear"
          type="number"
          required
          min="1000"
          class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
        />
      </div>
      <div>
        <label for="book-description" class="block font-body text-sm font-medium text-slate-700">
          Description
        </label>
        <textarea
          id="book-description"
          v-model="description"
          rows="4"
          class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
        />
      </div>
      <div>
        <label for="book-coverImageUrl" class="block font-body text-sm font-medium text-slate-700">
          Cover image URL
        </label>
        <input
          id="book-coverImageUrl"
          v-model="coverImageUrl"
          type="url"
          class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
        />
      </div>
      <div>
        <label for="book-filePath" class="block font-body text-sm font-medium text-slate-700">
          File path
        </label>
        <input
          id="book-filePath"
          v-model="filePath"
          type="text"
          class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
        />
      </div>
      <div>
        <label for="book-publisherId" class="block font-body text-sm font-medium text-slate-700">
          Publisher
        </label>
        <select
          id="book-publisherId"
          v-model="publisherId"
          required
          class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
        >
          <option :value="null" disabled>Select publisher</option>
          <option v-for="p in publishers" :key="p.id" :value="p.id">
            {{ p.name }}
          </option>
        </select>
      </div>
      <div>
        <label class="block font-body text-sm font-medium text-slate-700">
          Authors
        </label>
        <div class="mt-2 flex flex-wrap gap-2">
          <label
            v-for="a in authors"
            :key="a.id"
            class="inline-flex cursor-pointer items-center gap-2 rounded-clay border border-slate-300 px-3 py-1.5 font-body text-sm"
          >
            <input
              type="checkbox"
              :checked="authorIds.includes(a.id)"
              @change="toggleAuthor(a.id)"
            />
            {{ a.fullName }}
          </label>
        </div>
      </div>
      <div>
        <label class="block font-body text-sm font-medium text-slate-700">
          Categories
        </label>
        <div class="mt-2 flex flex-wrap gap-2">
          <label
            v-for="c in categories"
            :key="c.id"
            class="inline-flex cursor-pointer items-center gap-2 rounded-clay border border-slate-300 px-3 py-1.5 font-body text-sm"
          >
            <input
              type="checkbox"
              :checked="categoryIds.includes(c.id)"
              @change="toggleCategory(c.id)"
            />
            {{ c.name }}
          </label>
        </div>
      </div>
      <p v-if="errorMessage" class="font-body text-sm text-red-600">
        {{ errorMessage }}
      </p>
      <div class="flex gap-3">
        <button
          type="submit"
          class="clay-btn bg-primary px-4 py-2 font-body font-medium text-white disabled:opacity-50"
          :disabled="isSubmitting || !title.trim() || !isbn.trim() || publisherId == null"
        >
          {{ isSubmitting ? 'Saving…' : 'Save' }}
        </button>
        <RouterLink
          :to="ROUTES.ADMIN_BOOKS"
          class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body font-medium text-slate-700"
        >
          Cancel
        </RouterLink>
      </div>
    </form>
  </div>
</template>
