// https://vitejs.dev/guide/env-and-mode#intellisense-for-typescript
/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly FRONTEND_SENTRY_DSN: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
