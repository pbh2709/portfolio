<template>
  <div class="login-container">
    <div class="login-box">
      <h2>AI 파트너를 선택해주세요</h2>
      <p class="subtitle">당신의 여정을 함께할 파트너를 선택해보세요.</p>
      
      <div class="partner-list">
        <div
          class="partner-card"
          v-for="partner in partners"
          :key="partner.id"
          :class="{ selected: partner.id === selectedType }"
          @click="selectPartner(partner)"
        >
          <img
            :src="characterImages[partner.id]"
            alt="Partner Image"
            class="character-img"
          >
          <h3>{{ partner.name }}</h3>
          <p>{{ partner.tone }}</p>
        </div>
      </div>

      <button class="submit-btn" :disabled="!selectedType" @click="confirmSelection">
        {{ mode === 'change' ? '파트너 변경' : '선택 완료' }}
      </button>

      <!-- 🎈 눈에 확 띄는 애니메이션 적용된 인사 메시지 -->
      <div
        v-if="greetingMessage"
        v-motion="motionConfig"
        class="greeting-popup"
      >
        💬 {{ greetingMessage }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { usePartnerStore } from '@/stores/partnerStore'
import { partners } from '@/data/partners'

// Import character images
import chr1Nunko from '@/assets/chr1Nunko.png'
import chr2Harley from '@/assets/chr2Harley.png'
import chr3Ssurong from '@/assets/chr3Ssurong.png'
import chr4Eden from '@/assets/chr4Eden.png'
import chr5Ddato from '@/assets/chr5Ddato.png'

// Create image mapping
const characterImages = {
  'ironcoach': chr1Nunko,
  'cheerbuddy': chr2Harley,
  'zenmaster': chr3Ssurong,
  'sciencebot': chr4Eden,
  'darkknight': chr5Ddato
}

// props: onboarding | change
const props = defineProps({
  mode: {
    type: String,
    default: 'onboarding'
  }
})

const router = useRouter()
const partnerStore = usePartnerStore()
const selectedType = ref('')
const greetingMessage = ref('')

// 🎯 확실한 시각 효과용 애니메이션 설정
const motionConfig = {
  initial: { opacity: 0, scale: 0.5, y: 40 },
  enter: {
    opacity: 1,
    scale: [1.5, 0.9, 1.1, 1],
    rotate: [-8, 8, -4, 2, 0],
    y: 0,
    transition: {
      duration: 1.2,
      easing: 'ease-in-out'
    }
  },
  leave: {
    opacity: 0,
    scale: 0.9,
    y: -30,
    transition: { duration: 0.4 }
  }
}

const selectPartner = (partner) => {
  selectedType.value = partner.id
  partnerStore.setPartner(partner)
  localStorage.setItem('partner', JSON.stringify(partner))
}

const confirmSelection = () => {
  const selected = partners.find(p => p.id === selectedType.value)
  if (!selected) return

  greetingMessage.value = selected.greeting
  console.log('🎬 인사 메시지:', greetingMessage.value)

  setTimeout(() => {
    greetingMessage.value = ''
    if (props.mode === 'change') {
      router.push('/videos')
    } else {
      router.push('/recommend')
    }
  }, 3000) // ⏱ 충분히 인사 볼 수 있도록 시간 확보!
}

onMounted(() => {
  const current = partnerStore.selectedPartnerId || JSON.parse(localStorage.getItem('partner'))?.id
  selectedType.value = current || ''
})
</script>

<style scoped>
.login-container {
  display: flex;
  min-height: calc(100vh - 128px);
  background: white;
  border-radius: 32px;
  overflow: auto;
  box-shadow: 
    0 24px 48px rgba(0, 0, 0, 0.15),
    0 12px 24px rgba(0, 0, 0, 0.1),
    0 8px 16px rgba(162, 89, 77, 0.1);
  margin: 8px;
  padding: 32px;
}

.login-box {
  flex: 1;
  padding: 0 24px;
  display: flex;
  flex-direction: column;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  background: white;
}

.login-box h2 {
  font-size: 2.5rem;
  color: #1a1a1a;
  margin-bottom: 8px;
  font-weight: 600;
  text-align: center;
}

.subtitle {
  color: #666;
  margin-bottom: 32px;
  font-size: 1rem;
  text-align: center;
}

.partner-list {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
  margin-bottom: 32px;
  width: 100%;
  padding: 0 16px;
}

.partner-card {
  border: 2px solid #f0e6e6;
  border-radius: 16px;
  padding: 20px 16px;
  cursor: pointer;
  transition: all 0.3s;
  background-color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  aspect-ratio: 1;
  min-width: 0;
}

.partner-card .character-img {
  width: 120px;
  height: 120px;
  margin-bottom: 12px;
  object-fit: contain;
  transition: transform 0.3s;
}

.partner-card:hover .character-img {
  transform: scale(1.05);
}

.partner-card h3 {
  font-size: 1.1rem;
  color: #1a1a1a;
  margin-bottom: 6px;
}

.partner-card p {
  color: #666;
  font-size: 0.9rem;
  line-height: 1.4;
  flex-grow: 1;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.partner-card:hover {
  transform: translateY(-2px);
  border-color: #a17676;
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.1);
}

.partner-card.selected {
  border-color: #a17676;
  background-color: #fdf6f6;
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.1);
}

.submit-btn {
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

.submit-btn:hover:not(:disabled) {
  background-color: #8a6363;
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(161, 118, 118, 0.3);
}

.submit-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.greeting-popup {
  margin-top: 24px;
  background-color: #fdf6f6;
  padding: 16px 20px;
  border-radius: 16px;
  font-size: 1rem;
  color: #1a1a1a;
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.1);
  position: relative;
  text-align: center;
}

.greeting-popup::after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  border-width: 10px 10px 0 10px;
  border-style: solid;
  border-color: #fdf6f6 transparent transparent transparent;
}

@media (max-width: 1400px) {
  .partner-list {
    grid-template-columns: repeat(5, 1fr);
    gap: 12px;
    padding: 0;
  }

  .partner-card {
    padding: 16px 12px;
  }

  .partner-card .character-img {
    width: 100px;
    height: 100px;
    margin-bottom: 10px;
  }

  .partner-card h3 {
    font-size: 1rem;
  }

  .partner-card p {
    font-size: 0.85rem;
  }
}

@media (max-width: 1200px) {
  .partner-list {
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
  }
  
  .partner-card .character-img {
    width: 130px;
    height: 130px;
  }
}

@media (max-width: 768px) {
  .partner-list {
    grid-template-columns: repeat(2, 1fr);
  }

  .login-container {
    padding: 24px 16px;
  }

  .login-box {
    padding: 0;
  }

  .partner-card .character-img {
    width: 140px;
    height: 140px;
  }
}

@media (max-width: 480px) {
  .partner-list {
    grid-template-columns: 1fr;
  }
  
  .partner-card {
    aspect-ratio: auto;
    padding: 24px;
  }
  
  .partner-card .character-img {
    width: 160px;
    height: 160px;
    margin-bottom: 16px;
  }
}
</style>
