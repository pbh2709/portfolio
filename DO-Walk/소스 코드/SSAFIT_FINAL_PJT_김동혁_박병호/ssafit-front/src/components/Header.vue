<template>
  <header class="header">
    <nav class="nav-container">
      <!-- 로고 or 홈 -->
      <router-link to="/" class="logo">두:걸음</router-link>

      <!-- 오른쪽 네비게이션 -->
      <div class="nav-links">
        <router-link to="/videos">영상목록</router-link>
        <router-link to="/mypage">마이페이지</router-link>

        <!-- 로그인 상태 -->
       <template v-if="userStore.loginUser">
  <!-- ✅ 파트너 변경 버튼 -->
  <button @click="goChangePartner" class="partner-btn">🤖 파트너 변경</button>

  <!-- ✅ AI 대화 기능 추가 -->
  <router-link to="/talk" class="talk-btn">🧠 AI와 대화하기</router-link>

  <span class="welcome">{{ userStore.loginUser.userName }}님의 하루를 응원해요!</span>
  <button @click="logout" class="logout-btn">로그아웃</button>
</template>

        <!-- 비로그인 상태 -->
        <template v-else>
          <router-link to="/login">로그인</router-link>
          <router-link to="/regist">회원가입</router-link>
        </template>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import axios from '@/api/axios'

const router = useRouter()
const userStore = useUserStore()

// 앱이 로드되었을 때 localStorage에서 로그인 정보 불러오기
userStore.loadFromStorage()

const logout = async () => {
  try {
    await axios.delete('/user/logout', { data: { userId: userStore.loginUser.userId } })
    localStorage.removeItem('token')         // ✅ Access Token 제거
    localStorage.removeItem('refreshToken')  // ✅ Refresh Token 제거
    userStore.logout()                       // ✅ Pinia 상태 초기화
    router.push('/login')                    // ✅ 로그인 페이지로 이동
  } catch (err) {
    console.error('로그아웃 실패:', err)
    alert('로그아웃 중 문제가 발생했습니다.')
  }
}

// ✅ 파트너 변경 버튼 클릭 시
const goChangePartner = () => {
  router.push('/change-partner')
}
</script>

<style scoped>
.header {
  background-color: rgba(255, 255, 255, 0.9);
  padding: 16px 24px;
  color: #a17676;
  box-shadow: 
    0 4px 16px rgba(0, 0, 0, 0.05),
    0 1px 3px rgba(0, 0, 0, 0.1);
  border-radius: 24px;
  margin: 0 16px 16px 16px;
  backdrop-filter: blur(10px);
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-weight: 700;
  font-size: 24px;
  text-decoration: none;
  color: #a17676;
  transition: all 0.2s;
}

.logo:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 16px;
}

.nav-links a {
  color: #a17676;
  text-decoration: none;
  padding: 8px 16px;
  border-radius: 12px;
  transition: all 0.2s;
  font-weight: 500;
}

.nav-links a:hover {
  background-color: rgba(161, 118, 118, 0.1);
  transform: translateY(-1px);
}

.welcome {
  margin: 0 16px;
  font-weight: 600;
  padding: 8px 16px;
  background-color: rgba(161, 118, 118, 0.1);
  border-radius: 12px;
  color: #8a6363;
}

.logout-btn {
  background-color: #a17676;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 12px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s;
}

.logout-btn:hover {
  background-color: #8a6363;
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.partner-btn {
  background-color: #f8f0f0;
  color: #a17676;
  border: none;
  padding: 8px 16px;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.partner-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #f0e6e6;
}

.talk-btn {
  background-color: transparent;
  color: #a17676;
  border: 2px solid #a17676;
  padding: 8px 16px;
  border-radius: 12px;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.2s;
}

.talk-btn:hover {
  background-color: #a17676;
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
