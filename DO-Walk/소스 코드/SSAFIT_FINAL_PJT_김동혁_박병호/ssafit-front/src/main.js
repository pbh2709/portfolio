import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import { MotionPlugin } from '@vueuse/motion'
import { usePartnerStore } from '@/stores/partnerStore'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)            // ✅ Pinia 등록
app.use(router)           // ✅ Vue Router 등록
app.use(MotionPlugin)     // ✅ Motion 등록

app.mount('#app')

// store 초기화 이후 파트너 복원
const partnerStore = usePartnerStore()
partnerStore.loadFromStorage()