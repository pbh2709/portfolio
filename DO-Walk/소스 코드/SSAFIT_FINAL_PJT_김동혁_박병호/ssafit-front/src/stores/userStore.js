// src/stores/userStore.js
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    loginUser: null
  }),
  actions: {
    setLoginUser(user) {
      this.loginUser = user
      localStorage.setItem('loginUser', JSON.stringify(user))
    },
    logout() {
      this.loginUser = null
      localStorage.removeItem('loginUser')
    },
    loadFromStorage() {
      const stored = localStorage.getItem('loginUser')
      if (stored) {
        this.loginUser = JSON.parse(stored)
      }
    }
  }
})
