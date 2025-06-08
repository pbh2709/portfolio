<template>
  <div class="video-list">
    <div class="header-bar">
      <h2>오늘의 기분에 맞는 추천 영상</h2>
      <p class="subtitle">오늘 당신의 하루는 어떤가요? 딱 맞는 영상을 추천해드릴게요.</p>
      <div class="controls">
        <select v-model="mood">
          <option disabled value="">기분 선택</option>
          <option value="즐거움">즐거움</option>
          <option value="슬픔">슬픔</option>
          <option value="화남">화남</option>
          <option value="아무렇지 않음">아무렇지 않음</option>
        </select>
        <button class="reroll-btn" @click="rerollRecommendation" :disabled="!mood">🔄 추천 다시 받기</button>
      </div>
    </div>

    <ul>
      <li v-for="video in videos" :key="video.videoNo" class="video-item">
        <img :src="getThumbnail(video)" alt="썸네일" class="thumbnail-small" @click="goToDetail(video.videoNo)" />
        
        <div class="video-info">
          <h3 @click="goToDetail(video.videoNo)" class="clickable">{{ video.videoTitle }}</h3>
          <p class="channel-name">{{ video.channelName }}</p>
        </div>

        <button 
          @click="toggleFavorite(video)" 
          class="favorite-btn"
          :class="{ active: isFavorited(video) }"
        >
          {{ isFavorited(video) ? '❤️ 찜됨' : '🤍 찜하기' }}
        </button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted ,computed} from 'vue'
import axios from '../api/axios'
import { useRouter } from 'vue-router'
import { useMoodStore } from '@/stores/moodStore'

const videos = ref([])
const favorites = ref(new Set())
const router = useRouter()
const moodStore = useMoodStore()
const mood = computed({
  get: () => moodStore.selectedMood,
  set: (val) => moodStore.setMood(val)
})

const fetchVideosForUser = async () => {
  try {
    const res = await axios.get('/video/list/user')
    videos.value = res.data
  } catch (err) {
    console.error('로그인 유저 추천 영상 불러오기 실패:', err)
  }
}

const fetchFavorites = async () => {
  try {
    const res = await axios.get('/favorite')
    favorites.value = new Set(res.data.map(v => v.videoId))
  } catch (err) {
    console.error('찜 목록 불러오기 실패:', err)
  }
}

const isFavorited = (video) => favorites.value.has(video.videoId)

const toggleFavorite = async (video) => {
  try {
    if (isFavorited(video)) {
      await axios.delete(`/favorite/${video.videoNo}`)
      favorites.value.delete(video.videoId)
    } else {
      await axios.post(`/favorite/${video.videoNo}`)
      favorites.value.add(video.videoId)
    }
  } catch (err) {
    console.error('찜 실패:', err)
    alert('찜 처리 중 오류가 발생했습니다.')
  }
}

const goToDetail = (videoNo) => {
  router.push(`/video/${videoNo}`)
}

const getThumbnail = (video) => {
  return video.thumbnailUrl || `https://img.youtube.com/vi/${video.videoId}/0.jpg`
}

const rerollRecommendation = async () => {
  try {
    // 🔄 moodStore 갱신
    moodStore.setMood(mood.value)

    // 🔄 추천 요청
    await axios.post('/api/recommendation/generate', { mood: mood.value }, { withCredentials: true })
    await fetchVideosForUser()
  } catch (err) {
    console.error('리롤 실패:', err)
    alert('추천을 다시 받는 데 실패했어요.')
  }
}

onMounted(() => {
  fetchVideosForUser()
  fetchFavorites()
})
</script>

<style scoped>
.video-list {
  max-width: 1200px;
  margin: 8px auto;
  padding: 32px;
  background: white;
  border-radius: 32px;
  min-height: calc(100vh - 128px);
  box-shadow: 
    0 24px 48px rgba(0, 0, 0, 0.15),
    0 12px 24px rgba(0, 0, 0, 0.1),
    0 8px 16px rgba(162, 89, 77, 0.1);
}

.header-bar {
  margin-bottom: 32px;
  text-align: center;
}

.header-bar h2 {
  font-size: 2.5rem;
  color: #1a1a1a;
  margin-bottom: 8px;
  font-weight: 600;
}

.subtitle {
  color: #666;
  font-size: 1rem;
  margin-bottom: 24px;
}

.controls {
  display: flex;
  gap: 16px;
  align-items: center;
  justify-content: center;
  margin-top: 16px;
}

select {
  padding: 12px 16px;
  border-radius: 12px;
  border: 2px solid #f0e6e6;
  background: white;
  font-size: 1rem;
  color: #1a1a1a;
  cursor: pointer;
  transition: all 0.3s;
}

select:hover {
  border-color: #a17676;
}

select:focus {
  outline: none;
  border-color: #a17676;
  box-shadow: 0 0 0 2px rgba(161, 118, 118, 0.1);
}

.reroll-btn {
  padding: 12px 24px;
  background-color: #a17676;
  border: none;
  border-radius: 12px;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.2);
}

.reroll-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
  box-shadow: none;
}

.reroll-btn:hover:enabled {
  background-color: #8a6363;
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(161, 118, 118, 0.3);
}

ul {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 32px;
}

.video-item {
  border: 2px solid #f0e6e6;
  border-radius: 16px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 24px;
  background: white;
  transition: all 0.3s;
}

.video-item:hover {
  transform: translateY(-2px);
  border-color: #a17676;
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.1);
}

.thumbnail-small {
  width: 200px;
  height: 112px;
  object-fit: cover;
  border-radius: 12px;
  transition: transform 0.3s;
}

.video-item:hover .thumbnail-small {
  transform: scale(1.02);
}

.video-info {
  flex: 1;
  min-width: 0;
}

.clickable {
  cursor: pointer;
  color: #1a1a1a;
  text-decoration: none;
  font-weight: 600;
  font-size: 1.2rem;
  margin-bottom: 8px;
  display: block;
  transition: color 0.3s;
}

.clickable:hover {
  color: #a17676;
}

.channel-name {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 16px;
}

.favorite-btn {
  background: none;
  border: 2px solid #a17676;
  padding: 8px 16px;
  border-radius: 12px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s;
  color: #a17676;
  font-weight: 500;
  white-space: nowrap;
}

.favorite-btn:hover {
  background-color: #a17676;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.2);
}

.favorite-btn.active {
  background-color: #a17676;
  color: white;
}

@media (max-width: 1024px) {
  .video-list {
    margin: 0;
    border-radius: 0;
    padding: 24px;
  }

  .header-bar h2 {
    font-size: 2rem;
  }

  .subtitle {
    font-size: 0.9rem;
    margin-bottom: 20px;
  }

  .controls {
    flex-direction: column;
    gap: 12px;
  }

  select, .reroll-btn {
    width: 100%;
    max-width: 400px;
  }
}

@media (max-width: 768px) {
  .video-item {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;
    padding: 16px;
  }

  .thumbnail-small {
    width: 100%;
    height: 200px;
  }

  .video-info {
    text-align: center;
  }

  .clickable {
    font-size: 1.1rem;
  }

  .favorite-btn {
    width: 100%;
  }
}
</style>
