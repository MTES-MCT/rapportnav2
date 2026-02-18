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
    rollupOptions: {
      output: {
        format: 'es',
        manualChunks(id) {
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
        globPatterns: ['**/*.{js,css,html,ico,png,jpg,svg,webmanifest}']
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
