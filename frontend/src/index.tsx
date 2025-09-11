// Sentry initialization should be imported first!
import { initializeSentry } from './sentry'
import React from 'react'
import ReactDOM from 'react-dom/client'
// import reportWebVitals from './report-web-vitals'
import App from './app'

import 'react-toastify/dist/ReactToastify.css'
import 'rsuite/dist/rsuite.min.css'
import './assets/css/index.css'
import '@mtes-mct/monitor-ui/assets/stylesheets/rsuite-override.css'
import { setDefaultOptions } from 'date-fns'
import { fr } from 'date-fns/locale'

// setup dates in French
setDefaultOptions({ locale: fr })

initializeSentry().then(Sentry => {
  const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement, {
    onUncaughtError: Sentry.reactErrorHandler((error, errorInfo) => {
      console.warn('Uncaught error', error, errorInfo.componentStack)
    }),
    onCaughtError: Sentry.reactErrorHandler(),
    onRecoverableError: Sentry.reactErrorHandler()
  })

  root.render(
    <React.StrictMode>
      <App />
    </React.StrictMode>
  )
})

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
// reportWebVitals()
