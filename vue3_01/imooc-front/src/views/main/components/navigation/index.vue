<template>
  <mobile-navigation-vue v-if='isMobileTerminal' />
</template>

<script setup>
import { isMobileTerminal } from '@/utils/flexible'
import mobileNavigationVue from './mobile/index.vue'
import pcNavigationVue from './pc/index.vue'

</script>

<style scoped>

</style>
