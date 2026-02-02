<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import CtaSection from '../components/CtaSection.vue'
import { ROUTES } from '../constants/routes'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const isSubmitting = ref(false)
const errorMessage = ref('')

const isFormValid = computed(() => {
  return email.value.trim() !== '' && password.value.length > 0
})

const handleSubmit = async () => {
  if (!isFormValid.value || isSubmitting.value) return
  errorMessage.value = ''
  isSubmitting.value = true
  try {
    await authStore.login({
      email: email.value.trim(),
      password: password.value,
    })
    const redirect = route.query.redirect
    router.push(typeof redirect === 'string' && redirect ? redirect : ROUTES.HOME)
  } catch (err) {
    const msg = err.data?.message || err.message || 'Login failed.'
    errorMessage.value = (err.status === 400 && (msg.includes('Validation') || msg.includes('Email')))
      ? 'Vui lòng nhập đúng định dạng email (ví dụ: user1@example.com).'
      : msg
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <main>
    <section class="px-4 py-16 md:px-6 md:py-24" aria-labelledby="login-heading">
      <div class="mx-auto max-w-md">
        <h1 id="login-heading" class="font-heading text-3xl font-bold text-slate-900">
          Log in
        </h1>
        <p class="mt-2 font-body text-slate-600">
          Sign in to borrow books and manage your loans.
        </p>

        <form
          class="mt-8 space-y-4"
          novalidate
          @submit.prevent="handleSubmit"
        >
          <div>
            <label for="login-email" class="block font-body text-sm font-medium text-slate-700">
              Email
            </label>
            <input
              id="login-email"
              v-model="email"
              type="email"
              required
              autocomplete="email"
              class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
            />
          </div>
          <div>
            <label for="login-password" class="block font-body text-sm font-medium text-slate-700">
              Password
            </label>
            <input
              id="login-password"
              v-model="password"
              type="password"
              required
              autocomplete="current-password"
              class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900 shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
            />
          </div>
          <p v-if="errorMessage" class="font-body text-sm text-red-600">
            {{ errorMessage }}
          </p>
          <button
            type="submit"
            :disabled="!isFormValid || isSubmitting"
            class="clay-btn w-full bg-primary px-4 py-2.5 font-body font-medium text-white disabled:opacity-50"
          >
            {{ isSubmitting ? 'Signing in…' : 'Sign in' }}
          </button>
        </form>
      </div>
    </section>
    <CtaSection />
  </main>
</template>
