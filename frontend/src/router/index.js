import { createRouter, createWebHistory } from 'vue-router'
import { ROUTES } from '../constants/routes'
import { useAuthStore } from '../stores/auth'
import HomeView from '../views/HomeView.vue'
import BooksView from '../views/BooksView.vue'
import LoansView from '../views/LoansView.vue'
import ReviewsView from '../views/ReviewsView.vue'
import JoinView from '../views/JoinView.vue'
import LoginView from '../views/LoginView.vue'
import BookDetailView from '../views/BookDetailView.vue'
import SearchView from '../views/SearchView.vue'
import LoanCreateView from '../views/LoanCreateView.vue'
import LoanDetailView from '../views/LoanDetailView.vue'
import AdminLayout from '../views/admin/AdminLayout.vue'

const routes = [
  {
    path: ROUTES.HOME,
    name: 'home',
    component: HomeView,
    meta: { title: 'Library Hub - Home' },
  },
  {
    path: ROUTES.BOOKS,
    name: 'books',
    component: BooksView,
    meta: { title: 'Library Hub - Book Catalog' },
  },
  {
    path: '/books/:id',
    name: 'bookDetail',
    component: BookDetailView,
    meta: { title: 'Library Hub - Book Detail' },
  },
  {
    path: ROUTES.SEARCH,
    name: 'search',
    component: SearchView,
    meta: { title: 'Library Hub - Search' },
  },
  {
    path: ROUTES.LOANS,
    name: 'loans',
    component: LoansView,
    meta: { title: 'Library Hub - My Loans', requiresAuth: true },
  },
  {
    path: ROUTES.LOAN_NEW,
    name: 'loanNew',
    component: LoanCreateView,
    meta: { title: 'Library Hub - Borrow a Book', requiresAuth: true },
  },
  {
    path: '/loans/:id',
    name: 'loanDetail',
    component: LoanDetailView,
    meta: { title: 'Library Hub - Loan Detail', requiresAuth: true },
  },
  {
    path: ROUTES.REVIEWS,
    name: 'reviews',
    component: ReviewsView,
    meta: { title: 'Library Hub - Reviews', requiresAuth: true },
  },
  {
    path: ROUTES.JOIN,
    name: 'join',
    component: JoinView,
    meta: { title: 'Library Hub - Join' },
  },
  {
    path: ROUTES.LOGIN,
    name: 'login',
    component: LoginView,
    meta: { title: 'Library Hub - Log in' },
  },
  {
    path: ROUTES.ADMIN,
    component: AdminLayout,
    meta: { title: 'Library Hub - Admin', requiresAdmin: true },
    children: [
      {
        path: '',
        name: 'adminDashboard',
        component: () => import('../views/admin/AdminDashboardView.vue'),
        meta: { title: 'Library Hub - Admin Dashboard' },
      },
      {
        path: 'books',
        name: 'adminBooks',
        component: () => import('../views/admin/AdminBookListView.vue'),
        meta: { title: 'Library Hub - Admin Books' },
      },
      {
        path: 'books/new',
        name: 'adminBookNew',
        component: () => import('../views/admin/AdminBookFormView.vue'),
        meta: { title: 'Library Hub - Add Book' },
      },
      {
        path: 'books/:id/edit',
        name: 'adminBookEdit',
        component: () => import('../views/admin/AdminBookFormView.vue'),
        meta: { title: 'Library Hub - Edit Book' },
      },
      {
        path: 'authors',
        name: 'adminAuthors',
        component: () => import('../views/admin/AdminAuthorListView.vue'),
        meta: { title: 'Library Hub - Admin Authors' },
      },
      {
        path: 'authors/new',
        name: 'adminAuthorNew',
        component: () => import('../views/admin/AdminAuthorFormView.vue'),
        meta: { title: 'Library Hub - Add Author' },
      },
      {
        path: 'authors/:id/edit',
        name: 'adminAuthorEdit',
        component: () => import('../views/admin/AdminAuthorFormView.vue'),
        meta: { title: 'Library Hub - Edit Author' },
      },
      {
        path: 'categories',
        name: 'adminCategories',
        component: () => import('../views/admin/AdminCategoryListView.vue'),
        meta: { title: 'Library Hub - Admin Categories' },
      },
      {
        path: 'categories/new',
        name: 'adminCategoryNew',
        component: () => import('../views/admin/AdminCategoryFormView.vue'),
        meta: { title: 'Library Hub - Add Category' },
      },
      {
        path: 'categories/:id/edit',
        name: 'adminCategoryEdit',
        component: () => import('../views/admin/AdminCategoryFormView.vue'),
        meta: { title: 'Library Hub - Edit Category' },
      },
      {
        path: 'publishers',
        name: 'adminPublishers',
        component: () => import('../views/admin/AdminPublisherListView.vue'),
        meta: { title: 'Library Hub - Admin Publishers' },
      },
      {
        path: 'publishers/new',
        name: 'adminPublisherNew',
        component: () => import('../views/admin/AdminPublisherFormView.vue'),
        meta: { title: 'Library Hub - Add Publisher' },
      },
      {
        path: 'publishers/:id/edit',
        name: 'adminPublisherEdit',
        component: () => import('../views/admin/AdminPublisherFormView.vue'),
        meta: { title: 'Library Hub - Edit Publisher' },
      },
      {
        path: 'loans',
        name: 'adminLoans',
        component: () => import('../views/admin/AdminLoanListView.vue'),
        meta: { title: 'Library Hub - Admin Loans' },
      },
      {
        path: 'loans/:id',
        name: 'adminLoanDetail',
        component: () => import('../views/admin/AdminLoanDetailView.vue'),
        meta: { title: 'Library Hub - Loan Detail' },
      },
      {
        path: 'users',
        name: 'adminUsers',
        component: () => import('../views/admin/AdminUserListView.vue'),
        meta: { title: 'Library Hub - Admin Users' },
      },
      {
        path: 'users/:id/edit',
        name: 'adminUserEdit',
        component: () => import('../views/admin/AdminUserFormView.vue'),
        meta: { title: 'Library Hub - Edit User' },
      },
      {
        path: 'reports',
        name: 'adminReports',
        component: () => import('../views/admin/AdminReportsView.vue'),
        meta: { title: 'Library Hub - Admin Reports' },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, _from, next) => {
  const authStore = useAuthStore()

  if (to.meta?.requiresAdmin) {
    if (!authStore.isAuthenticated) {
      next({ path: ROUTES.LOGIN, query: { redirect: to.fullPath } })
      return
    }
    if (!authStore.isAdmin) {
      next(ROUTES.HOME)
      return
    }
  }

  if (to.meta?.requiresAuth && !authStore.isAuthenticated) {
    next({ path: ROUTES.LOGIN, query: { redirect: to.fullPath } })
    return
  }

  if (to.meta?.title) {
    document.title = to.meta.title
  }
  next()
})

export default router
