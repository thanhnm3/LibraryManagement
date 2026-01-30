<script setup>
import { RouterLink } from 'vue-router'
import { ROUTES } from '../constants/routes'

const currentYear = new Date().getFullYear()
</script>

<template>
  <footer class="border-t border-slate-200/80 bg-white/60 px-4 py-8 md:px-6" role="contentinfo">
    <div class="mx-auto max-w-6xl">
      <div class="flex flex-col items-center justify-between gap-4 md:flex-row">
        <RouterLink
          :to="ROUTES.HOME"
          class="flex items-center gap-2 font-heading text-lg font-bold text-primary transition-colors duration-200 hover:text-primary-dark focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
        >
          <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
          </svg>
          Library Hub
        </RouterLink>
        <nav class="flex flex-wrap justify-center gap-6" aria-label="Footer navigation">
          <RouterLink
            :to="ROUTES.BOOKS"
            class="font-body text-sm text-slate-600 transition-colors duration-200 hover:text-primary focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
          >
            Catalog
          </RouterLink>
          <RouterLink
            :to="ROUTES.LOANS"
            class="font-body text-sm text-slate-600 transition-colors duration-200 hover:text-primary focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
          >
            My Loans
          </RouterLink>
          <RouterLink
            :to="ROUTES.REVIEWS"
            class="font-body text-sm text-slate-600 transition-colors duration-200 hover:text-primary focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
          >
            Reviews
          </RouterLink>
          <RouterLink
            :to="ROUTES.JOIN"
            class="font-body text-sm text-slate-600 transition-colors duration-200 hover:text-primary focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
          >
            Join
          </RouterLink>
        </nav>
      </div>
      <p class="mt-6 text-center font-body text-sm text-slate-500">
        &copy; {{ currentYear }} Library Hub. All rights reserved.
      </p>
    </div>
  </footer>
</template>
