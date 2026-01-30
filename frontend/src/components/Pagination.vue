<script setup>
const props = defineProps({
  page: {
    type: Number,
    required: true,
  },
  totalPages: {
    type: Number,
    required: true,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['change'])

const goPrevious = () => {
  emit('change', Math.max(0, props.page - 1))
}

const goNext = () => {
  emit('change', Math.min(props.totalPages - 1, props.page + 1))
}
</script>

<template>
  <div v-if="totalPages > 1" class="pagination flex items-center gap-4">
    <button
      type="button"
      class="pagination_prev clay-btn border border-slate-300 bg-white px-4 py-2 font-body text-slate-700 disabled:opacity-50"
      :disabled="disabled || page <= 0"
      @click="goPrevious"
    >
      Previous
    </button>
    <span class="pagination_info font-body text-sm text-slate-600">
      Page {{ page + 1 }} of {{ totalPages }}
    </span>
    <button
      type="button"
      class="pagination_next clay-btn border border-slate-300 bg-white px-4 py-2 font-body text-slate-700 disabled:opacity-50"
      :disabled="disabled || page >= totalPages - 1"
      @click="goNext"
    >
      Next
    </button>
  </div>
</template>
