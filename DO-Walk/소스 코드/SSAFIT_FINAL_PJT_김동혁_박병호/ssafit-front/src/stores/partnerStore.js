import { defineStore } from 'pinia'

export const usePartnerStore = defineStore('partner', {
  state: () => ({
    selectedPartner: null
  }),
  getters: {
    selectedPartnerId: (state) => state.selectedPartner?.id
  },
  actions: {
    setPartner(partner) {
      this.selectedPartner = partner
      localStorage.setItem('partner', JSON.stringify(partner))
    },
    loadFromStorage() {
      const saved = localStorage.getItem('partner')
      if (saved) {
        this.selectedPartner = JSON.parse(saved)
      }
    }
  }
})
