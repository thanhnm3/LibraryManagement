<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { RouterLink } from 'vue-router'
import { getAllAuthors, deleteAuthor } from '../../api'
import { ROUTES } from '../../constants/routes'

const router = useRouter()
const authors = ref([])
const page = ref(0)
const totalPages = ref(0)
const pageSize = 20
const search = ref('')
const isLoading = ref(true)
const errorMessage = ref('')
const deletingId = ref(null)

const loadAuthors = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    const res = await getAllAuthors(page.value, pageSize, search.value.trim() || undefined)
    authors.value = res.content || []
    totalPages.value = res.totalPages ?? 0
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load authors.'
    console.error(err)
    authors.value = []
  } finally {
    isLoading.value = false
  }
}

const getEditPath = (id) => `/admin/authors/${id}/edit`

const handleDelete = async (id) => {
  if (deletingId.value != null) return
  if (!window.confirm('Delete this author?')) return
  deletingId.value = id
  errorMessage.value = ''
  try {
    await deleteAuthor(id)
    await loadAuthors()
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to delete.'
    console.error(err)
  } finally {
    deletingId.value = null
  }
}

const handleSearch = () => {
  page.value = 0
  loadAuthors()
}

onMounted(loadAuthors)
watch(page, loadAuthors)
</script>

<template>
  <div class="admin-author-list-view">
    <h1 class="font-heading text-2xl font-bold text-slate-900">
      Quản lý tác giả
    </h1>
    <p class="mt-2 font-body text-slate-600">
      Danh sách tác giả. Thêm, sửa, xóa.
    </p>

    <div class="mt-6 flex flex-wrap items-center gap-4">
      <input
        v-model="search"
        type="text"
        placeholder="Search by name"
        class="rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
        @keyup.enter="handleSearch"
      />
      <button
        type="button"
        class="clay-btn bg-primary px-4 py-2 font-body font-medium text-white"
        @click="handleSearch"
      >
        Search
      </button>
      <RouterLink
        :to="ROUTES.ADMIN_AUTHOR_NEW"
        class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body font-medium text-slate-700"
      >
        Thêm tác giả
      </RouterLink>
    </div>

    <p v-if="errorMessage" class="mt-4 font-body text-sm text-red-600">
      {{ errorMessage }}
    </p>
    <p v-if="isLoading" class="mt-4 font-body text-slate-600">Loading…</p>
    <div v-else class="mt-6 overflow-x-auto">
      <table class="min-w-full divide-y divide-slate-200 rounded-clay border border-slate-200">
        <thead class="bg-slate-50">
          <tr>
            <th scope="col" class="admin-author-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              ID
            </th>
            <th scope="col" class="admin-author-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              Full name
            </th>
            <th scope="col" class="admin-author-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              Bio
            </th>
            <th scope="col" class="admin-author-list-view_th px-4 py-3 text-right font-body text-sm font-medium text-slate-700">
              Actions
            </th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-200 bg-white">
          <tr v-for="author in authors" :key="author.id" class="admin-author-list-view_row">
            <td class="admin-author-list-view_td px-4 py-3 font-body text-sm text-slate-900">
              {{ author.id }}
            </td>
            <td class="admin-author-list-view_td px-4 py-3 font-body text-sm text-slate-900">
              {{ author.fullName }}
            </td>
            <td class="admin-author-list-view_td max-w-xs truncate px-4 py-3 font-body text-sm text-slate-600">
              {{ author.bio ?? '—' }}
            </td>
            <td class="admin-author-list-view_td px-4 py-3 text-right">
              <RouterLink
                :to="getEditPath(author.id)"
                class="font-body text-sm font-medium text-primary hover:underline"
              >
                Sửa
              </RouterLink>
              <button
                type="button"
                class="ml-3 font-body text-sm font-medium text-red-600 hover:underline disabled:opacity-50"
                :disabled="deletingId === author.id"
                @click="handleDelete(author.id)"
              >
                {{ deletingId === author.id ? 'Deleting…' : 'Xóa' }}
              </button>
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
