<template>
  <div class="chat-wrapper">
    <div class="mood-select">
      <div class="mood-section">
        <label for="mood">오늘 기분은 어때요?</label>
        <select id="mood" v-model="moodStore.selectedMood" @change="saveMood">
          <option disabled value="">선택해주세요</option>
          <option value="행복">행복한 기분</option>
          <option value="슬픔">슬픈 기분</option>
          <option value="화남">화나는 기분</option>
          <option value="불안">불안한 기분</option>
          <option value="지침">지친 기분</option>
        </select>
      </div>

      <div class="partner-info">
        <h3>지금 당신의 파트너는?</h3>
        <div v-if="partner" class="partner-content">
          <img :src="partner.image" :alt="partner.name" class="partner-image">
          <div class="partner-details">
            <p class="partner-name">{{ partner.name }}</p>
            <p class="partner-tone">{{ partner.tone }}</p>
          </div>
        </div>
        <div v-else class="partner-empty">
          <span class="empty-icon">🤖</span>
          <p>파트너를 선택해주세요</p>
        </div>
      </div>
    </div>

    <div class="chat-box" ref="chatBox">
      <div v-for="(msg, index) in chatHistory" :key="index" :class="['chat-msg', msg.sender]">
        <div class="bubble">{{ msg.text }}</div>
      </div>
    </div>

    <div class="input-area">
      <textarea
        v-model="question"
        placeholder="하고 싶은 말을 털어놓아도 좋아요."
        @keyup.enter.exact.prevent="submitQuestion"
      ></textarea>
      <button @click="submitQuestion" :disabled="!question.trim()">전송</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'
import { useRouter } from 'vue-router'
import axios from '@/api/axios'
import { useMoodStore } from '@/stores/moodStore'
import { usePartnerStore } from '@/stores/partnerStore'
import { partners } from '@/data/partners'

const router = useRouter()
const question = ref('')
const chatHistory = ref([])
const chatBox = ref(null)

const moodStore = useMoodStore()
const partnerStore = usePartnerStore()
const partner = ref(null)

onMounted(() => {
  partnerStore.loadFromStorage()
  moodStore.loadFromStorage?.()
  const selectedPartner = partners.find(p => p.id === partnerStore.selectedPartnerId)
  if (selectedPartner) {
    partner.value = selectedPartner
  }
})

watch(() => partnerStore.selectedPartnerId, (newId) => {
  const selectedPartner = partners.find(p => p.id === newId)
  if (selectedPartner) {
    partner.value = selectedPartner
  }
})

const saveMood = () => {
  moodStore.setMood(moodStore.selectedMood)
}

const submitQuestion = async () => {
  if (!partnerStore.selectedPartnerId) {
    alert('AI 파트너가 설정되지 않았어요. 먼저 선택해주세요!')
    router.push('/partner-select')
    return
  }

  if (!moodStore.selectedMood) {
    alert('오늘 기분을 먼저 선택해주세요!')
    router.push('/recommend')
    return
  }

  const userMsg = { sender: 'user', text: question.value }
  chatHistory.value.push(userMsg)

  const lastFew = chatHistory.value.slice(-6)
  const summarizedContext = lastFew
    .map(msg => `${msg.sender === 'user' ? '🙋‍♀️ 사용자' : '🤖 ' + partner.value.name}: ${msg.text}`)
    .join('\n')

  const getSpeakingStyle = (id) => {
    switch (id) {
      case 'ironcoach':
        return `
🧠 성격 요약:
넌코는 엄격하고 무뚝뚝한 군기 담당 운동코치다. 감정을 위로하지 않으며, 칭찬도 까다롭게 한다.  
감정을 해석할 수는 있지만 표현은 항상 거칠고 명령조다. 운동이나 멘탈 관리가 주제일 때만 열정적으로 개입한다.

🗣️ 말투 특징:
- 문장은 짧고 명령형이다.
- 감정적인 위로 대신 판단과 훈련을 말한다.
- “~해라”, “그건 안 된다”, “그 정도론 부족해” 같은 단정적 화법 사용
        `

      case 'cheerbuddy':
        return `
🧠 성격 요약:
할리는 하이텐션 칭찬 요정이다. 감정 표현이 강하고 격렬하며, 모든 상황에 긍정과 에너지로 반응한다.

🗣️ 말투 특징:
- “꺄악!”, “와아~”, “대박!” 등 감탄사와 이모지를 다량 사용
- 밝고 들뜬 말투, 끝을 늘리는 말버릇 있음 (너무 멋져~ 💖✨)
- 사용자 말에 무조건 리액션 먼저!
        `

      case 'zenmaster':
        return `
🧠 성격 요약:
쑤롱은 조용한 명상가이다. 감정을 고요히 받아들이고, 판단하지 않으며, 사색적으로 반응한다.

🗣️ 말투 특징:
- 자연물과 흐름을 비유로 사용한다.
- 단정하지 않고 여운을 남기는 말투
- “그 또한 흐름이죠” 같은 멈춤과 수용의 언어
        `

      case 'sciencebot':
        return `
🧠 성격 요약:
이든은 감정을 데이터처럼 분석하는 과학자형 AI다. 공감보다는 원리, 수치, 논리로 접근한다.

🗣️ 말투 특징:
- “세로토닌 수치”, “BDNF 분비” 같은 용어 사용
- 감정 해석은 관찰적이고 중립적이다
- 종결은 '결론'이나 '시사점' 형태로 끝난다
        `

      case 'darkknight':
        return `
🧠 성격 요약:
따토는 냉소적이고 말수가 적다. 감정을 직시하고, 회의적으로 받아들이지만 때로 짧은 위로를 남긴다.

🗣️ 말투 특징:
- “그래봤자...”, “어차피...” 같은 직설적 표현
- 문장은 짧고 비관적이며, 비유는 어둡다
- 마지막에 가끔 짧은 위로 한 줄 가능
        `

      default:
        return `
🧠 성격 요약:
감정을 유연하게 받아들이고, 상황에 맞춰 공감과 제안을 섞는 중립형 캐릭터다.

🗣️ 말투 특징:
- 따뜻하지만 도식적이지 않은 화법
- 질문에 부드럽게 반응하며 흐름을 이어간다
        `
    }
  }

  const prompt = `
너는 '${partner.value.name}'이라는 AI 파트너 캐릭터야.  
운동 코치가 아니라 감정을 공유하고 해석하는 인격체야.  
GPT처럼 반응하지 마. 캐릭터 그 자체로 말해줘.

🎭 캐릭터 성격 및 말투:
${getSpeakingStyle(partner.value.id)}

💬 최근 대화 흐름:
${summarizedContext}

🗣 지금 사용자의 요청:
"${question.value}"

📌 꼭 이 말에 답해줘. 질문을 무시하지 마!

📜 대화 규칙:
1. '${partner.value.name}:'으로 시작해.
2. 2~3문장. 짧고 자연스럽게.
3. 질문에 정확히 반응할 것. 동문서답 금지.
4. 캐릭터의 말투, 태도, 성격을 유지할 것.
5. 운동/식단/조언은 사용자가 꺼내지 않으면 언급 금지. 단, 캐릭터 성격에 맞게 자연스러운 흐름이면 허용.
  `

  try {
    const { data } = await axios.post('/ai/recommend', { prompt })
    chatHistory.value.push({ sender: 'ai', text: data })
    question.value = ''
    nextTick(() => {
      chatBox.value.scrollTop = chatBox.value.scrollHeight
    })
  } catch (err) {
    console.error('GPT 응답 실패:', err)
    chatHistory.value.push({ sender: 'ai', text: '⚠️ 응답에 실패했어요. 다시 시도해주세요.' })
  }
}
</script>



<style scoped>
.chat-wrapper {
  max-width: 800px;
  margin: 32px auto;
  display: flex;
  flex-direction: column;
  gap: 24px;
  background: white;
  padding: 32px;
  border-radius: 32px;
  box-shadow: 
    0 24px 48px rgba(0, 0, 0, 0.15),
    0 12px 24px rgba(0, 0, 0, 0.1),
    0 8px 16px rgba(162, 89, 77, 0.1);
}

.mood-select {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  padding: 24px;
  background: #fdf6f6;
  border-radius: 24px;
  align-items: center;
}

.mood-select .mood-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.mood-select label {
  font-size: 1.1rem;
  font-weight: 600;
  color: #1a1a1a;
}

.mood-select select {
  padding: 12px 16px;
  border: 2px solid #f0e6e6;
  border-radius: 12px;
  font-size: 1rem;
  background: white;
  color: #1a1a1a;
  transition: all 0.3s;
}

.mood-select select:focus {
  outline: none;
  border-color: #a17676;
  box-shadow: 0 0 0 2px rgba(161, 118, 118, 0.1);
}

.partner-info {
  background: white;
  padding: 20px;
  border-radius: 16px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.1);
}

.partner-info h3 {
  font-size: 1.1rem;
  color: #1a1a1a;
  margin-bottom: 16px;
  font-weight: 600;
}

.partner-content {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 8px;
  background: #fdf6f6;
  border-radius: 12px;
}

.partner-details {
  text-align: left;
}

.partner-name {
  font-size: 1.2rem;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.partner-tone {
  font-size: 0.9rem;
  color: #666;
  line-height: 1.4;
}

.partner-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px;
  color: #666;
}

.empty-icon {
  font-size: 32px;
}

.partner-image {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #f0e6e6;
}

.chat-box {
  height: 500px;
  overflow-y: auto;
  background: #fdf6f6;
  padding: 24px;
  border-radius: 24px;
  border: none;
  display: flex;
  flex-direction: column;
  gap: 16px;
  box-shadow: inset 0 2px 4px rgba(161, 118, 118, 0.1);
}

.chat-msg {
  display: flex;
  max-width: 80%;
}

.chat-msg.user {
  align-self: flex-end;
}

.chat-msg.ai {
  align-self: flex-start;
}

.bubble {
  padding: 16px 20px;
  border-radius: 20px;
  background: white;
  white-space: pre-line;
  color: #1a1a1a;
  line-height: 1.6;
  font-size: 1rem;
  box-shadow: 0 4px 12px rgba(161, 118, 118, 0.1);
}

.chat-msg.ai .bubble {
  background: #a17676;
  color: white;
}

.input-area {
  display: flex;
  gap: 16px;
}

textarea {
  flex: 1;
  min-height: 80px;
  padding: 16px;
  font-size: 1rem;
  border-radius: 16px;
  border: 2px solid #f0e6e6;
  resize: none;
  transition: all 0.3s;
  background: white;
  color: #1a1a1a;
}

textarea:focus {
  outline: none;
  border-color: #a17676;
  box-shadow: 0 0 0 2px rgba(161, 118, 118, 0.1);
}

button {
  padding: 16px 32px;
  background: #a17676;
  color: white;
  font-weight: 600;
  font-size: 1rem;
  border: none;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
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
}

@media (max-width: 1024px) {
  .chat-wrapper {
    margin: 16px;
    padding: 24px;
  }
}

@media (max-width: 768px) {
  .chat-wrapper {
    margin: 0;
    border-radius: 0;
  }

  .mood-select {
    grid-template-columns: 1fr;
  }
  
  .mood-select .mood-section {
    text-align: center;
  }
  
  .partner-info {
    padding: 16px;
  }

  .input-area {
    flex-direction: column;
  }

  button {
    width: 100%;
  }
}
</style>
