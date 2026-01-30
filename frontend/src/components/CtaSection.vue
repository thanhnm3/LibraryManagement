<script setup>
const handleSubmit = (event) => {
  event.preventDefault()
  const form = event.target
  const email = form?.querySelector('input[type="email"]')?.value
  if (email) {
    console.info('Join sign-up (demo):', email)
  }
}
</script>

<template>
  <section id="join" class="px-4 py-16 md:px-6 md:py-24" aria-labelledby="join-heading">
    <div class="mx-auto max-w-4xl">
      <div class="clay-card overflow-hidden p-8 text-center md:p-12">
        <h2 id="join-heading" class="font-heading text-3xl font-bold text-slate-900 md:text-4xl">
          Join the library
        </h2>
        <p class="mx-auto mt-4 max-w-xl font-body text-lg text-slate-600">
          Get a free membership. Borrow books, track loans, and discover new reads. No credit card required.
        </p>
        <form
          class="mx-auto mt-8 max-w-md"
          method="post"
          action="#"
          @submit="handleSubmit"
        >
          <label for="join-email" class="sr-only">Email address</label>
          <div class="flex flex-col gap-3 sm:flex-row">
            <input
              id="join-email"
              type="email"
              name="email"
              required
              autocomplete="email"
              placeholder="you@example.com"
              class="min-w-0 flex-1 rounded-clay border-2 border-slate-200 bg-white px-4 py-3 font-body text-slate-800 placeholder-slate-400 transition-colors duration-200 focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
            />
            <button type="submit" class="clay-btn shrink-0 bg-primary px-6 py-3 text-white">
              Sign up
            </button>
          </div>
          <p class="mt-3 font-body text-sm text-slate-500">
            We will only use your email to send you updates. Unsubscribe anytime.
          </p>
        </form>
        <p class="mt-6 font-body text-sm text-slate-600">
          Backend API at <code class="rounded bg-slate-100 px-1.5 py-0.5 font-mono text-xs">http://localhost:8080</code> will be connected later.
        </p>
      </div>
    </div>
  </section>
</template>
