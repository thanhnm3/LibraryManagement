<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCategoryById, createCategory, updateCategory } from '../../api'
import { ROUTES } from '../../constants/routes'

const route = useRoute()
const router = useRouter()
const name = ref('')
const description = ref('')
const isLoading = ref(true)
const isSubmitting = ref(false)
const errorMessage = ref('')

const isEdit = computed(() => Boolean(route.params.id && route.params.id !== 'new'))
const categoryId = computed(() => (isEdit.value ? Number(route.params.id) : null))

const loadCategory = async () => {
  if (!isEdit.value) {
    isLoading.value = false
    return
  }
  isLoading.value = true
  errorMessage.value = ''
  try {
    const category = await getCategoryById(categoryId.value)
    name.value = category.name ?? ''
    description.value = category.description ?? ''
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load category.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

const handleSubmit = async () => {
  if (!name.value.trim()) return
  isSubmitting.value = true
  errorMessage.value = ''
  try {
    if (isEdit.value) {
      await updateCategory(categoryId.value, {
        name: name.value.trim(),
        description: description.value.trim() || null,
      })
    } else {
      await createCategory({
        name: name.value.trim(),
        description: description.value.trim() || null,
      })
    }
    router.push(ROUTES.ADMIN_CATEGORIES)
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to save.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

onMounted(loadCategory)
</script>

<template>
  <div class="admin-category-form-view">
    <h1 class="font-heading text-2xl font-bold text-slate-900">
      {{ isEdit ? 'Sửa danh mục' : 'Thêm danh mục' }}
    </h1>
    <p v-if="isLoading" class="mt-4 font-body text-slate-600">Loading…</p>
    <form v-else class="mt-6 max-w-md space-y-4" @submit.prevent="handleSubmit">
      <div>
        <label for="category-name" class="block font-body text-sm font-medium text-slate-700">
          Name
        </label>
        <input
          id="category-name"
          v-model="name"
          type="text"
          required
          class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
        />
      </div>
      <div>
        <label for="category-description" class="block font-body text-sm font-medium text-slate-700">
          Description
        </label>
        <textarea
          id="category-description"
          v-model="description"
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
          :disabled="isSubmitting || !name.trim()"
        >
          {{ isSubmitting ? 'Saving…' : 'Save' }}
        </button>
        <RouterLink
          :to="ROUTES.ADMIN_CATEGORIES"
          class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body font-medium text-slate-700"
        >
          Cancel
        </RouterLink>
      </div>
    </form>
  </div>
</template>
