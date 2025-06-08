<template>
  <div class="login-container">
    <div class="login-box">
      <h2>환영합니다!</h2>
      <p class="subtitle">계정을 만들고, 두:걸음과 여정을 함께 해 보세요.</p>
      
      <form @submit.prevent="regist">
        <div class="input-group">
          <input type="text" id="userId" v-model="user.userId" placeholder="아이디" required />
        </div>

        <div class="input-group">
          <input type="password" id="password" v-model="user.password" placeholder="비밀번호" required />
        </div>

        <div class="input-group">
          <input type="text" id="userName" v-model="user.userName" placeholder="이름" required />
        </div>

        <div class="input-group">
          <input type="email" id="email" v-model="user.email" placeholder="이메일" required />
        </div>

        <div class="input-group">
          <input type="number" id="age" v-model="user.age" placeholder="나이" required />
        </div>

        <button type="submit">회원가입</button>
      </form>

      <p class="login-link">
        이미 계정이 있으신가요?
        <button type="button" @click="goToLogin">로그인</button>
      </p>
    </div>

    <div class="login-welcome">
      <img src="@/assets/registCampfire.png" alt="Welcome" class="welcome-image" />
      <div class="welcome-text">
        <h1>첫 시작만으로</h1>
        <h1>다음 걸음을 내딛을 수 있습니다.</h1>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from '@/api/axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const user = ref({
  userId: '',
  password: '',
  userName: '',
  email: '',
  age: null,
})

const regist = async () => {
  try {
    const res = await axios.post('/user/regist', user.value)
    alert('회원가입 성공!')
    router.push('/login')
  } catch (err) {
    alert('회원가입 실패! 아이디 중복이거나 서버 오류입니다.')
    console.error(err)
  }
}

const goToLogin = () => {
  router.push('/login')
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
  padding: 48px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  max-width: 480px;
  background: white;
}

.login-box h2 {
  font-size: 2.5rem;
  color: #1a1a1a;
  margin-bottom: 8px;
  font-weight: 600;
}

.subtitle {
  color: #666;
  margin-bottom: 32px;
  font-size: 1rem;
}

.input-group {
  margin-bottom: 16px;
  position: relative;
}

input {
  width: 100%;
  padding: 14px;
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

input[type="number"] {
  -webkit-appearance: textfield;
  -moz-appearance: textfield;
  appearance: textfield;
}

input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

button[type="submit"] {
  width: 100%;
  padding: 14px;
  background-color: #a17676;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  margin-top: 24px;
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.2);
}

button[type="submit"]:hover {
  background-color: #8a6363;
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(161, 118, 118, 0.3);
}

.login-link {
  margin-top: 20px;
  text-align: center;
  color: #666;
}

.login-link button {
  background: none;
  border: none;
  color: #a17676;
  font-weight: 600;
  cursor: pointer;
  padding: 4px 8px;
  transition: all 0.3s;
}

.login-link button:hover {
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
    padding: 32px;
  }

  .login-container {
    height: calc(100vh - 96px);
    margin: 0;
  }
}
</style>
