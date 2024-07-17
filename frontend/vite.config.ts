import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import eslint from 'vite-plugin-eslint'
import importMetaEnv from '@import-meta-env/unplugin'

// https://vitejs.dev/config/
export default defineConfig({
  define: {
    // https://www.apollographql.com/docs/react/development-testing/reducing-bundle-size
    'globalThis.__DEV__': JSON.stringify(false)
  },
  build: {
    rollupOptions: {
      output: {
        format: 'es',
        manualChunks(id) {
          // Separate third-party dependencies into a common chunk
          if (id.includes('node_modules')) {
            return 'vendor'
          }
          if (id.includes('src/pam')) {
            return 'pam'
          }
        }
      }
    }
  },
  plugins: [
    react(),
    eslint(),
    importMetaEnv.vite({
      env: './.env',
      example: './.env.example'
    })
  ],
  server: {
    host: true,
    proxy: {
      // '/api': 'http://localhost:80',
      // '/graphql': 'http://localhost:80'
      '/api': 'http://backend:80',
      '/graphql': 'http://backend:80'
    }
  }
})
