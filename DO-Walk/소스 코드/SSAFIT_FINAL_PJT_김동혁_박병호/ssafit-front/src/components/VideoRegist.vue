<script setup>
import { ref } from 'vue'
import axios from '../api/axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const video = ref({
  videoTitle: '',
  channelName: '',
  fitnessPart: ''
})

const submitVideo = async () => {
  try {
    await axios.post('/videos', video.value)
    alert('등록 완료!')
    router.push('/videos')
  } catch (err) {
    alert('등록 실패')
    console.error(err)
  }
}
</script>
