import React from 'react'
import ReactDOM from 'react-dom/client'
// import reportWebVitals from './report-web-vitals'
import App from './app'
import * as Sentry from '@sentry/react'
import { createRoutesFromChildren, matchRoutes, useLocation, useNavigationType } from 'react-router-dom'
import { HttpClient, CaptureConsole, Debug } from "@sentry/integrations";

import 'react-toastify/dist/ReactToastify.css'
import 'rsuite/dist/rsuite.min.css'
import './assets/css/index.css'
import '@mtes-mct/monitor-ui/assets/stylesheets/rsuite-override.css'

Sentry.init({
    dsn: 'https://8857258f9f1549968b13e15759bdf2bc@sentry.incubateur.net/121',
    integrations: [
        new Sentry.BrowserTracing({
            // See docs for support of different versions of variation of react router
            // https://docs.sentry.io/platforms/javascript/guides/react/configuration/integrations/react-router/
            routingInstrumentation: Sentry.reactRouterV6Instrumentation(
                React.useEffect,
                useLocation,
                useNavigationType,
                createRoutesFromChildren,
                matchRoutes
            )
        }),
        new Sentry.Replay(),
        new HttpClient(),
        new Debug(),
        new CaptureConsole(),
    ],
    replaysOnErrorSampleRate: 1.0,
    replaysSessionSampleRate: 0.1,
    tracesSampleRate: 1.0
})

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement)

root.render(
    <React.StrictMode>
        <App/>
    </React.StrictMode>
)

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
// reportWebVitals()
