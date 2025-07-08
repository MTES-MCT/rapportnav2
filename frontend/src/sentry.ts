import { useEffect } from 'react'
import {
  browserProfilingIntegration,
  browserTracingIntegration,
  init,
  reactRouterV6BrowserTracingIntegration,
  replayIntegration,
  captureConsoleIntegration,
  httpClientIntegration
} from '@sentry/react'
import { createRoutesFromChildren, matchRoutes, useLocation, useNavigationType } from 'react-router-dom'
import packageJson from '../package.json'

const version = packageJson.version

enum SentryMode {
  UNKNOWN = 'unknown',
  LOCAL = 'local',
  INTEGRATION = 'integration',
  PRODUCTION = 'production'
}

/**
 * Determines the current Sentry mode based on the URL
 * @returns The appropriate SentryMode for the current environment
 */
function getSentryMode(): SentryMode {
  const currentUrl = window.location.href

  // Define URL patterns for each environment
  const urlPatterns: Record<SentryMode, RegExp> = {
    [SentryMode.PRODUCTION]: /rapport-nav\.din\.developpement-durable\.gouv\.fr/,
    [SentryMode.INTEGRATION]: /int-rapportnav-appli01/,
    [SentryMode.LOCAL]: /localhost|127\.0\.0\.1/,
    [SentryMode.UNKNOWN]: /^$/ // Empty pattern that won't match anything
  }

  // Find the first matching environment
  for (const [mode, pattern] of Object.entries(urlPatterns) as [SentryMode, RegExp][]) {
    if (pattern.test(currentUrl)) {
      return mode
    }
  }

  return SentryMode.UNKNOWN
}
const initSentry = () => {
  const FRONTEND_SENTRY_DSN = import.meta.env.FRONTEND_SENTRY_DSN

  // TODO should better leverage Vite Mode: https://vite.dev/guide/env-and-mode#modes
  const environment = getSentryMode()

  const isDev = import.meta.env.DEV
  const release = version

  init({
    dsn: FRONTEND_SENTRY_DSN,
    environment: environment,
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
