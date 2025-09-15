import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import importMetaEnv from '@import-meta-env/unplugin'
import eslint from 'vite-plugin-eslint2'
import path from 'path'
import svgr from 'vite-plugin-svgr'

// https://vitejs.dev/config/
export default defineConfig({
  define: {
    // https://www.apollographql.com/docs/react/development-testing/reducing-bundle-size
    'globalThis.__DEV__': JSON.stringify(false)
  },
  resolve: {
    alias: {
      '@common': path.resolve(__dirname, './src/features/common'),
      '@features': path.resolve(__dirname, './src/features'),
      '@pages': path.resolve(__dirname, './src/pages'),
      '@router': path.resolve(__dirname, './src/router')
    }
  },
  build: {
    rollupOptions: {
      output: {
        format: 'es',
        manualChunks(id) {
          // Separate third-party dependencies into a common chunk
          // UI libraries
          if (id.includes('rsuite') || id.includes('@mtes-mct/monitor-ui')) {
            return 'ui-vendor'
          }
          // Sentry (separate chunk for optional loading)
          if (id.includes('@sentry')) {
            return 'sentry'
          }
          if (id.includes('node_modules')) {
            return 'vendor'
          }
          if (id.includes('src/features/pam')) {
            return 'pam'
          }
          if (id.includes('src/v2')) {
            return 'v2'
          }
        }
      }
    },
    // Optimize asset handling
    assetsInlineLimit: 4096, // Inline small assets
    cssCodeSplit: true,
    sourcemap: false
  },
  plugins: [
    react(),
    svgr(),
    eslint(),
    importMetaEnv.vite({
      env: './.env',
      example: './.env.example'
    })
  ],
  server: {
    host: true,
    proxy: {
      '/api': 'http://localhost:80',
      '/graphql': 'http://localhost:80'
    }
  }
})
