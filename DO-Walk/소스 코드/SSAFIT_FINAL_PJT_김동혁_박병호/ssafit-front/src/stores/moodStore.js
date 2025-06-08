import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useMoodStore = defineStore('mood', () => {
  const selectedMood = ref('')
  const setMood = (newMood) => {
    selectedMood.value = newMood
  }

  return { selectedMood, setMood }
})