<script setup>
import { ref, computed } from 'vue'
import CtaSection from '../components/CtaSection.vue'
import { registerUser } from '../api'
const email = ref('')
const password = ref('')
const fullName = ref('')
const isSubmitting = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const isFormValid = computed(() => {
  return (
    email.value.trim() !== '' &&
    password.value.trim().length >= 8 &&
    fullName.value.trim() !== ''
  )
})

const handleSubmit = async () => {
  if (!isFormValid.value || isSubmitting.value) return
  errorMessage.value = ''
  successMessage.value = ''
  isSubmitting.value = true
  try {
    await registerUser({
      email: email.value.trim(),
      password: password.value,
      fullName: fullName.value.trim(),
    })
    successMessage.value = 'Registration successful. You can now use the library.'
    email.value = ''
    password.value = ''
    fullName.value = ''
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Registration failed.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <main>
    <section class="px-4 py-16 md:px-6 md:py-24" aria-labelledby="join-heading">
      <div class="mx-auto max-w-md">
        <h1 id="join-heading" class="font-heading text-3xl font-bold text-slate-900">
          Join the Library
        </h1>
        <p class="mt-2 font-body text-slate-600">
          Create an account to borrow books and leave reviews.
        </p>

        <form
          class="mt-8 space-y-4"
          novalidate
          @submit.prevent="handleSubmit"
        >
          <div>
            <label for="join-email" class="block font-body text-sm font-medium text-slate-700">
              Email
            </label>
            <input
              id="join-email"
              v-model="email"
              type="email"
              required
              autocomplete="email"
              class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
            />
          </div>
          <div>
            <label for="join-password" class="block font-body text-sm font-medium text-slate-700">
              Password (min 8 characters)
            </label>
            <input
              id="join-password"
              v-model="password"
              type="password"
              required
              minlength="8"
              autocomplete="new-password"
              class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
            />
          </div>
          <div>
            <label for="join-fullName" class="block font-body text-sm font-medium text-slate-700">
              Full name
            </label>
            <input
              id="join-fullName"
              v-model="fullName"
              type="text"
              required
              autocomplete="name"
              class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
            />
          </div>
          <p v-if="errorMessage" class="font-body text-sm text-red-600">
            {{ errorMessage }}
          </p>
          <p v-if="successMessage" class="font-body text-sm text-green-600">
            {{ successMessage }}
          </p>
          <button
            type="submit"
            :disabled="!isFormValid || isSubmitting"
            class="clay-btn w-full bg-primary px-4 py-2.5 font-body font-medium text-white disabled:opacity-50"
          >
            {{ isSubmitting ? 'Registering…' : 'Register' }}
          </button>
        </form>
      </div>
    </section>
    <CtaSection />
  </main>
</template>
