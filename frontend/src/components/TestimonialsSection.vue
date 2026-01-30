<script setup>
const testimonials = [
  {
    id: 1,
    quote: 'Library Hub made finding and borrowing books so easy. I love the clay-style design and how fast I can check my loans.',
    name: 'Alex Chen',
    role: 'Regular reader',
    rating: 5,
  },
  {
    id: 2,
    quote: 'The catalog is huge and the "My Loans" section keeps me on track. No more late fees!',
    name: 'Sam Rivera',
    role: 'Student',
    rating: 5,
  },
  {
    id: 3,
    quote: 'Clean, playful, and responsive. Best library app I have used.',
    name: 'Jordan Lee',
    role: 'Book club member',
    rating: 5,
  },
]
</script>

<template>
  <section id="testimonials" class="px-4 py-16 md:px-6 md:py-24" aria-labelledby="testimonials-heading">
    <div class="mx-auto max-w-6xl">
      <h2 id="testimonials-heading" class="font-heading text-3xl font-bold text-slate-900 md:text-4xl">
        What readers say
      </h2>
      <p class="mt-2 max-w-2xl font-body text-slate-600">
        Real reviews from our community. Join and add your own.
      </p>

      <ul class="mt-10 grid gap-6 sm:grid-cols-2 lg:grid-cols-3" role="list">
        <li v-for="t in testimonials" :key="t.id">
          <article class="clay-card h-full cursor-default p-6">
            <div class="flex gap-1 text-amber-500" aria-label="Rated 5 out of 5 stars">
              <svg v-for="i in 5" :key="i" class="h-5 w-5" fill="currentColor" viewBox="0 0 20 20" aria-hidden="true">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
              </svg>
            </div>
            <blockquote class="mt-4 font-body italic text-slate-600">
              {{ t.quote }}
            </blockquote>
            <footer class="mt-4">
              <p class="font-body font-semibold text-slate-900">{{ t.name }}</p>
              <p class="font-body text-sm text-slate-500">{{ t.role }}</p>
            </footer>
          </article>
        </li>
      </ul>
    </div>
  </section>
</template>
