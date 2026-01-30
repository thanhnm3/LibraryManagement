<script setup>
import TheHeader from './components/TheHeader.vue'
import TheFooter from './components/TheFooter.vue'
</script>

<template>
  <div class="min-h-screen bg-gradient-to-br from-clay-cream via-clay-lilac/30 to-clay-blue/40">
    <TheHeader />
    <RouterView />
    <TheFooter />
  </div>
</template>
