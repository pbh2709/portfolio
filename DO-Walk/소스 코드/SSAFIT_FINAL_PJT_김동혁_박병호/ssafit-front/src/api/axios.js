import axios from 'axios'
import { useUserStore } from '@/stores/userStore'

const instance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 15000,
  withCredentials: true
})

// ✅ 요청 인터셉터 - accessToken 자동 첨부
instance.interceptors.request.use(config => {
  const token = localStorage.getItem('accessToken') // ✅ 여기 반드시 accessToken
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`
  }
  return config
}, error => {
  return Promise.reject(error)
})

// ✅ 응답 인터셉터 - 401 발생 시 accessToken 재발급 처리
instance.interceptors.response.use(
  res => res,
  async error => {
    const originalRequest = error.config

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      try {
        const refreshToken = localStorage.getItem('refreshToken')
        if (!refreshToken) throw new Error('Refresh Token 없음')

        // 🔄 Refresh Token으로 accessToken 재발급 요청
        const res = await axios.post('http://localhost:8080/user/refresh', {
          refreshToken
        })

        const newAccessToken = res.data.accessToken

        // ✅ 통일된 키로 저장해야 다음 요청에도 제대로 헤더가 붙음
        localStorage.setItem('accessToken', newAccessToken)
        originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`

        return instance(originalRequest)
      } catch (err) {
        console.error('🔴 토큰 재발급 실패:', err)

        // 로그아웃 처리
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')

        const userStore = useUserStore()
        userStore.logout()

        window.location.href = '/login'
      }
    }

    return Promise.reject(error)
  }
)

export default instance
