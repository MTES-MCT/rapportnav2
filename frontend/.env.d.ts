// https://vitejs.dev/guide/env-and-mode#intellisense-for-typescript
/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly FRONTEND_SENTRY_DSN: string
  readonly MAILTO_RAPPORTNAV_SUPPORT: string
  readonly MAILTO_SUPPORT_RESOURCE: string
  readonly SATI_ENABLED_SERVICES: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
