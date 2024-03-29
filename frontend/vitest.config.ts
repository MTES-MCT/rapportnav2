import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'

export default defineConfig({
    plugins: [react()],
    test: {
        include: ['src/**/*.{test,spec}.{js,mjs,cjs,ts,mts,cts,jsx,tsx}'],
        globals: true,
        environment: 'jsdom',
        server: {
            deps: {
                inline: ['@mtes-mct/monitor-ui']
            }
        },
        coverage: {
            reportOnFailure: true,
            provider: "istanbul",
            reporter: ['text', 'lcov', 'json-summary', 'json'],
            thresholds: {
                lines: 70,
                branches: 70,
                functions: 70,
                statements: 70
            }
        },
    }
})
