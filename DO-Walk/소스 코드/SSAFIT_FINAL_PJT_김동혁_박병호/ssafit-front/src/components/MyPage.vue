<template>
  <div class="mypage">
    <!-- ✅ 유저 정보 -->
    <section class="user-info" v-if="loginUser">
      <div class="profile-header">
        <div class="profile-icon">👤</div>
        <h2>{{ loginUser.userName }} 님의 마이페이지</h2>
      </div>
      
      <div class="info-grid">
        <div class="info-item">
          <span class="label">ID</span>
          <span class="value">{{ loginUser.userId }}</span>
        </div>
        <div class="info-item">
          <span class="label">이메일</span>
          <span class="value">{{ loginUser.email }}</span>
        </div>
        <div class="info-item">
          <span class="label">이름</span>
          <span class="value">{{ loginUser.userName }}</span>
        </div>
        <div class="info-item">
          <span class="label">나이</span>
          <span class="value">{{ loginUser.age }} 세</span>
        </div>
        <div class="info-item">
          <span class="label">가입일</span>
          <span class="value">{{ loginUser.joinDate || '정보 없음' }}</span>
        </div>
      </div>

      <div class="action-buttons">
        <button class="edit-button" @click="openEditModal">
          <span class="icon">✏️</span>
          회원정보 수정
        </button>
        <button class="edit-button" @click="openIdPwModal">
          <span class="icon">🔒</span>
          비밀번호 변경
        </button>
      </div>
    </section>

    <!-- 🤖 현재 파트너 -->
    <section class="partner-info">
      <div class="section-header">
        <h3>현재 나의 AI 파트너는?</h3>
      </div>
      <div class="partner-content" v-if="partner">
        <img :src="partner.image" :alt="partner.name" class="partner-image">
        <div class="partner-details">
          <div class="partner-header">
            <span class="partner-emoji">{{ getPartnerEmoji(partner.id) }}</span>
            <span class="partner-name">{{ partner.name }}</span>
          </div>
          <p class="partner-tone">{{ partner.tone }}</p>
          <p class="partner-greeting">{{ partner.greeting }}</p>
        </div>
      </div>
      <div class="partner-empty" v-else>
        <span class="partner-emoji">🤖</span>
        <p>선택된 파트너가 없습니다</p>
      </div>
    </section>

    <div class="main-actions">
      <button @click="showReviewModal = true" class="action-button diary">
        <span class="icon">📘</span>
        내 일기 보기
      </button>
      <button @click="showFavoriteModal = true" class="action-button favorite">
        <span class="icon">❤️</span>
        찜한 영상 보기
      </button>
    </div>

    <!-- 나머지 모달 코드는 그대로 유지 -->
    <div v-if="showEditModal" class="modal-overlay">
      <div class="modal-box modal-pretty">
        <h3>✏️ 회원 정보 수정</h3>
        <div class="form-group">
          <label for="email">이메일</label>
          <input id="email" v-model="editUser.email" type="email" />
        </div>
        <div class="form-group">
          <label for="name">이름</label>
          <input id="name" v-model="editUser.userName" type="text" />
        </div>
        <div class="form-group">
          <label for="age">나이</label>
          <input id="age" v-model.number="editUser.age" type="number" />
        </div>
        <div class="edit-actions">
          <button class="save-btn" @click="updateUserInfo">
            <span class="icon">💾</span> 저장
          </button>
          <button class="cancel-btn" @click="cancelEdit">
            <span class="icon">❌</span> 취소
          </button>
        </div>
      </div>
    </div>

    <!-- 🔒 비밀번호 변경 모달 -->
    <div v-if="showIdPwModal" class="modal-overlay">
      <div class="modal-box modal-pretty">
        <h3>🔒 비밀번호 변경</h3>
        <div class="form-group">
          <label for="newPw">새 비밀번호</label>
          <input id="newPw" v-model="idPwForm.password" type="password" />
        </div>
        <div class="form-group">
          <label for="confirmPw">비밀번호 확인</label>
          <input id="confirmPw" v-model="idPwForm.confirmPassword" type="password" />
        </div>
        <div class="edit-actions">
          <button class="save-btn" @click="updatePassword">
            <span class="icon">💾</span> 저장 후 로그아웃
          </button>
          <button class="cancel-btn" @click="cancelPwEdit">
            <span class="icon">❌</span> 취소
          </button>
        </div>
      </div>
    </div>

    <!-- 📘 일기 모달 -->
    <div v-if="showReviewModal" class="modal-overlay">
      <div class="modal-box">
        <div class="modal-header">
          <h3>📘 나의 운동 일기장</h3>
          <p class="modal-subtitle">그 순간에 느꼈던 소중한 기록이에요.</p>
        </div>
        <div class="chat-box">
          <div v-for="review in reviews" :key="review.reviewNo" class="chat-msg">
            <div class="bubble">
              {{ review.content?.trim() || '(내용 없음)' }}
            </div>
            <div class="meta">
              <span class="date">🕒 {{ review.regDate }}</span>
            </div>
          </div>
        </div>
        <button @click="showReviewModal = false" class="close-button">
          <span class="icon">✖️</span> 닫기
        </button>
      </div>
    </div>

    <!-- ❤️ 찜 영상 모달 -->
    <div v-if="showFavoriteModal" class="modal-overlay">
      <div class="modal-box">
        <div class="modal-header">
          <h3>❤️ 찜한 영상 목록</h3>
          <p class="modal-subtitle">언젠가 꼭 다시 보고 싶어서 찜해 놓은 거 맞죠?</p>
        </div>
        <div v-if="favorites.length === 0" class="empty-state">
          <span class="empty-icon">💭</span>
          <p>아직 찜한 영상이 없습니다</p>
        </div>
        <ul v-else class="favorite-list">
          <li v-for="video in favorites" :key="video.videoNo" class="favorite-item">
            <img :src="video.thumbnailUrl" alt="썸네일" class="thumbnail" />
            <div class="video-info">
              <p @click="goToDetail(video.videoNo)" class="video-title">{{ video.videoTitle }}</p>
              <p class="meta">
                <span class="channel">{{ video.channelName }}</span>
                <span class="dot">•</span>
                <span class="views">조회수 {{ formatViewCount(video.viewCount) }}</span>
              </p>
              <button @click="removeFavorite(video.videoNo)" class="unfavorite-button">
                <span class="icon">💔</span> 찜 해제
              </button>
            </div>
          </li>
        </ul>
        <button @click="showFavoriteModal = false" class="close-button">
          <span class="icon">✖️</span> 닫기
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from '@/api/axios'
import { usePartnerStore } from '@/stores/partnerStore'
import { partners } from '@/data/partners'

const loginUser = ref(null)
const editUser = ref({})
const reviews = ref([])
const favorites = ref([])
const showReviewModal = ref(false)
const showFavoriteModal = ref(false)
const showEditModal = ref(false)
const showIdPwModal = ref(false)

const idPwForm = ref({
  password: '',
  confirmPassword: ''
})

const router = useRouter()
const partnerStore = usePartnerStore()
partnerStore.loadFromStorage()
const partner = partners.find(p => p.id === partnerStore.selectedPartnerId)

const fetchData = async () => {
  try {
    const userRes = await axios.get('/user/info')
    loginUser.value = userRes.data
    localStorage.setItem('loginUser', JSON.stringify(userRes.data))

    const reviewRes = await axios.get('/review/list')
    reviews.value = reviewRes.data.filter(r => r.writerId?.toLowerCase() === loginUser.value.userId.toLowerCase())

    const favRes = await axios.get('/favorite')
    favorites.value = favRes.data
  } catch (err) {
    console.error('데이터 불러오기 실패:', err)
  }
}

const goToDetail = (videoNo) => {
  router.push(`/video/${videoNo}`)
}

const removeFavorite = async (videoNo) => {
  try {
    await axios.delete(`/favorite/${videoNo}`)
    favorites.value = favorites.value.filter(v => v.videoNo !== videoNo)
  } catch (err) {
    console.error('찜 해제 실패:', err)
  }
}

const openEditModal = () => {
  editUser.value = JSON.parse(JSON.stringify(loginUser.value))
  showEditModal.value = true
}

const cancelEdit = () => {
  editUser.value = {}
  showEditModal.value = false
}

const updateUserInfo = async () => {
  try {
    await axios.put('/user/update', editUser.value)
    loginUser.value = { ...editUser.value }
    showEditModal.value = false
    alert('회원 정보가 수정되었습니다!')
  } catch (err) {
    console.error('회원 정보 수정 실패:', err)
    alert('수정 중 오류가 발생했습니다.')
  }
}

const openIdPwModal = () => {
  idPwForm.value.password = ''
  idPwForm.value.confirmPassword = ''
  showIdPwModal.value = true
}

const cancelPwEdit = () => {
  idPwForm.value.password = ''
  idPwForm.value.confirmPassword = ''
  showIdPwModal.value = false
}

const updatePassword = async () => {
  const { password, confirmPassword } = idPwForm.value

  if (!password || !confirmPassword) {
    alert('비밀번호를 모두 입력해주세요.')
    return
  }

  if (password !== confirmPassword) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  }

  try {
    await axios.put('/user/password', { password })
    alert('비밀번호가 변경되었습니다. 다시 로그인해주세요.')
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('loginUser')
    router.push('/login')
  } catch (err) {
    console.error('비밀번호 변경 실패:', err)
    alert('변경 중 오류가 발생했습니다.')
  }
}

// 파트너 이모지 매핑 함수 추가
function getPartnerEmoji(partnerId) {
  const emojiMap = {
    'ironcoach': '💪',
    'cheerbuddy': '🌟',
    'zenmaster': '🧘',
    'sciencebot': '🔬',
    'darkknight': '🌙'
  }
  return emojiMap[partnerId] || '🤖'
}

// 조회수 포맷팅 함수 추가
function formatViewCount(count) {
  if (!count) return '0회'
  if (count >= 10000) {
    return Math.floor(count / 10000) + '만회'
  }
  if (count >= 1000) {
    return Math.floor(count / 1000) + '천회'
  }
  return count + '회'
}

onMounted(fetchData)
</script>

<style scoped>
.mypage {
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

.profile-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 32px;
}

.profile-icon {
  width: 64px;
  height: 64px;
  background: #fdf6f6;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  box-shadow: 0 8px 16px rgba(161, 118, 118, 0.15);
}

h2 {
  font-size: 2.5rem;
  color: #1a1a1a;
  font-weight: 600;
  margin: 0;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
  background: #fdf6f6;
  padding: 32px;
  border-radius: 24px;
  box-shadow: 0 8px 16px rgba(161, 118, 118, 0.1);
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.label {
  color: #666;
  font-size: 0.9rem;
  font-weight: 500;
}

.value {
  color: #1a1a1a;
  font-size: 1.1rem;
  font-weight: 600;
}

.action-buttons {
  display: flex;
  gap: 16px;
  margin-bottom: 48px;
}

.edit-button {
  flex: 1;
  padding: 16px;
  background: white;
  border: 2px solid #f0e6e6;
  border-radius: 16px;
  color: #1a1a1a;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s;
}

.edit-button:hover {
  background: #fdf6f6;
  border-color: #a17676;
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(161, 118, 118, 0.15);
}

.partner-info {
  background: #fdf6f6;
  border-radius: 24px;
  padding: 24px;
  margin: 24px 0;
}

.section-header h3 {
  font-size: 1.5rem;
  color: #1a1a1a;
  margin-bottom: 24px;
  text-align: center;
  font-weight: 600;
}

.partner-content {
  display: flex;
  gap: 24px;
  background: white;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 8px 16px rgba(161, 118, 118, 0.1);
}

.partner-image {
  width: 120px;
  height: 120px;
  border-radius: 60px;
  object-fit: cover;
  border: 3px solid #f0e6e6;
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.15);
}

.partner-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.partner-header {
  display: flex;
  align-items: center;
  gap: 12px;
}

.partner-emoji {
  font-size: 24px;
}

.partner-name {
  font-size: 1.4rem;
  font-weight: 600;
  color: #1a1a1a;
}

.partner-tone {
  font-size: 1rem;
  color: #666;
  line-height: 1.5;
}

.partner-greeting {
  font-size: 1rem;
  color: #a17676;
  font-style: italic;
  line-height: 1.5;
  margin-top: 8px;
  padding-left: 12px;
  border-left: 3px solid #f0e6e6;
}

.partner-empty {
  text-align: center;
  padding: 32px;
  background: white;
  border-radius: 16px;
  color: #666;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.partner-empty .partner-emoji {
  font-size: 48px;
}

.main-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
}

.action-button {
  padding: 24px;
  background: #fdf6f6;
  border: none;
  border-radius: 24px;
  color: #1a1a1a;
  font-size: 1.2rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  transition: all 0.3s;
}

.action-button:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(161, 118, 118, 0.2);
}

.action-button .icon {
  font-size: 1.5rem;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.modal-box {
  background: white;
  border-radius: 32px;
  padding: 32px;
  width: 90%;
  max-width: 800px;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 24px 48px rgba(0, 0, 0, 0.2);
}

.modal-header {
  text-align: center;
  margin-bottom: 32px;
}

.modal-header h3 {
  font-size: 2rem;
  color: #1a1a1a;
  margin-bottom: 8px;
  font-weight: 600;
}

.modal-subtitle {
  color: #666;
  font-size: 1rem;
}

.form-group {
  margin-bottom: 24px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #666;
  font-size: 0.9rem;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #f0e6e6;
  border-radius: 12px;
  font-size: 1rem;
  transition: all 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #a17676;
  box-shadow: 0 0 0 2px rgba(161, 118, 118, 0.1);
}

.edit-actions {
  display: flex;
  gap: 16px;
  margin-top: 32px;
}

.save-btn,
.cancel-btn {
  flex: 1;
  padding: 12px;
  border-radius: 12px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s;
}

.save-btn {
  background: #a17676;
  color: white;
}

.save-btn:hover {
  background: #8a6363;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.2);
}

.cancel-btn {
  background: white;
  border: 2px solid #f0e6e6;
  color: #666;
}

.cancel-btn:hover {
  border-color: #a17676;
  color: #a17676;
  transform: translateY(-2px);
}

.chat-box {
  display: flex;
  flex-direction: column;
  gap: 24px;
  margin: 32px 0;
}

.chat-msg .bubble {
  background: #fdf6f6;
  padding: 20px 24px;
  border-radius: 20px;
  color: #1a1a1a;
  line-height: 1.6;
  font-size: 1rem;
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.1);
}

.chat-msg .meta {
  margin-top: 8px;
  padding-left: 12px;
  color: #666;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 8px;
}

.favorite-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
  margin: 32px 0;
}

.favorite-item {
  display: flex;
  gap: 24px;
  padding: 24px;
  background: #fdf6f6;
  border-radius: 20px;
  transition: all 0.3s;
}

.favorite-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(161, 118, 118, 0.15);
}

.thumbnail {
  width: 200px;
  height: 112px;
  object-fit: cover;
  border-radius: 12px;
}

.video-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.video-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 8px;
  cursor: pointer;
  transition: color 0.3s;
}

.video-title:hover {
  color: #a17676;
}

.meta {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 16px;
}

.dot {
  font-size: 0.5rem;
  margin: 0 4px;
}

.unfavorite-button {
  align-self: flex-start;
  padding: 8px 16px;
  background: white;
  border: 2px solid #f0e6e6;
  border-radius: 12px;
  color: #666;
  font-size: 0.9rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.3s;
}

.unfavorite-button:hover {
  border-color: #a17676;
  color: #a17676;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.1);
}

.close-button {
  width: 100%;
  padding: 16px;
  background: #a17676;
  border: none;
  border-radius: 16px;
  color: white;
  font-size: 1rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s;
  margin-top: 32px;
}

.close-button:hover {
  background: #8a6363;
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(161, 118, 118, 0.2);
}

.empty-state {
  text-align: center;
  padding: 48px 0;
  color: #666;
}

.empty-state .empty-icon {
  font-size: 48px;
  display: block;
  margin-bottom: 16px;
}

@media (max-width: 1024px) {
  .mypage {
    margin: 0;
    padding: 24px;
    border-radius: 0;
  }

  .profile-header {
    flex-direction: column;
    text-align: center;
  }

  h2 {
    font-size: 2rem;
  }

  .info-grid {
    grid-template-columns: 1fr;
    padding: 24px;
  }

  .action-buttons {
    flex-direction: column;
  }

  .main-actions {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .modal-box {
    width: 95%;
    padding: 24px;
    border-radius: 24px;
  }

  .favorite-item {
    flex-direction: column;
    padding: 16px;
  }

  .thumbnail {
    width: 100%;
    height: 200px;
  }

  .video-info {
    padding: 16px 0 0;
  }

  .unfavorite-button {
    width: 100%;
    justify-content: center;
  }

  .partner-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
    padding: 16px;
  }

  .partner-header {
    justify-content: center;
  }

  .partner-greeting {
    border-left: none;
    padding-left: 0;
    border-top: 3px solid #f0e6e6;
    padding-top: 12px;
  }
}
</style>
