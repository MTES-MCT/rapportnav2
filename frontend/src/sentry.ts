import { useEffect } from 'react'
import { createRoutesFromChildren, matchRoutes, useLocation, useNavigationType } from 'react-router-dom'
import packageJson from '../package.json'

const version = packageJson.version
const FRONTEND_SENTRY_DSN = import.meta.env.FRONTEND_SENTRY_DSN
const environment = import.meta.env.SPRING_PROFILES_ACTIVE
const isDev = import.meta.env.DEV

// Export the initialization function to be called from main
export const initializeSentry = async () => {
  const Sentry = await import('@sentry/react')

  Sentry.init({
    dsn: FRONTEND_SENTRY_DSN,
    environment: environment,
    enabled: !isDev,
    release: version,
    integrations: [
      Sentry.httpClientIntegration(),
      Sentry.captureConsoleIntegration(),
      Sentry.reactRouterV6BrowserTracingIntegration({
        useEffect: useEffect,
        useLocation,
        useNavigationType,
        createRoutesFromChildren,
        matchRoutes
      })
    ]
  })

  return Sentry
}
