<script setup>
import { RouterLink, useRouter } from 'vue-router'
import { ROUTES } from '../constants/routes'

const router = useRouter()

const goToJoin = () => {
  router.push(ROUTES.JOIN)
}
</script>

<template>
  <section id="hero" class="relative overflow-hidden px-4 pb-16 pt-8 md:px-6 md:pb-24 md:pt-12" aria-labelledby="hero-heading">
    <div class="mx-auto max-w-6xl">
      <div class="grid gap-8 lg:grid-cols-2 lg:items-center lg:gap-12">
        <div class="text-center lg:text-left">
          <h1 id="hero-heading" class="font-heading text-4xl font-bold leading-tight text-slate-900 md:text-5xl lg:text-6xl">
            Your next great read is waiting
          </h1>
          <p class="mt-4 max-w-xl font-body text-lg text-slate-600 md:mx-0 md:mt-6">
            Browse thousands of books, track your loans, and join a community of readers. Library Hub makes borrowing simple and fun.
          </p>
          <div class="mt-8 flex flex-wrap items-center justify-center gap-4 lg:justify-start">
            <button
              type="button"
              class="clay-btn bg-primary px-8 py-4 text-lg text-white"
              @click="goToJoin"
            >
              Join the Library
            </button>
            <RouterLink
              :to="ROUTES.BOOKS"
              class="clay-card inline-flex items-center gap-2 px-6 py-4 font-body font-semibold text-slate-700"
            >
              <svg class="h-5 w-5 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
              </svg>
              Browse catalog
            </RouterLink>
          </div>
        </div>
        <div class="relative flex justify-center">
          <div class="clay-card flex h-64 w-64 items-center justify-center bg-clay-peach/80 md:h-80 md:w-80">
            <svg class="h-40 w-40 text-primary/80 md:h-48 md:w-48" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
              <path d="M18 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zM6 4h5v8l-2.5-1.5L6 12V4z" />
            </svg>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
