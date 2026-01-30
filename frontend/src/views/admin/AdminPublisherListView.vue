<script setup>
import { ref, onMounted, watch } from 'vue'
import { RouterLink } from 'vue-router'
import { getAllPublishers, deletePublisher } from '../../api'
import { ROUTES } from '../../constants/routes'

const publishers = ref([])
const page = ref(0)
const totalPages = ref(0)
const pageSize = 20
const isLoading = ref(true)
const errorMessage = ref('')
const deletingId = ref(null)

const loadPublishers = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    const res = await getAllPublishers(page.value, pageSize)
    publishers.value = res.content || []
    totalPages.value = res.totalPages ?? 0
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load publishers.'
    console.error(err)
    publishers.value = []
  } finally {
    isLoading.value = false
  }
}

const getEditPath = (id) => `/admin/publishers/${id}/edit`

const handleDelete = async (id) => {
  if (deletingId.value != null) return
  if (!window.confirm('Delete this publisher?')) return
  deletingId.value = id
  errorMessage.value = ''
  try {
    await deletePublisher(id)
    await loadPublishers()
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to delete.'
    console.error(err)
  } finally {
    deletingId.value = null
  }
}

onMounted(loadPublishers)
watch(page, loadPublishers)
</script>

<template>
  <div class="admin-publisher-list-view">
    <h1 class="font-heading text-2xl font-bold text-slate-900">
      Quản lý NXB
    </h1>
    <p class="mt-2 font-body text-slate-600">
      Danh sách nhà xuất bản. Thêm, sửa, xóa.
    </p>

    <div class="mt-6">
      <RouterLink
        :to="ROUTES.ADMIN_PUBLISHER_NEW"
        class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body font-medium text-slate-700"
      >
        Thêm NXB
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
            <th scope="col" class="admin-publisher-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              ID
            </th>
            <th scope="col" class="admin-publisher-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              Name
            </th>
            <th scope="col" class="admin-publisher-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              Website
            </th>
            <th scope="col" class="admin-publisher-list-view_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700">
              Address
            </th>
            <th scope="col" class="admin-publisher-list-view_th px-4 py-3 text-right font-body text-sm font-medium text-slate-700">
              Actions
            </th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-200 bg-white">
          <tr v-for="pub in publishers" :key="pub.id" class="admin-publisher-list-view_row">
            <td class="admin-publisher-list-view_td px-4 py-3 font-body text-sm text-slate-900">
              {{ pub.id }}
            </td>
            <td class="admin-publisher-list-view_td px-4 py-3 font-body text-sm text-slate-900">
              {{ pub.name }}
            </td>
            <td class="admin-publisher-list-view_td max-w-xs truncate px-4 py-3 font-body text-sm text-slate-600">
              {{ pub.website ?? '—' }}
            </td>
            <td class="admin-publisher-list-view_td max-w-xs truncate px-4 py-3 font-body text-sm text-slate-600">
              {{ pub.address ?? '—' }}
            </td>
            <td class="admin-publisher-list-view_td px-4 py-3 text-right">
              <RouterLink
                :to="getEditPath(pub.id)"
                class="font-body text-sm font-medium text-primary hover:underline"
              >
                Sửa
              </RouterLink>
              <button
                type="button"
                class="ml-3 font-body text-sm font-medium text-red-600 hover:underline disabled:opacity-50"
                :disabled="deletingId === pub.id"
                @click="handleDelete(pub.id)"
              >
                {{ deletingId === pub.id ? 'Deleting…' : 'Xóa' }}
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
