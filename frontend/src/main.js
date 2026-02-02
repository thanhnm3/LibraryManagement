import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import './index.css'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.mount('#app')

// Redirect to login on 401 (token expired or invalid)
window.addEventListener('auth:unauthorized', () => {
  const redirect = router.currentRoute.value.fullPath
  router.push(redirect && redirect !== '/login' ? `/login?redirect=${encodeURIComponent(redirect)}` : '/login')
})
