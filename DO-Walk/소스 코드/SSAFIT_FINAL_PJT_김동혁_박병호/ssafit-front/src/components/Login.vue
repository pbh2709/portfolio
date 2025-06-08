<template>
  <div class="login-container">
    <div class="login-box">
      <h2>안녕하세요!</h2>
      <p class="subtitle">두:걸음과 함께 발을 내딛어보아요.</p>
      
      <form @submit.prevent="login">
        <div class="input-group">
          <input type="text" id="userId" v-model="user.userId" placeholder="아이디" required />
        </div>

        <div class="input-group">
          <input type="password" id="password" v-model="user.password" placeholder="비밀번호" required />
        </div>

        <button type="submit">로그인</button>
      </form>

      <p class="register-link">
        두:걸음이 처음이신가요?
        <button type="button" @click="goToRegist">회원가입</button>
      </p>
    </div>

    <div class="login-welcome">
      <img src="@/assets/loginCampfire2.png" alt="Welcome" class="welcome-image" />
      <div class="welcome-text">
        <h1>여기까지 온 발걸음이</h1>
        <h1>오롯이 당신의 것입니다.</h1>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from '@/api/axios'
import { useUserStore } from '@/stores/userStore'
import { usePartnerStore } from '@/stores/partnerStore'

const router = useRouter()
const userStore = useUserStore()
const partnerStore = usePartnerStore()

const user = ref({
  userId: '',
  password: ''
})

const login = async () => {
  try {
    const res = await axios.post('/user/login', user.value)
    const { accessToken, refreshToken, userId, userName } = res.data

    // ✅ 토큰 저장 (키 정확히!)
    localStorage.setItem('accessToken', accessToken)
    localStorage.setItem('refreshToken', refreshToken)

    // ✅ Pinia에 로그인 정보 저장
    userStore.setLoginUser({ userId, userName })

    // ✅ 파트너 선택 여부에 따라 분기 이동
    if (partnerStore.partnerName) {
      router.push('/recommend')
    } else {
      router.push('/partner-select')
    }
  } catch (err) {
    alert('로그인 실패! 아이디나 비밀번호를 확인해주세요.')
    console.error(err)
  }
}

const goToRegist = () => {
  router.push('/regist')
}
</script>

<style scoped>
.login-container {
  display: flex;
  height: calc(100vh - 128px);
  background: white;
  border-radius: 32px;
  overflow: hidden;
  box-shadow: 
    0 24px 48px rgba(0, 0, 0, 0.15),
    0 12px 24px rgba(0, 0, 0, 0.1),
    0 8px 16px rgba(162, 89, 77, 0.1);
  margin: 8px;
}

.login-box {
  flex: 1;
  padding: 64px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  max-width: 480px;
  background: white;
}

.login-box h2 {
  font-size: 2.5rem;
  color: #1a1a1a;
  margin-bottom: 12px;
  font-weight: 600;
}

.subtitle {
  color: #666;
  margin-bottom: 48px;
  font-size: 1rem;
}

.input-group {
  margin-bottom: 24px;
  position: relative;
}

input {
  width: 100%;
  padding: 16px;
  background: #f8f8f8;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  transition: all 0.3s;
}

input::placeholder {
  color: #999;
}

input:focus {
  outline: none;
  background: #f0f0f0;
}

button[type="submit"] {
  width: 100%;
  padding: 16px;
  background-color: #a17676;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  margin-top: 32px;
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.2);
}

button[type="submit"]:hover {
  background-color: #8a6363;
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(161, 118, 118, 0.3);
}

.register-link {
  margin-top: 24px;
  text-align: center;
  color: #666;
}

.register-link button {
  background: none;
  border: none;
  color: #a17676;
  font-weight: 600;
  cursor: pointer;
  padding: 4px 8px;
  transition: all 0.3s;
}

.register-link button:hover {
  color: #8a6363;
  text-decoration: underline;
}

.login-welcome {
  flex: 1.2;
  position: relative;
  overflow: hidden;
  border-radius: 24px;
  margin: 16px 16px 16px 0;
  background: white;
  box-shadow: inset 0 0 48px rgba(162, 89, 77, 0.03);
}

.welcome-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.welcome-text {
  position: absolute;
  bottom: 48px;
  left: 48px;
  color: white;
}

.welcome-text h1 {
  font-size: 2rem;
  margin-bottom: 24px;
  max-width: 400px;
  line-height: 1.3;
}



@media (max-width: 1024px) {
  .login-welcome {
    display: none;
  }
  
  .login-box {
    max-width: 100%;
    padding: 48px 32px;
  }

  .login-container {
    height: calc(100vh - 96px);
    margin: 0;
  }
}
</style>
