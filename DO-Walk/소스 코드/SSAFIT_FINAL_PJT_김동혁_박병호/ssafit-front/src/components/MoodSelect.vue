<template>
  <div class="mood-select-wrapper">
    <div class="mood-select-card">
      <div class="header">
        <span class="emoji">🌟</span>
        <h2>오늘의 기분은 어떠신가요?</h2>
        <p class="subtitle">당신의 기분에 맞는 운동을 추천해드릴게요</p>
      </div>

      <div class="select-area">
        <label for="mood">나의 기분 선택하기</label>
        <select id="mood" v-model="mood">
          <option disabled value="">기분을 선택해주세요</option>
          <option value="즐거움">즐거움</option>
          <option value="슬픔">슬픔</option>
          <option value="화남">화남</option>
          <option value="아무렇지 않음">아무렇지 않음</option>
        </select>
      </div>

      <button @click="requestRecommendation" :disabled="!mood">
        <span class="icon">✨</span>
        맞춤 운동 추천받기
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from '@/api/axios'
import { useRouter } from 'vue-router'
import { useMoodStore } from '@/stores/moodStore'

const mood = ref('')
const router = useRouter()
const moodStore = useMoodStore()

const requestRecommendation = async () => {
  try {
    // ✅ store에 먼저 기분 저장
    moodStore.setMood(mood.value)

    // ✅ 기분으로 추천 요청
    await axios.post('http://localhost:8080/api/recommendation/generate',
      { mood: mood.value },
      { withCredentials: true }
    )

    alert('운동 영상이 추천되었습니다!')
    router.push('/videos')
  } catch (error) {
    console.log('✅ mood:', mood.value)
    console.error('추천 요청 실패:', error)
    alert('추천 요청에 실패했습니다. 다시 시도해주세요.')
  }
}
</script>

<style scoped>
.mood-select-wrapper {
  max-width: 800px;
  margin: 32px auto;
  padding: 0 16px;
}

.mood-select-card {
  background: white;
  padding: 32px;
  border-radius: 32px;
  box-shadow: 
    0 24px 48px rgba(0, 0, 0, 0.15),
    0 12px 24px rgba(0, 0, 0, 0.1),
    0 8px 16px rgba(162, 89, 77, 0.1);
  text-align: center;
}

.header {
  margin-bottom: 32px;
}

.emoji {
  font-size: 48px;
  display: block;
  margin-bottom: 16px;
}

h2 {
  font-size: 2rem;
  color: #1a1a1a;
  margin-bottom: 8px;
  font-weight: 600;
}

.subtitle {
  color: #666;
  font-size: 1.1rem;
}

.select-area {
  background: #fdf6f6;
  padding: 24px;
  border-radius: 24px;
  margin-bottom: 32px;
}

label {
  display: block;
  margin-bottom: 12px;
  color: #1a1a1a;
  font-size: 1.1rem;
  font-weight: 500;
}

select {
  width: 100%;
  max-width: 400px;
  padding: 16px;
  font-size: 1rem;
  border: 2px solid #f0e6e6;
  border-radius: 16px;
  background: white;
  color: #1a1a1a;
  cursor: pointer;
  transition: all 0.3s;
}

select:focus {
  outline: none;
  border-color: #a17676;
  box-shadow: 0 0 0 2px rgba(161, 118, 118, 0.1);
}

select option {
  padding: 12px;
}

button {
  padding: 16px 32px;
  font-size: 1.1rem;
  font-weight: 600;
  background: #a17676;
  color: white;
  border: none;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

button:hover:not(:disabled) {
  background: #8a6363;
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(161, 118, 118, 0.2);
}

button:disabled {
  background: #f0e6e6;
  color: #a17676;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.icon {
  font-size: 1.2rem;
}

@media (max-width: 768px) {
  .mood-select-wrapper {
    margin: 0;
  }

  .mood-select-card {
    border-radius: 0;
    padding: 24px 16px;
  }

  h2 {
    font-size: 1.5rem;
  }

  .subtitle {
    font-size: 1rem;
  }

  .select-area {
    padding: 16px;
  }

  select {
    width: 100%;
  }

  button {
    width: 100%;
    padding: 16px;
  }
}
</style>
