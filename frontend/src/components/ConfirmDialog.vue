<script setup>
defineProps({
  isOpen: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: 'Confirm',
  },
  message: {
    type: String,
    default: 'Are you sure?',
  },
  confirmLabel: {
    type: String,
    default: 'Confirm',
  },
  cancelLabel: {
    type: String,
    default: 'Cancel',
  },
  isDanger: {
    type: Boolean,
    default: false,
  },
  isLoading: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['confirm', 'cancel'])

const handleConfirm = () => {
  emit('confirm')
}

const handleCancel = () => {
  emit('cancel')
}
</script>

<template>
  <Teleport to="body">
    <div
      v-if="isOpen"
      class="confirm-dialog fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4"
      role="dialog"
      aria-modal="true"
      :aria-labelledby="title ? 'confirm-dialog-title' : undefined"
    >
      <div class="confirm-dialog_content max-w-md rounded-clay border border-slate-200 bg-white p-6 shadow-lg">
        <h2 id="confirm-dialog-title" class="confirm-dialog_title font-heading text-lg font-semibold text-slate-900">
          {{ title }}
        </h2>
        <p class="confirm-dialog_message mt-2 font-body text-slate-700">
          {{ message }}
        </p>
        <div class="confirm-dialog_actions mt-6 flex justify-end gap-3">
          <button
            type="button"
            class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body font-medium text-slate-700"
            :disabled="isLoading"
            @click="handleCancel"
          >
            {{ cancelLabel }}
          </button>
          <button
            type="button"
            :class="[
              'clay-btn px-4 py-2 font-body font-medium text-white',
              isDanger ? 'bg-red-600 hover:bg-red-700' : 'bg-primary',
            ]"
            :disabled="isLoading"
            @click="handleConfirm"
          >
            {{ isLoading ? '…' : confirmLabel }}
          </button>
        </div>
      </div>
    </div>
  </Teleport>
</template>
