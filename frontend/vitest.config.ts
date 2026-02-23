import { defineConfig, mergeConfig } from 'vitest/config'
import viteConfig from './vite.config.ts'

export default mergeConfig(
  viteConfig,
  defineConfig({
    test: {
      include: ['src/**/*.{test,spec}.{js,mjs,cjs,ts,mts,cts,jsx,tsx}'],
      globals: true,
      environment: 'jsdom',
      setupFiles: ['./vitest.setup.ts'],
      server: {
        deps: {
          inline: ['@mtes-mct/monitor-ui', 'styled-components']
        }
      },
      // Suppress source map warnings for missing .map files
      sourcemapIgnoreList: () => true,
      // the following mapping should match those in tsconfig.json
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
)
