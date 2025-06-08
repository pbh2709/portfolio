<template>
  <div v-if="video.videoNo" class="video-detail">
    <div class="video-header">
      <div class="video-meta">
        <h2 class="title">{{ video.videoTitle }}</h2>
        <div class="meta-info">
          <div class="channel-info">
            <span class="channel-name">{{ video.channelName }}</span>
          </div>
          <div class="view-info">
            <span class="view-count">조회수 {{ formatViewCount(video.viewCount) }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="video-container">
      <iframe
        class="video-player"
        width="100%"
        height="400"
        :src="youtubeUrl"
        frameborder="0"
        allowfullscreen
      ></iframe>
    </div>

    <ReviewInput :videoNo="video.videoNo" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from '@/api/axios'
import ReviewInput from './ReviewInput.vue'

const route = useRoute()
const video = ref({})
const youtubeUrl = ref('')
const videoNo = route.params.videoNo

onMounted(async () => {
  try {
    const { data } = await axios.get(`/video/detail/${videoNo}`)
    console.log('🎯 data from server:', data)

    video.value = data.video || data
    console.log('✅ video.value:', video.value)

    const id = getYoutubeId(video.value)
    youtubeUrl.value = `https://www.youtube.com/embed/${id}`
  } catch (error) {
    console.error('영상 정보를 불러오지 못했습니다.', error)
  }
})

function getYoutubeId(video) {
  if (video.videoId) return video.videoId
  if (video.videoUrl) {
    const match = video.videoUrl.match(/v=([^&]+)/)
    return match ? match[1] : 'M7lc1UVf-VE'
  }
  return 'M7lc1UVf-VE' // fallback
}

function formatViewCount(count) {
  if (!count) return '0'
  if (count >= 10000) {
    return Math.floor(count / 10000) + '만회'
  }
  if (count >= 1000) {
    return Math.floor(count / 1000) + '천회'
  }
  return count + '회'
}
</script>

<style scoped>
.video-detail {
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

.video-header {
  margin-bottom: 24px;
}

.video-meta {
  padding: 0 8px;
}

.title {
  font-size: 2rem;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 16px;
  line-height: 1.3;
}

.meta-info {
  display: flex;
  align-items: center;
  gap: 24px;
  color: #666;
  font-size: 1rem;
}

.channel-info {
  display: flex;
  align-items: center;
}

.channel-name {
  font-weight: 500;
}

.view-info {
  display: flex;
  align-items: center;
  color: #666;
}

.video-container {
  position: relative;
  width: 100%;
  padding-top: 56.25%; /* 16:9 Aspect Ratio */
  margin: 32px 0;
  border-radius: 16px;
  overflow: hidden;
  background: #f0e6e6;
  box-shadow: 0 8px 16px rgba(161, 118, 118, 0.1);
}

.video-player {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border: none;
  border-radius: 16px;
}

@media (max-width: 1024px) {
  .video-detail {
    margin: 0;
    border-radius: 0;
    padding: 24px;
  }

  .title {
    font-size: 1.5rem;
    margin-bottom: 12px;
  }

  .meta-info {
    font-size: 0.9rem;
    gap: 16px;
  }
}

@media (max-width: 768px) {
  .video-detail {
    padding: 16px;
  }

  .video-meta {
    padding: 0;
  }

  .title {
    font-size: 1.3rem;
  }

  .meta-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
}
</style>
