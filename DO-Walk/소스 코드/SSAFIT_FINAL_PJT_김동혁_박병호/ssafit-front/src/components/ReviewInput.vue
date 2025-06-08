<template>
  <div class="review-input">
    <h3 class="review-title">오늘 운동이나 기분에 대해 적어보세요 💬</h3>
    <p class="review-subtitle">파트너와 함께 이야기를 나누며 운동 여정을 기록해보세요.</p>

    <div class="input-container">
      <textarea
        v-model="reviewText"
        placeholder="예: 오늘 스쿼트를 너무 많이 해서 다리가 후들거려요 😵"
      ></textarea>

      <button 
        @click="submitReview" 
        :disabled="!reviewText.trim()"
        class="submit-btn"
      >
        작성 완료!
      </button>
    </div>

    <div v-if="reaction" class="reaction-box">
      <div class="partner-icon">🤖</div>
      <div class="reaction-content">{{ reaction }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from '@/api/axios'
import { usePartnerStore } from '@/stores/partnerStore'
import { useMoodStore } from '@/stores/moodStore'
import { useUserStore } from '@/stores/userStore'

const props = defineProps({
  videoNo: {
    type: [Number, String],
    required: true
  }
})

const router = useRouter()
const reviewText = ref('')
const reaction = ref('')

const partnerStore = usePartnerStore()
const moodStore = useMoodStore()
const userStore = useUserStore()

onMounted(() => {
  console.log('✅ ReviewInput.vue mounted')
  if (!partnerStore.selectedPartner) partnerStore.loadFromStorage()
  if (!moodStore.selectedMood) moodStore.loadFromStorage?.()
})

const submitReview = async () => {
  if (!partnerStore.selectedPartnerId) {
    alert('AI 파트너를 먼저 선택해주세요!')
    router.push('/partner-select')
    return
  }

  if (!moodStore.selectedMood) {
    alert('오늘 기분을 먼저 선택해주세요!')
    router.push('/recommend')
    return
  }

  const accessToken = localStorage.getItem('accessToken')
  if (!accessToken) {
    alert('로그인이 필요합니다!')
    router.push('/login')
    return
  }

  try {
    // 1. AI 반응 요청
    const { data: gptReply } = await axios.post('/review/reply', {
      reviewText: reviewText.value,
      partnerId: partnerStore.selectedPartnerId,
      mood: moodStore.selectedMood
    })

    // 2. 리뷰 저장 요청 (writerId는 서버에서 JWT로 추출)
    await axios.post('/review/write', {
      videoNo: Number(props.videoNo),
      content: reviewText.value + '\n\n🤖 ' + gptReply
    })

    reaction.value = gptReply
    reviewText.value = ''
  } catch (err) {
    console.error('리뷰 등록 실패:', err)
    reaction.value = '⚠️ 파트너가 반응하지 않았어요. 다시 시도해보세요.'
  }
}
</script>

<style scoped>
.review-input {
  margin-top: 48px;
  border: 2px solid #f0e6e6;
  padding: 32px;
  border-radius: 24px;
  background-color: white;
  box-shadow: 0 8px 16px rgba(161, 118, 118, 0.1);
}

.review-title {
  font-size: 1.8rem;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 8px;
}

.review-subtitle {
  color: #666;
  font-size: 1rem;
  margin-bottom: 24px;
}

.input-container {
  position: relative;
}

textarea {
  width: 100%;
  min-height: 120px;
  padding: 16px;
  font-size: 1rem;
  border-radius: 16px;
  border: 2px solid #f0e6e6;
  resize: vertical;
  transition: all 0.3s;
  background-color: white;
  color: #1a1a1a;
  line-height: 1.5;
}

textarea:focus {
  outline: none;
  border-color: #a17676;
  box-shadow: 0 0 0 2px rgba(161, 118, 118, 0.1);
}

textarea::placeholder {
  color: #999;
}

.submit-btn {
  margin-top: 16px;
  padding: 12px 24px;
  font-weight: 600;
  background-color: #a17676;
  color: white;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
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

.reaction-box {
  margin-top: 32px;
  padding: 24px;
  background-color: #fdf6f6;
  border-radius: 16px;
  color: #1a1a1a;
  white-space: pre-line;
  display: flex;
  gap: 16px;
  align-items: flex-start;
  animation: slideIn 0.5s ease-out;
}

.partner-icon {
  font-size: 1.5rem;
  background: white;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 8px rgba(161, 118, 118, 0.15);
  flex-shrink: 0;
}

.reaction-content {
  flex: 1;
  line-height: 1.6;
  font-size: 1rem;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 1024px) {
  .review-input {
    padding: 24px;
    margin-top: 32px;
  }

  .review-title {
    font-size: 1.5rem;
  }
}

@media (max-width: 768px) {
  .review-input {
    padding: 20px;
    margin-top: 24px;
    border-radius: 16px;
  }

  .review-title {
    font-size: 1.3rem;
  }

  .review-subtitle {
    font-size: 0.9rem;
    margin-bottom: 16px;
  }

  textarea {
    padding: 12px;
    font-size: 0.9rem;
  }

  .submit-btn {
    width: 100%;
  }

  .reaction-box {
    padding: 16px;
    margin-top: 24px;
  }
}
</style>
