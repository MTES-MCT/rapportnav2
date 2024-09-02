import { useEffect } from 'react'
import {
  browserProfilingIntegration,
  browserTracingIntegration,
  init,
  reactRouterV6BrowserTracingIntegration,
  replayIntegration
} from '@sentry/react'
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
      captureConsoleIntegration(),
      browserTracingIntegration(),
      browserProfilingIntegration()
    ],
    replaysOnErrorSampleRate: 1.0,
    replaysSessionSampleRate: 0.1,

    // Set tracesSampleRate to 1.0 to capture 100%
    // of transactions for tracing.
    // We recommend adjusting this value in production
    tracesSampleRate: 1.0,

    // Set profilesSampleRate to 1.0 to profile every transaction.
    // Since profilesSampleRate is relative to tracesSampleRate,
    // the final profiling rate can be computed as tracesSampleRate * profilesSampleRate
    // For example, a tracesSampleRate of 0.5 and profilesSampleRate of 0.5 would
    // results in 25% of transactions being profiled (0.5*0.5=0.25)
    profilesSampleRate: 1.0
  })
}

export default initSentry
