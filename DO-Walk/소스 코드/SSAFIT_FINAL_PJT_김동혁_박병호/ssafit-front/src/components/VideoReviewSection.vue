<template>
  <div class="video-review-section">
    <!-- ✏️ 리뷰 작성 폼 -->
    <div class="review-form">
      <h3>리뷰 작성</h3>
      <form @submit.prevent="writeReview">
        <input v-model="newReview.title" placeholder="리뷰 제목" required />
        <textarea v-model="newReview.content" placeholder="리뷰 내용" required></textarea>
        <button type="submit">등록</button>
      </form>
    </div>

    <!-- 📋 리뷰 목록 -->
    <div class="review-list">
      <h3>리뷰 목록</h3>
      <ul>
        <li v-for="review in reviews" :key="review.reviewNo">
          <strong>{{ review.title }}</strong>
          <p>{{ review.content }}</p>
          <small>작성자: {{ review.writer }} ({{ review.writerId }})</small>

          <!-- ✏️ 본인 리뷰일 경우 수정/삭제 가능 -->
          <div v-if="review.writerId === loginUser?.userId">
            <button @click="startEdit(review)">수정</button>
            <button @click="deleteReview(review.reviewNo)">삭제</button>
          </div>

          <!-- ✏️ 수정 입력 폼 -->
          <div v-if="editReview.reviewNo === review.reviewNo">
            <input v-model="editReview.title" />
            <textarea v-model="editReview.content"></textarea>
            <button @click="updateReview">수정 완료</button>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '../api/axios'
import { useRoute } from 'vue-router'

const route = useRoute()
const videoNo = parseInt(route.params.videoNo)
const reviews = ref([])
const loginUser = JSON.parse(localStorage.getItem('loginUser'))

const newReview = ref({
  reviewNo: null,
  title: '',
  content: '',
  writer: loginUser?.userName || '',
  writerId: loginUser?.userId || '',
  videoNo: videoNo,
  regDate: ''
})

const editReview = ref({
  reviewNo: null,
  videoNo: videoNo,
  writerId: loginUser?.userId || '',
  title: '',
  writer: loginUser?.userName || '',
  content: '',
  regDate: ''
})

const fetchReviews = async () => {
  const res = await axios.get(`/video/detail/${videoNo}`)
  reviews.value = res.data.reviewList
}

const writeReview = async () => {
  try {
    console.log('리뷰 등록 데이터:', newReview.value)
    await axios.post('/review/write', newReview.value)
    newReview.value.title = ''
    newReview.value.content = ''
    fetchReviews()
  } catch (err) {
    console.error('리뷰 등록 실패:', err)
    alert('리뷰 등록 중 오류가 발생했습니다.')
  }
}

const deleteReview = async (id) => {
  await axios.delete(`/review/delete/${id}`)
  fetchReviews()
}

const startEdit = (review) => {
  editReview.value = { ...review }
}

const updateReview = async () => {
  await axios.put('/review/update', editReview.value)
  editReview.value = {
    reviewNo: null,
    videoNo: videoNo,
    writerId: loginUser?.userId || '',
    title: '',
    writer: loginUser?.userName || '',
    content: '',
    regDate: ''
  }
  fetchReviews()
}

onMounted(fetchReviews)
</script>

<style scoped>
.video-review-section {
  padding: 20px;
  max-width: 700px;
  margin: 0 auto;
}
.review-form input,
.review-form textarea {
  width: 100%;
  margin-bottom: 10px;
}
.review-list ul {
  list-style: none;
  padding: 0;
}
.review-list li {
  margin-bottom: 20px;
  border-bottom: 1px solid #ccc;
  padding-bottom: 10px;
}
</style>
