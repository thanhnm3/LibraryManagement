<script setup>
import { RouterLink } from 'vue-router'
import { ROUTES } from '../constants/routes'

const loans = [
  { id: 1, title: 'The Great Gatsby', dueDate: '2025-02-15', status: 'active' },
  { id: 2, title: 'Atomic Habits', dueDate: '2025-02-20', status: 'active' },
  { id: 3, title: 'Educated', dueDate: '2025-01-28', status: 'overdue' },
]

const getStatusLabel = (status) => (status === 'overdue' ? 'Overdue' : 'On loan')
const getStatusClass = (status) => (status === 'overdue' ? 'bg-red-100 text-red-800' : 'bg-clay-mint/60 text-slate-700')
</script>

<template>
  <section id="my-loans" class="px-4 py-16 md:px-6 md:py-24" aria-labelledby="my-loans-heading">
    <div class="mx-auto max-w-6xl">
      <h2 id="my-loans-heading" class="font-heading text-3xl font-bold text-slate-900 md:text-4xl">
        My loans
      </h2>
      <p class="mt-2 max-w-2xl font-body text-slate-600">
        A quick peek at your current borrows. Sign in to see and manage all your loans.
      </p>

      <div class="clay-card mt-10 max-w-2xl overflow-hidden p-0">
        <div class="border-b border-slate-200 bg-clay-lilac/30 px-4 py-3 md:px-6">
          <h3 class="font-heading text-lg font-semibold text-slate-800">Borrow status (demo)</h3>
        </div>
        <ul class="divide-y divide-slate-200" role="list">
          <li
            v-for="loan in loans"
            :key="loan.id"
            class="flex cursor-pointer flex-col gap-2 px-4 py-4 transition-colors duration-200 hover:bg-slate-50/80 md:flex-row md:items-center md:justify-between md:px-6"
          >
            <div class="flex items-center gap-3">
              <div class="flex h-10 w-10 shrink-0 items-center justify-center rounded-clay bg-clay-blue/50">
                <svg class="h-5 w-5 text-primary" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M18 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zM6 4h5v8l-2.5-1.5L6 12V4z" />
                </svg>
              </div>
              <div>
                <p class="font-body font-semibold text-slate-900">{{ loan.title }}</p>
                <p class="font-body text-sm text-slate-600">Due {{ loan.dueDate }}</p>
              </div>
            </div>
            <span
              :class="['inline-flex w-fit rounded-full px-3 py-1 font-body text-sm font-medium', getStatusClass(loan.status)]"
            >
              {{ getStatusLabel(loan.status) }}
            </span>
          </li>
        </ul>
        <div class="border-t border-slate-200 bg-slate-50/50 px-4 py-3 text-center md:px-6">
          <RouterLink
            :to="ROUTES.JOIN"
            class="font-body text-sm font-medium text-primary transition-colors duration-200 hover:text-primary-dark hover:underline focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
          >
            Sign in to manage all loans
          </RouterLink>
        </div>
      </div>
    </div>
  </section>
</template>
