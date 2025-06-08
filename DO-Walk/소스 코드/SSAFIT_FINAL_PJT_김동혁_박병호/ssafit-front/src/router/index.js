import { createRouter, createWebHistory } from 'vue-router'

// 컴포넌트 import
import Login from '../components/Login.vue'
import Regist from '../components/Regist.vue'
import VideoList from '../components/VideoList.vue'
import VideoDetail from '../components/VideoDetail.vue'
import MyPage from '../components/MyPage.vue'
import PartnerSelect from '../views/PartnerSelect.vue'
import MoodSelect from '../components/MoodSelect.vue'
import QuestionInput from '../components/QuestionInput.vue'  // ✅ 추가

// Pinia stores
import { useUserStore } from '@/stores/userStore'
import { usePartnerStore } from '@/stores/partnerStore'

const routes = [
  { path: '/login', component: Login },
  { path: '/regist', component: Regist },
  { path: '/videos', component: VideoList },
  { path: '/video/:videoNo', component: VideoDetail },
  { path: '/mypage', component: MyPage },
  { path: '/partner-select', component: PartnerSelect, props: { mode: 'onboarding' } },
  { path: '/change-partner', component: PartnerSelect, props: { mode: 'change' } },
  { path: '/recommend', component: MoodSelect },
  { path: '/talk', component: QuestionInput }, // ✅ AI 감성 대화 경로 추가
  { path: '/', redirect: '/videos' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// ✅ 네비게이션 가드
router.beforeEach((to, from, next) => {
  const loginUser = JSON.parse(localStorage.getItem('loginUser'))

  if (!loginUser && to.path !== '/login' && to.path !== '/regist') {
    return next('/login')
  }

  if (loginUser && (to.path === '/login' || to.path === '/regist')) {
    return next('/videos')
  }

  if (to.path === '/') {
    const partnerStore = usePartnerStore()
    const partnerId = partnerStore.selectedPartnerId || JSON.parse(localStorage.getItem('partner'))?.id

    if (!partnerId) {
      return next('/partner-select')
    } else {
      return next('/videos')
    }
  }

  next()
})

export default router
