<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAuthorById, createAuthor, updateAuthor } from '../../api'
import { ROUTES } from '../../constants/routes'

const route = useRoute()
const router = useRouter()
const fullName = ref('')
const bio = ref('')
const isLoading = ref(true)
const isSubmitting = ref(false)
const errorMessage = ref('')

const isEdit = computed(() => Boolean(route.params.id && route.params.id !== 'new'))
const authorId = computed(() => (isEdit.value ? Number(route.params.id) : null))

const loadAuthor = async () => {
  if (!isEdit.value) {
    isLoading.value = false
    return
  }
  isLoading.value = true
  errorMessage.value = ''
  try {
    const author = await getAuthorById(authorId.value)
    fullName.value = author.fullName ?? ''
    bio.value = author.bio ?? ''
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load author.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

const handleSubmit = async () => {
  if (!fullName.value.trim()) return
  isSubmitting.value = true
  errorMessage.value = ''
  try {
    if (isEdit.value) {
      await updateAuthor(authorId.value, {
        fullName: fullName.value.trim(),
        bio: bio.value.trim() || null,
      })
    } else {
      await createAuthor({
        fullName: fullName.value.trim(),
        bio: bio.value.trim() || null,
      })
    }
    router.push(ROUTES.ADMIN_AUTHORS)
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to save.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

onMounted(loadAuthor)
</script>

<template>
  <div class="admin-author-form-view">
    <h1 class="font-heading text-2xl font-bold text-slate-900">
      {{ isEdit ? 'Sửa tác giả' : 'Thêm tác giả' }}
    </h1>
    <p v-if="isLoading" class="mt-4 font-body text-slate-600">Loading…</p>
    <form v-else class="mt-6 max-w-md space-y-4" @submit.prevent="handleSubmit">
      <div>
        <label for="author-fullName" class="block font-body text-sm font-medium text-slate-700">
          Full name
        </label>
        <input
          id="author-fullName"
          v-model="fullName"
          type="text"
          required
          class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
        />
      </div>
      <div>
        <label for="author-bio" class="block font-body text-sm font-medium text-slate-700">
          Bio
        </label>
        <textarea
          id="author-bio"
          v-model="bio"
          rows="4"
          class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
        />
      </div>
      <p v-if="errorMessage" class="font-body text-sm text-red-600">
        {{ errorMessage }}
      </p>
      <div class="flex gap-3">
        <button
          type="submit"
          class="clay-btn bg-primary px-4 py-2 font-body font-medium text-white disabled:opacity-50"
          :disabled="isSubmitting || !fullName.trim()"
        >
          {{ isSubmitting ? 'Saving…' : 'Save' }}
        </button>
        <router-link
          :to="ROUTES.ADMIN_AUTHORS"
          class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body font-medium text-slate-700"
        >
          Cancel
        </router-link>
      </div>
    </form>
  </div>
</template>
