<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getUserById, updateUser, changePassword, updateUserStatus, updateUserRole } from '../../api'
import { ROUTES } from '../../constants/routes'

const route = useRoute()
const router = useRouter()
const user = ref(null)
const email = ref('')
const fullName = ref('')
const status = ref('ACTIVE')
const role = ref('MEMBER')
const oldPassword = ref('')
const newPassword = ref('')
const isLoading = ref(true)
const isSubmitting = ref(false)
const errorMessage = ref('')
const passwordMessage = ref('')

const userId = computed(() => Number(route.params.id))

const loadUser = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    user.value = await getUserById(userId.value)
    email.value = user.value.email ?? ''
    fullName.value = user.value.fullName ?? ''
    status.value = user.value.status ?? 'ACTIVE'
    role.value = user.value.role ?? 'MEMBER'
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to load user.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

const handleSaveProfile = async () => {
  if (isSubmitting.value) return
  isSubmitting.value = true
  errorMessage.value = ''
  try {
    await updateUser(userId.value, {
      email: email.value.trim(),
      fullName: fullName.value.trim(),
    })
    await loadUser()
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to update profile.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

const handleChangePassword = async () => {
  if (isSubmitting.value || !oldPassword.value || !newPassword.value) return
  if (newPassword.value.length < 8) {
    passwordMessage.value = 'New password must be at least 8 characters.'
    return
  }
  isSubmitting.value = true
  passwordMessage.value = ''
  errorMessage.value = ''
  try {
    await changePassword(userId.value, {
      oldPassword: oldPassword.value,
      newPassword: newPassword.value,
    })
    oldPassword.value = ''
    newPassword.value = ''
    passwordMessage.value = 'Password updated.'
  } catch (err) {
    passwordMessage.value = err.data?.message || err.message || 'Failed to change password.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

const handleUpdateStatus = async () => {
  if (isSubmitting.value) return
  isSubmitting.value = true
  errorMessage.value = ''
  try {
    await updateUserStatus(userId.value, { status: status.value })
    await loadUser()
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to update status.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

const handleUpdateRole = async () => {
  if (isSubmitting.value) return
  isSubmitting.value = true
  errorMessage.value = ''
  try {
    await updateUserRole(userId.value, { role: role.value })
    await loadUser()
  } catch (err) {
    errorMessage.value = err.data?.message || err.message || 'Failed to update role.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

onMounted(loadUser)
</script>

<template>
  <div class="admin-user-form-view">
    <h1 class="font-heading text-2xl font-bold text-slate-900">
      Sửa người dùng
    </h1>
    <router-link
      :to="ROUTES.ADMIN_USERS"
      class="mt-2 inline-block font-body text-sm font-medium text-primary hover:underline"
    >
      Back to users
    </router-link>

    <p v-if="isLoading" class="mt-6 font-body text-slate-600">Loading…</p>
    <template v-else-if="user">
      <p v-if="errorMessage" class="mt-4 font-body text-sm text-red-600">
        {{ errorMessage }}
      </p>

      <section class="mt-6 clay-card max-w-md space-y-4 p-6" aria-labelledby="profile-heading">
        <h2 id="profile-heading" class="font-heading text-lg font-semibold text-slate-900">
          Profile
        </h2>
        <div>
          <label for="user-email" class="block font-body text-sm font-medium text-slate-700">Email</label>
          <input
            id="user-email"
            v-model="email"
            type="email"
            required
            class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
          />
        </div>
        <div>
          <label for="user-fullName" class="block font-body text-sm font-medium text-slate-700">Full name</label>
          <input
            id="user-fullName"
            v-model="fullName"
            type="text"
            required
            class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
          />
        </div>
        <button
          type="button"
          class="clay-btn bg-primary px-4 py-2 font-body font-medium text-white disabled:opacity-50"
          :disabled="isSubmitting"
          @click="handleSaveProfile"
        >
          {{ isSubmitting ? 'Saving…' : 'Save profile' }}
        </button>
      </section>

      <section class="mt-6 clay-card max-w-md space-y-4 p-6" aria-labelledby="password-heading">
        <h2 id="password-heading" class="font-heading text-lg font-semibold text-slate-900">
          Change password
        </h2>
        <div>
          <label for="user-oldPassword" class="block font-body text-sm font-medium text-slate-700">Current password</label>
          <input
            id="user-oldPassword"
            v-model="oldPassword"
            type="password"
            class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
          />
        </div>
        <div>
          <label for="user-newPassword" class="block font-body text-sm font-medium text-slate-700">New password (min 8)</label>
          <input
            id="user-newPassword"
            v-model="newPassword"
            type="password"
            minlength="8"
            class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
          />
        </div>
        <p v-if="passwordMessage" class="font-body text-sm" :class="passwordMessage.startsWith('Password updated') ? 'text-green-600' : 'text-red-600'">
          {{ passwordMessage }}
        </p>
        <button
          type="button"
          class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body font-medium text-slate-700 disabled:opacity-50"
          :disabled="isSubmitting || !oldPassword || !newPassword"
          @click="handleChangePassword"
        >
          {{ isSubmitting ? 'Updating…' : 'Change password' }}
        </button>
      </section>

      <section class="mt-6 clay-card max-w-md space-y-4 p-6" aria-labelledby="status-heading">
        <h2 id="status-heading" class="font-heading text-lg font-semibold text-slate-900">
          Status
        </h2>
        <div>
          <label for="user-status" class="block font-body text-sm font-medium text-slate-700">Status</label>
          <select
            id="user-status"
            v-model="status"
            class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
          >
            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
            <option value="BANNED">BANNED</option>
          </select>
        </div>
        <button
          type="button"
          class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body font-medium text-slate-700 disabled:opacity-50"
          :disabled="isSubmitting"
          @click="handleUpdateStatus"
        >
          {{ isSubmitting ? 'Updating…' : 'Update status' }}
        </button>
      </section>

      <section class="mt-6 clay-card max-w-md space-y-4 p-6" aria-labelledby="role-heading">
        <h2 id="role-heading" class="font-heading text-lg font-semibold text-slate-900">
          Role
        </h2>
        <div>
          <label for="user-role" class="block font-body text-sm font-medium text-slate-700">Role</label>
          <select
            id="user-role"
            v-model="role"
            class="mt-1 block w-full rounded-clay border border-slate-300 px-3 py-2 font-body text-slate-900"
          >
            <option value="ADMIN">ADMIN</option>
            <option value="MEMBER">MEMBER</option>
          </select>
        </div>
        <button
          type="button"
          class="clay-btn border border-slate-300 bg-white px-4 py-2 font-body font-medium text-slate-700 disabled:opacity-50"
          :disabled="isSubmitting"
          @click="handleUpdateRole"
        >
          {{ isSubmitting ? 'Updating…' : 'Update role' }}
        </button>
      </section>
    </template>
  </div>
</template>
