// Sentry initialization should be imported first!
import { initializeSentry } from './sentry'
import React, { lazy, Suspense } from 'react'
import ReactDOM from 'react-dom/client'
// import reportWebVitals from './report-web-vitals'

// Import only critical CSS
import './assets/css/index.css'
import '@mtes-mct/monitor-ui/assets/stylesheets/rsuite-override.css'

initializeSentry().then(Sentry => {
  const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement, {
    onUncaughtError: Sentry.reactErrorHandler((error, errorInfo) => {
      console.warn('Uncaught error', error, errorInfo.componentStack)
    }),
    onCaughtError: Sentry.reactErrorHandler(),
    onRecoverableError: Sentry.reactErrorHandler()
  })

  const App = lazy(() => import('./app'))
  const ActionLoader = lazy(() => import('./v2/features/common/components/ui/action-loader.tsx'))

  root.render(
    <React.StrictMode>
      <Suspense fallback={<ActionLoader />}>
        <App />
      </Suspense>
    </React.StrictMode>
  )
})

// Optional: report web vitals in production only, and load it lazily.
// if (import.meta.env.PROD) {
//   import('./report-web-vitals').then(({ default: reportWebVitals }) => {
//     reportWebVitals(console.log)
//   })
// }
