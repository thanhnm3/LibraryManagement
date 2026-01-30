<script setup>
const books = [
  { id: 1, title: 'The Great Gatsby', author: 'F. Scott Fitzgerald', category: 'Classic' },
  { id: 2, title: 'To Kill a Mockingbird', author: 'Harper Lee', category: 'Fiction' },
  { id: 3, title: '1984', author: 'George Orwell', category: 'Sci-Fi' },
  { id: 4, title: 'Pride and Prejudice', author: 'Jane Austen', category: 'Romance' },
]

const categories = [
  { name: 'Fiction', count: 1200 },
  { name: 'Non-Fiction', count: 890 },
  { name: 'Science', count: 450 },
  { name: 'Children', count: 680 },
]

const authors = [
  { name: 'Jane Austen', books: 6 },
  { name: 'Charles Dickens', books: 14 },
  { name: 'Agatha Christie', books: 85 },
]
</script>

<template>
  <section id="catalog" class="px-4 py-16 md:px-6 md:py-24" aria-labelledby="catalog-heading">
    <div class="mx-auto max-w-6xl">
      <h2 id="catalog-heading" class="font-heading text-3xl font-bold text-slate-900 md:text-4xl">
        Book catalog
      </h2>
      <p class="mt-2 max-w-2xl font-body text-slate-600">
        Discover books, authors, and categories. Our catalog is updated regularly.
      </p>

      <div class="mt-10 space-y-12">
        <div>
          <h3 class="font-heading text-xl font-semibold text-slate-800">Featured books</h3>
          <ul class="mt-4 grid gap-4 sm:grid-cols-2 lg:grid-cols-4" role="list">
            <li v-for="book in books" :key="book.id">
              <article class="clay-card h-full cursor-pointer p-5">
                <div class="mb-3 flex h-12 w-12 items-center justify-center rounded-clay bg-clay-blue/60">
                  <svg class="h-6 w-6 text-primary" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M18 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zM6 4h5v8l-2.5-1.5L6 12V4z" />
                  </svg>
                </div>
                <h4 class="font-heading font-semibold text-slate-900">{{ book.title }}</h4>
                <p class="mt-1 font-body text-sm text-slate-600">{{ book.author }}</p>
                <span class="mt-2 inline-block rounded-full bg-clay-mint/60 px-2.5 py-0.5 font-body text-xs font-medium text-slate-700">{{ book.category }}</span>
              </article>
            </li>
          </ul>
        </div>

        <div class="grid gap-8 md:grid-cols-2">
          <div>
            <h3 class="font-heading text-xl font-semibold text-slate-800">Categories</h3>
            <ul class="mt-4 space-y-3" role="list">
              <li v-for="cat in categories" :key="cat.name">
                <div class="clay-card flex cursor-pointer items-center justify-between p-4">
                  <span class="font-body font-medium text-slate-800">{{ cat.name }}</span>
                  <span class="rounded-full bg-clay-lilac/60 px-3 py-1 font-body text-sm font-medium text-slate-700">{{ cat.count }} books</span>
                </div>
              </li>
            </ul>
          </div>
          <div>
            <h3 class="font-heading text-xl font-semibold text-slate-800">Popular authors</h3>
            <ul class="mt-4 space-y-3" role="list">
              <li v-for="author in authors" :key="author.name">
                <div class="clay-card flex cursor-pointer items-center justify-between p-4">
                  <span class="font-body font-medium text-slate-800">{{ author.name }}</span>
                  <span class="font-body text-sm text-slate-600">{{ author.books }} titles</span>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
