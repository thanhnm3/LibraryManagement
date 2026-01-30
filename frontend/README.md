# Library Hub – Frontend

Playful bookstore/library management landing page built with **Vite + Vue 3 + Tailwind CSS**. Claymorphism-style cards, hero section, book catalog preview, “My loans” demo, reader testimonials, and “Join the library” CTA.

## Tech stack

- **Vue 3** (Composition API, `<script setup>`)
- **Vite 6**
- **Tailwind CSS 3**

## Run locally

```bash
cd frontend
npm install
npm run dev
```

Open [http://localhost:5173](http://localhost:5173).

## Build

```bash
npm run build
npm run preview   # preview production build
```

## Backend API

The backend REST API is expected at `http://localhost:8080`. Configure proxy in `vite.config.js` (e.g. `/api` → `http://localhost:8080`) when integrating.

## Structure

- `src/App.vue` – Root layout
- `src/components/` – TheHeader, HeroSection, BookCatalogSection, MyLoansSection, TestimonialsSection, CtaSection, TheFooter
- `src/index.css` – Tailwind + claymorphism utility classes
- `public/favicon.svg` – Favicon

All source and assets live under `frontend/` only.
