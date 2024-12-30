import react from '@vitejs/plugin-react'
import * as path from 'path'
import { defineConfig } from 'vitest/config'

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@common': path.resolve(__dirname, './src/features/common'),
      '@features': path.resolve(__dirname, './src/features'),
      '@pages': path.resolve(__dirname, './src/pages'),
      '@router': path.resolve(__dirname, './src/router')
    }
  },
  test: {
    include: ['src/**/*.{test,spec}.{js,mjs,cjs,ts,mts,cts,jsx,tsx}'],
    globals: true,
    environment: 'jsdom',
    server: {
      deps: {
        inline: ['@mtes-mct/monitor-ui']
      }
    },
    // the following mapping should match those in tsconfig.json
    alias: {
      '@common': path.resolve(__dirname, './src/features/common'),
      '@features': path.resolve(__dirname, './src/features'),
      '@pages': path.resolve(__dirname, './src/pages'),
      '@router': path.resolve(__dirname, './src/router')
    },
    coverage: {
      reportOnFailure: true,
      provider: 'istanbul',
      reporter: ['text', 'lcov', 'json-summary', 'json'],
      thresholds: {
        lines: 40,
        branches: 40,
        functions: 40,
        statements: 40
      }
      // exclude: ['src/v2/**/*.{js,mjs,cjs,ts,mts,cts,jsx,tsx}']
    }
  }
})
