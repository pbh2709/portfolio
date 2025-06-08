<template>
  <div class="app-container">
    <Header />
    <router-view />
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore' // 📌 Pinia 스토어 import
import Header from './components/Header.vue'

const router = useRouter()
const userStore = useUserStore()

// 페이지 로드 시 로그인 정보 불러오기
userStore.loadFromStorage()

const logout = () => {
  userStore.logout()
  alert('로그아웃 되었습니다.')
  router.push('/login')
}
</script>

<style>
@import url("https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.9/dist/web/static/pretendard.min.css");

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Pretendard", -apple-system, BlinkMacSystemFont, system-ui, Roboto, "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo", "Noto Sans KR", "Malgun Gothic", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", sans-serif;
}

body {
  min-height: 100vh;
  background: linear-gradient(135deg, #A2594D 0%, #B57461 50%, #C88F75 100%);
}

.app-container {
  min-height: 100vh;
  width: 100%;
  max-width: 1440px;
  margin: 0 auto;
  position: relative;
  padding: 32px;
}

#app {
  font-family: "Pretendard", -apple-system, BlinkMacSystemFont, system-ui, Roboto, "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo", "Noto Sans KR", "Malgun Gothic", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  min-height: 100vh;
  background: radial-gradient(circle at top right, rgba(255, 255, 255, 0.08), transparent);
}

.nav-bar {
  background-color: #f0f8ff;
  padding: 10px 20px;
  border-bottom: 1px solid #ccc;
  font-size: 16px;
}
.nav-bar a {
  margin-right: 10px;
  text-decoration: none;
  color: #007acc;
}
.nav-bar .nav-btn {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 4px 10px;
  border-radius: 4px;
  cursor: pointer;
}
.nav-bar .nav-btn:hover {
  background-color: #d32f2f;
}
.welcome-msg {
  margin-right: 10px;
  font-weight: bold;
  color: #333;
}
</style>
