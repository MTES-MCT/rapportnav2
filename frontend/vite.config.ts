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
        codeSplitting: {
          groups: [
            {
              name: 'react-vendor',
              test: /node_modules[\\/](react|react-dom|react-router-dom)/,
              priority: 30
            },
            {
              name: 'ui-vendor',
              test: /node_modules[\\/](rsuite|@mtes-mct[\\/]monitor-ui|styled-components)/,
              priority: 20
            },
            {
              name: 'vendor',
              test: /node_modules/,
              priority: 10
            }
          ]
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
        navigateFallback: null
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
