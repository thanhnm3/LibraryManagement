<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import { RouterLink } from 'vue-router'
import { getBookById, getReviewsByBookId, getAverageRatingByBookId, createReview, updateReview, deleteReview } from '../api'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const authStore = useAuthStore()
const bookId = computed(() => Number(route.params.id))
const book = ref(null)
const reviews = ref([])
const averageRating = ref(null)
const isLoading = ref(true)
const errorMessage = ref('')
const reviewPage = ref(0)
const reviewPageSize = 20
const totalReviewPages = ref(0)
const showAddReview = ref(false)
const addRating = ref(5)
const addComment = ref('')
const editingReviewId = ref(null)
const editRating = ref(5)
const editComment = ref('')
const isSubmitting = ref(false)
const isDeleting = ref(null)

const userId = computed(() => authStore.user?.id ?? null)

const loadBook = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    book.value = await getBookById(bookId.value)
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load book.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

const loadReviews = async () => {
  try {
    const res = await getReviewsByBookId(bookId.value, reviewPage.value, reviewPageSize)
    reviews.value = res.content || []
    totalReviewPages.value = res.totalPages ?? 0
  } catch (err) {
    console.error(err)
    reviews.value = []
  }
}

const loadAverageRating = async () => {
  try {
    averageRating.value = await getAverageRatingByBookId(bookId.value)
  } catch (err) {
    console.error(err)
    averageRating.value = null
  }
}

const loadData = async () => {
  await loadBook()
  if (book.value) {
    await Promise.all([loadReviews(), loadAverageRating()])
  }
}

const authorNames = computed(() => {
  if (!book.value?.authors?.length) return '—'
  return book.value.authors.map((a) => a.fullName).join(', ')
})

const categoryNames = computed(() => {
  if (!book.value?.categories?.length) return '—'
  return book.value.categories.map((c) => c.name).join(', ')
})

const handleAddReview = async () => {
  if (userId.value == null || isSubmitting.value) return
  isSubmitting.value = true
  errorMessage.value = ''
  try {
    await createReview({
      userId: userId.value,
      bookId: bookId.value,
      rating: addRating.value,
      comment: addComment.value.trim() || null,
    })
    showAddReview.value = false
    addRating.value = 5
    addComment.value = ''
    await Promise.all([loadReviews(), loadAverageRating()])
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to add review.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

const startEditReview = (review) => {
  editingReviewId.value = review.id
  editRating.value = review.rating
  editComment.value = review.comment ?? ''
}

const cancelEditReview = () => {
  editingReviewId.value = null
}

const handleUpdateReview = async () => {
  if (editingReviewId.value == null || isSubmitting.value) return
  isSubmitting.value = true
  errorMessage.value = ''
  try {
    await updateReview(editingReviewId.value, {
      rating: editRating.value,
      comment: editComment.value.trim() || null,
    })
    cancelEditReview()
    await Promise.all([loadReviews(), loadAverageRating()])
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to update review.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

const handleDeleteReview = async (reviewId) => {
  if (isDeleting.value != null) return
  if (!window.confirm('Delete this review?')) return
  isDeleting.value = reviewId
  errorMessage.value = ''
  try {
    await deleteReview(reviewId)
    await Promise.all([loadReviews(), loadAverageRating()])
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to delete review.'
    console.error(err)
  } finally {
    isDeleting.value = null
  }
}

const borrowLink = computed(() => {
  const path = '/loans/new'
  const q = new URLSearchParams()
  q.set('bookId', String(bookId.value))
  return `${path}?${q.toString()}`
})

onMounted(loadData)
watch(bookId, loadData)
watch(reviewPage, loadReviews)
</script>

<template>
  <div class="book-detail-view px-4 py-16 md:px-6 md:py-24">
    <div class="mx-auto max-w-4xl">
      <p v-if="isLoading" class="font-body text-slate-600">Loading…</p>
      <p v-else-if="errorMessage && !book" class="font-body text-red-600">
        {{ errorMessage }}
      </p>
      <template v-else-if="book">
        <h1 class="font-heading text-3xl font-bold text-slate-900">
          {{ book.title }}
        </h1>
        <p class="mt-2 font-body text-slate-600">
          ISBN: {{ book.isbn }} · {{ book.publicationYear }}
        </p>
        <p class="mt-1 font-body text-slate-600">
          Authors: {{ authorNames }} · Categories: {{ categoryNames }}
        </p>
        <p v-if="book.publisher" class="mt-1 font-body text-slate-600">
          Publisher: {{ book.publisher.name }}
        </p>
        <p v-if="book.description" class="mt-4 font-body text-slate-700">
          {{ book.description }}
        </p>

        <div v-if="averageRating" class="mt-6 clay-card p-4">
          <p class="font-body text-sm text-slate-600">Average rating</p>
          <p class="font-body font-semibold text-slate-900">
            {{ averageRating.averageRating?.toFixed(1) ?? '—' }} / 5 ({{ averageRating.totalReviews ?? 0 }} reviews)
          </p>
        </div>

        <div class="mt-6 flex flex-wrap gap-3">
          <RouterLink
            :to="borrowLink"
            class="clay-btn bg-primary px-4 py-2 font-body font-medium text-white"
          >
            Borrow this book
          </RouterLink>
        </div>

        <section class="mt-10" aria-labelledby="reviews-heading">
          <h2 id="reviews-heading" class="font-heading text-xl font-semibold text-slate-900">
            Reviews
          </h2>
          <p v-if="errorMessage" class="mt-2 font-body text-sm text-red-600">
            {{ errorMessage }}
          </p>
          <div v-if="userId != null && !showAddReview" class="mt-3">
            <button
              type="button"
              class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body text-slate-700"
              @click="showAddReview = true"
            >
              Add review
            </button>
          </div>
          <div v-if="userId != null && showAddReview" class="mt-4 clay-card p-4">
            <p class="font-body font-medium text-slate-800">Add a review</p>
            <div class="mt-2 space-y-2">
              <label for="add-rating" class="block font-body text-sm text-slate-700">Rating (1–5)</label>
              <input
                id="add-rating"
                v-model.number="addRating"
                type="number"
                min="1"
                max="5"
                class="block w-20 rounded-clay border border-slate-300 px-2 py-1 font-body"
              />
              <label for="add-comment" class="block font-body text-sm text-slate-700">Comment</label>
              <textarea
                id="add-comment"
                v-model="addComment"
                rows="3"
                class="block w-full rounded-clay border border-slate-300 px-3 py-2 font-body"
              />
            </div>
            <div class="mt-3 flex gap-2">
              <button
                type="button"
                class="clay-btn bg-primary px-4 py-2 font-body text-white"
                :disabled="isSubmitting"
                @click="handleAddReview"
              >
                {{ isSubmitting ? 'Submitting…' : 'Submit' }}
              </button>
              <button
                type="button"
                class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body text-slate-700"
                :disabled="isSubmitting"
                @click="showAddReview = false"
              >
                Cancel
              </button>
            </div>
          </div>
          <p v-if="userId == null" class="mt-2 font-body text-sm text-slate-600">
            Log in to add a review.
          </p>

          <ul class="mt-6 space-y-4" role="list">
            <li v-for="review in reviews" :key="review.id" class="clay-card p-4">
              <div v-if="editingReviewId === review.id" class="space-y-3">
                <p class="font-body font-medium text-slate-800">{{ review.userFullName }}</p>
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
                    @click="handleUpdateReview"
                  >
                    {{ isSubmitting ? 'Saving…' : 'Save' }}
                  </button>
                  <button
                    type="button"
                    class="clay-btn border border-slate-300 bg-white px-3 py-1.5 font-body text-sm text-slate-700"
                    :disabled="isSubmitting"
                    @click="cancelEditReview"
                  >
                    Cancel
                  </button>
                </div>
              </div>
              <div v-else>
                <p class="font-body font-medium text-slate-800">{{ review.userFullName }}</p>
                <p class="mt-1 font-body text-sm text-slate-600">
                  {{ review.rating }}/5 · {{ review.createdAt }}
                </p>
                <p v-if="review.comment" class="mt-2 font-body text-slate-700">
                  {{ review.comment }}
                </p>
                <div v-if="userId === review.userId" class="mt-3 flex gap-2">
                  <button
                    type="button"
                    class="clay-btn border border-slate-300 bg-white px-3 py-1.5 font-body text-sm text-slate-700"
                    @click="startEditReview(review)"
                  >
                    Edit
                  </button>
                  <button
                    type="button"
                    class="clay-btn border border-red-200 bg-white px-3 py-1.5 font-body text-sm text-red-700"
                    :disabled="isDeleting === review.id"
                    @click="handleDeleteReview(review.id)"
                  >
                    {{ isDeleting === review.id ? 'Deleting…' : 'Delete' }}
                  </button>
                </div>
              </div>
            </li>
          </ul>
          <div v-if="totalReviewPages > 1" class="mt-4 flex gap-2">
            <button
              type="button"
              class="clay-btn border border-slate-300 bg-white px-3 py-1.5 font-body text-sm text-slate-700 disabled:opacity-50"
              :disabled="reviewPage <= 0"
              @click="reviewPage = Math.max(0, reviewPage - 1)"
            >
              Previous
            </button>
            <span class="flex items-center font-body text-sm text-slate-600">
              Page {{ reviewPage + 1 }} of {{ totalReviewPages }}
            </span>
            <button
              type="button"
              class="clay-btn border border-slate-300 bg-white px-3 py-1.5 font-body text-sm text-slate-700 disabled:opacity-50"
              :disabled="reviewPage >= totalReviewPages - 1"
              @click="reviewPage = Math.min(totalReviewPages - 1, reviewPage + 1)"
            >
              Next
            </button>
          </div>
        </section>
      </template>
    </div>
  </div>
</template>
