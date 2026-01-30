<script setup>
import { ref } from 'vue'
import { RouterLink } from 'vue-router'
import { ROUTES } from '../constants/routes'

const isMenuOpen = ref(false)

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
}

const closeMenu = () => {
  isMenuOpen.value = false
}
</script>

<template>
  <header
    class="sticky top-4 left-4 right-4 z-50 mx-auto max-w-7xl rounded-clay-lg border-[3px] border-white/80 bg-white/90 px-4 py-3 shadow-clay backdrop-blur-sm md:px-6"
    role="banner"
  >
    <nav class="flex items-center justify-between gap-4" aria-label="Main navigation">
      <RouterLink
        :to="ROUTES.HOME"
        class="flex items-center gap-2 font-heading text-xl font-bold text-primary transition-colors duration-200 hover:text-primary-dark focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
      >
        <svg class="h-8 w-8 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
        </svg>
        <span>Library Hub</span>
      </RouterLink>

      <ul class="hidden items-center gap-6 md:flex">
        <li>
          <RouterLink
            :to="ROUTES.BOOKS"
            class="font-body font-medium text-slate-700 transition-colors duration-200 hover:text-primary focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
          >
            Catalog
          </RouterLink>
        </li>
        <li>
          <RouterLink
            :to="ROUTES.SEARCH"
            class="font-body font-medium text-slate-700 transition-colors duration-200 hover:text-primary focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
          >
            Search
          </RouterLink>
        </li>
        <li>
          <RouterLink
            :to="ROUTES.LOANS"
            class="font-body font-medium text-slate-700 transition-colors duration-200 hover:text-primary focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
          >
            My Loans
          </RouterLink>
        </li>
        <li>
          <RouterLink
            :to="ROUTES.REVIEWS"
            class="font-body font-medium text-slate-700 transition-colors duration-200 hover:text-primary focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
          >
            Reviews
          </RouterLink>
        </li>
        <li>
          <RouterLink :to="ROUTES.ADMIN" class="font-body font-medium text-slate-700 transition-colors duration-200 hover:text-primary focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2">
            Admin
          </RouterLink>
        </li>
        <li>
          <RouterLink :to="ROUTES.JOIN" class="clay-btn inline-block bg-primary px-5 py-2.5 text-white">
            Join the Library
          </RouterLink>
        </li>
      </ul>

      <button
        type="button"
        class="flex h-10 w-10 items-center justify-center rounded-clay border-2 border-slate-200 bg-white text-slate-700 transition-colors duration-200 hover:border-primary hover:text-primary focus:outline-none focus:ring-2 focus:ring-primary md:hidden"
        aria-controls="mobile-menu"
        :aria-expanded="isMenuOpen"
        @click="toggleMenu"
      >
        <span class="sr-only">Open menu</span>
        <svg v-if="!isMenuOpen" class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
        </svg>
        <svg v-else class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>
    </nav>

    <div
      id="mobile-menu"
      class="mt-4 border-t border-slate-200 pt-4 md:hidden"
      :class="{ hidden: !isMenuOpen }"
      role="dialog"
      aria-label="Mobile menu"
    >
      <ul class="flex flex-col gap-3">
        <li>
          <RouterLink :to="ROUTES.BOOKS" class="block py-2 font-body font-medium text-slate-700" @click="closeMenu">
            Catalog
          </RouterLink>
        </li>
        <li>
          <RouterLink :to="ROUTES.SEARCH" class="block py-2 font-body font-medium text-slate-700" @click="closeMenu">
            Search
          </RouterLink>
        </li>
        <li>
          <RouterLink :to="ROUTES.LOANS" class="block py-2 font-body font-medium text-slate-700" @click="closeMenu">
            My Loans
          </RouterLink>
        </li>
        <li>
          <RouterLink :to="ROUTES.REVIEWS" class="block py-2 font-body font-medium text-slate-700" @click="closeMenu">
            Reviews
          </RouterLink>
        </li>
        <li>
          <RouterLink :to="ROUTES.ADMIN" class="block py-2 font-body font-medium text-slate-700" @click="closeMenu">
            Admin
          </RouterLink>
        </li>
        <li>
          <RouterLink
            :to="ROUTES.JOIN"
            class="clay-btn mt-2 inline-block bg-primary px-5 py-2.5 text-center text-white"
            @click="closeMenu"
          >
            Join the Library
          </RouterLink>
        </li>
      </ul>
    </div>
  </header>
</template>
