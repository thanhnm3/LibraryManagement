<script setup>
import { RouterLink, RouterView } from 'vue-router'
import { ROUTES } from '../../constants/routes'
</script>

<template>
  <div class="admin-layout flex min-h-screen">
    <aside class="admin-layout_sidebar w-56 shrink-0 border-r border-slate-200 bg-slate-50 p-4">
      <nav class="admin-layout_nav flex flex-col gap-1" aria-label="Admin navigation">
        <RouterLink :to="ROUTES.ADMIN" class="admin-layout_link rounded px-3 py-2 text-slate-700 hover:bg-slate-200">
          Dashboard
        </RouterLink>
        <RouterLink :to="ROUTES.ADMIN_BOOKS" class="admin-layout_link rounded px-3 py-2 text-slate-700 hover:bg-slate-200">
          Sách
        </RouterLink>
        <RouterLink :to="ROUTES.ADMIN_AUTHORS" class="admin-layout_link rounded px-3 py-2 text-slate-700 hover:bg-slate-200">
          Tác giả
        </RouterLink>
        <RouterLink :to="ROUTES.ADMIN_CATEGORIES" class="admin-layout_link rounded px-3 py-2 text-slate-700 hover:bg-slate-200">
          Danh mục
        </RouterLink>
        <RouterLink :to="ROUTES.ADMIN_PUBLISHERS" class="admin-layout_link rounded px-3 py-2 text-slate-700 hover:bg-slate-200">
          NXB
        </RouterLink>
        <RouterLink :to="ROUTES.ADMIN_LOANS" class="admin-layout_link rounded px-3 py-2 text-slate-700 hover:bg-slate-200">
          Mượn trả
        </RouterLink>
        <RouterLink :to="ROUTES.ADMIN_USERS" class="admin-layout_link rounded px-3 py-2 text-slate-700 hover:bg-slate-200">
          Người dùng
        </RouterLink>
        <RouterLink :to="ROUTES.ADMIN_REPORTS" class="admin-layout_link rounded px-3 py-2 text-slate-700 hover:bg-slate-200">
          Báo cáo
        </RouterLink>
      </nav>
    </aside>
    <main class="admin-layout_main flex-1 p-6">
      <RouterView />
    </main>
  </div>
</template>
