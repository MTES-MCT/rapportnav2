import importMetaEnv from '@import-meta-env/unplugin'
import react from '@vitejs/plugin-react'
import path from 'path'
import { defineConfig } from 'vite'
import eslint from 'vite-plugin-eslint2'
import { VitePWA } from 'vite-plugin-pwa'
import svgr from 'vite-plugin-svgr'

// https://vitejs.dev/config/
export default defineConfig({
  define: {
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
    rolldownOptions: {
      output: {
        format: 'es',
        manualChunks(id) {
          // Keep the login page (and the DSFR design system it imports) in a
          // dedicated async chunk loaded only on /login. Must come before the
          // node_modules and src/v2 branches below, otherwise the login module
          // and @gouvfr/dsfr would be folded into the eager vendor/v2 chunks.
          if (
            id.includes('src/v2/pages/login-page') ||
            id.includes('src/v2/features/auth/components/login') ||
            id.includes('@gouvfr/dsfr')
          ) {
            return 'login'
          }
          // Separate third-party dependencies into a common chunk
          // UI libraries
          if (id.includes('rsuite') || id.includes('@mtes-mct/monitor-ui')) {
            return 'ui-vendor'
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
    VitePWA({
      registerType: 'autoUpdate',
      injectRegister: 'auto',
      manifest: {
        name: 'RapportNav',
        short_name: 'RapportNav',
        lang: 'fr',
        icons: [
          {
            src: 'favicon.ico',
            sizes: '64x64 32x32 24x24 16x16',
            type: 'image/x-icon'
          }
        ]
      },
      workbox: {
        // defining cached files formats
        // Note: index.html is excluded because the backend injects a CSP nonce per request
        // Caching it would serve stale nonces, causing CSP violations and blank pages
        globPatterns: ['**/*.{js,css,ico,png,jpg,svg,webmanifest}'],
        navigateFallback: null,
      },
      devOptions: {
        enabled: true
        /* other options */
      }
    }),
    importMetaEnv.vite({
      env: './.env',
      example: './.env.example'
    })
  ],
  server: {
    host: true,
    sourcemapIgnoreList(sourcePath) {
      // Ignore source map warnings for node_modules packages
      return sourcePath.includes('node_modules')
    },
    proxy: {
      '/api': 'http://localhost:80',
      '/graphql': 'http://localhost:80'
    }
  },
  assetsInclude: ['**/*.odt']
})
