import { useEffect } from 'react'
import { init, reactRouterV6BrowserTracingIntegration, replayIntegration } from '@sentry/react'
import { createRoutesFromChildren, matchRoutes, useLocation, useNavigationType } from 'react-router-dom'
import { captureConsoleIntegration, debugIntegration, httpClientIntegration } from '@sentry/integrations'
import packageJson from '../package.json'

const version = packageJson.version

const initSentry = () => {
  const FRONTEND_SENTRY_DSN = import.meta.env.FRONTEND_SENTRY_DSN
  const viteMode = import.meta.env.MODE
  const isDev = import.meta.env.DEV
  const release = version

  init({
    dsn: FRONTEND_SENTRY_DSN,
    environment: viteMode,
    enabled: !isDev,
    release: release,
    integrations: [
      reactRouterV6BrowserTracingIntegration({
        useEffect: useEffect,
        useLocation,
        useNavigationType,
        createRoutesFromChildren,
        matchRoutes
      }),
      replayIntegration(),
      httpClientIntegration(),
      debugIntegration(),
      captureConsoleIntegration()
    ],
    replaysOnErrorSampleRate: 1.0,
    replaysSessionSampleRate: 0.1,
    tracesSampleRate: 1.0
  })
}

export default initSentry
