<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import { RouterLink } from 'vue-router'
import { getReviewsByUserId, updateReview, deleteReview } from '../api'

const route = useRoute()
const reviews = ref([])
const isLoading = ref(false)
const errorMessage = ref('')
const editingId = ref(null)
const editRating = ref(5)
const editComment = ref('')
const isSubmitting = ref(false)
const isDeleting = ref(null)

const userId = computed(() => {
  const id = route.query.userId
  if (id) {
    const num = Number(id)
    return Number.isFinite(num) ? num : null
  }
  return null
})

const loadReviews = async () => {
  if (userId.value == null) return
  isLoading.value = true
  errorMessage.value = ''
  try {
    reviews.value = await getReviewsByUserId(userId.value)
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load reviews.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

const startEdit = (review) => {
  editingId.value = review.id
  editRating.value = review.rating
  editComment.value = review.comment ?? ''
}

const cancelEdit = () => {
  editingId.value = null
  editRating.value = 5
  editComment.value = ''
}

const handleUpdate = async () => {
  if (editingId.value == null || userId.value == null || isSubmitting.value) return
  isSubmitting.value = true
  errorMessage.value = ''
  try {
    await updateReview(
      editingId.value,
      { rating: editRating.value, comment: editComment.value.trim() || null },
      userId.value
    )
    await loadReviews()
    cancelEdit()
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to update review.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

const handleDelete = async (reviewId) => {
  if (userId.value == null || isDeleting.value != null) return
  if (!window.confirm('Delete this review?')) return
  isDeleting.value = reviewId
  errorMessage.value = ''
  try {
    await deleteReview(reviewId, userId.value, false)
    await loadReviews()
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to delete review.'
    console.error(err)
  } finally {
    isDeleting.value = null
  }
}

onMounted(loadReviews)
watch(userId, loadReviews)
</script>

<template>
  <main>
    <section class="px-4 py-16 md:px-6 md:py-24" aria-labelledby="reviews-heading">
      <div class="mx-auto max-w-4xl">
        <h1 id="reviews-heading" class="font-heading text-3xl font-bold text-slate-900 md:text-4xl">
          My Reviews
        </h1>
        <p class="mt-2 max-w-2xl font-body text-slate-600">
          Reviews you wrote. Use <code class="rounded bg-slate-100 px-1">?userId=1</code> to select a user (no auth yet).
        </p>

        <div v-if="userId == null" class="mt-10 clay-card max-w-2xl p-6">
          <p class="font-body text-slate-600">
            Add <code class="rounded bg-slate-100 px-1">?userId=1</code> to the URL to see reviews for that user.
          </p>
        </div>

        <template v-else>
          <p v-if="errorMessage" class="mt-4 font-body text-sm text-red-600">
            {{ errorMessage }}
          </p>
          <p v-if="isLoading" class="mt-4 font-body text-slate-600">Loading…</p>
          <ul v-else class="mt-6 space-y-4" role="list">
            <li v-for="review in reviews" :key="review.id" class="clay-card p-4">
              <div v-if="editingId === review.id" class="space-y-3">
                <p class="font-body font-semibold text-slate-900">{{ review.bookTitle }}</p>
                <div>
                  <label for="edit-rating" class="block font-body text-sm text-slate-700">Rating (1–5)</label>
                  <input
                    id="edit-rating"
                    v-model.number="editRating"
                    type="number"
                    min="1"
                    max="5"
                    class="mt-1 block w-20 rounded-clay border border-slate-300 px-2 py-1 font-body"
                  />
                </div>
                <div>
                  <label for="edit-comment" class="block font-body text-sm text-slate-700">Comment</label>
                  <textarea
                    id="edit-comment"
                    v-model="editComment"
                    rows="3"
                    class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body"
                  />
                </div>
                <div class="flex gap-2">
                  <button
                    type="button"
                    class="clay-btn bg-primary px-3 py-1.5 font-body text-sm text-white"
                    :disabled="isSubmitting"
                    @click="handleUpdate"
                  >
                    {{ isSubmitting ? 'Saving…' : 'Save' }}
                  </button>
                  <button
                    type="button"
                    class="clay-btn border border-slate-300 bg-white px-3 py-1.5 font-body text-sm text-slate-700"
                    :disabled="isSubmitting"
                    @click="cancelEdit"
                  >
                    Cancel
                  </button>
                </div>
              </div>
              <div v-else>
                <RouterLink
                  :to="{ name: 'bookDetail', params: { id: review.bookId } }"
                  class="font-body font-semibold text-slate-900 hover:text-primary hover:underline"
                >
                  {{ review.bookTitle }}
                </RouterLink>
                <p class="mt-1 font-body text-sm text-slate-600">
                  Rating: {{ review.rating }}/5 · {{ review.createdAt }}
                </p>
                <p v-if="review.comment" class="mt-2 font-body text-slate-700">
                  {{ review.comment }}
                </p>
                <div class="mt-3 flex gap-2">
                  <button
                    type="button"
                    class="clay-btn border border-slate-300 bg-white px-3 py-1.5 font-body text-sm text-slate-700"
                    @click="startEdit(review)"
                  >
                    Edit
                  </button>
                  <button
                    type="button"
                    class="clay-btn border border-red-200 bg-white px-3 py-1.5 font-body text-sm text-red-700"
                    :disabled="isDeleting === review.id"
                    @click="handleDelete(review.id)"
                  >
                    {{ isDeleting === review.id ? 'Deleting…' : 'Delete' }}
                  </button>
                </div>
              </div>
            </li>
          </ul>
          <p v-if="!isLoading && reviews.length === 0" class="mt-6 font-body text-slate-600">
            No reviews yet. Leave a review on a book detail page.
          </p>
        </template>
      </div>
    </section>
  </main>
</template>
