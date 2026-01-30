<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getPublisherById, createPublisher, updatePublisher } from '../../api'
import { ROUTES } from '../../constants/routes'

const route = useRoute()
const router = useRouter()
const name = ref('')
const website = ref('')
const address = ref('')
const isLoading = ref(true)
const isSubmitting = ref(false)
const errorMessage = ref('')

const isEdit = computed(() => Boolean(route.params.id && route.params.id !== 'new'))
const publisherId = computed(() => (isEdit.value ? Number(route.params.id) : null))

const loadPublisher = async () => {
  if (!isEdit.value) {
    isLoading.value = false
    return
  }
  isLoading.value = true
  errorMessage.value = ''
  try {
    const publisher = await getPublisherById(publisherId.value)
    name.value = publisher.name ?? ''
    website.value = publisher.website ?? ''
    address.value = publisher.address ?? ''
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load publisher.'
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
      await updatePublisher(publisherId.value, {
        name: name.value.trim(),
        website: website.value.trim() || null,
        address: address.value.trim() || null,
      })
    } else {
      await createPublisher({
        name: name.value.trim(),
        website: website.value.trim() || null,
        address: address.value.trim() || null,
      })
    }
    router.push(ROUTES.ADMIN_PUBLISHERS)
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to save.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

onMounted(loadPublisher)
</script>

<template>
  <div class="admin-publisher-form-view">
    <h1 class="font-heading text-2xl font-bold text-slate-900">
      {{ isEdit ? 'Sửa NXB' : 'Thêm NXB' }}
    </h1>
    <p v-if="isLoading" class="mt-4 font-body text-slate-600">Loading…</p>
    <form v-else class="mt-6 max-w-md space-y-4" @submit.prevent="handleSubmit">
      <div>
        <label for="publisher-name" class="block font-body text-sm font-medium text-slate-700">
          Name
        </label>
        <input
          id="publisher-name"
          v-model="name"
          type="text"
          required
          class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
        />
      </div>
      <div>
        <label for="publisher-website" class="block font-body text-sm font-medium text-slate-700">
          Website
        </label>
        <input
          id="publisher-website"
          v-model="website"
          type="url"
          class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
        />
      </div>
      <div>
        <label for="publisher-address" class="block font-body text-sm font-medium text-slate-700">
          Address
        </label>
        <input
          id="publisher-address"
          v-model="address"
          type="text"
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
          :to="ROUTES.ADMIN_PUBLISHERS"
          class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body font-medium text-slate-700"
        >
          Cancel
        </RouterLink>
      </div>
    </form>
  </div>
</template>
