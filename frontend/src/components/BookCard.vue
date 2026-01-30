<script setup>
import { RouterLink } from 'vue-router'
import { computed } from 'vue'

const props = defineProps({
  book: {
    type: Object,
    required: true,
  },
  to: {
    type: [Object, String],
    default: null,
  },
})

const authorNames = computed(() => {
  if (!props.book.authors?.length) return '—'
  return props.book.authors.map((a) => a.fullName).join(', ')
})

const linkTo = computed(() => {
  if (props.to) return props.to
  return { name: 'bookDetail', params: { id: props.book.id } }
})
</script>

<template>
  <RouterLink
    :to="linkTo"
    class="book-card clay-card block h-full p-5 transition-colors duration-200 hover:border-primary/50"
  >
    <div class="book-card_icon mb-3 flex h-12 w-12 items-center justify-center rounded-clay bg-clay-blue/60">
      <svg class="h-6 w-6 text-primary" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
        <path d="M18 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zM6 4h5v8l-2.5-1.5L6 12V4z" />
      </svg>
    </div>
    <h2 class="book-card_title font-heading font-semibold text-slate-900">{{ book.title }}</h2>
    <p class="book-card_authors mt-1 font-body text-sm text-slate-600">{{ authorNames }}</p>
    <p class="book-card_meta mt-1 font-body text-xs text-slate-500">{{ book.isbn }} · {{ book.publicationYear }}</p>
  </RouterLink>
</template>
