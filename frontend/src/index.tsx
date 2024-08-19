import React from 'react'
import ReactDOM from 'react-dom/client'
// import reportWebVitals from './report-web-vitals'
import App from './app'
import initSentry from './sentry.ts'

import 'react-toastify/dist/ReactToastify.css'
import 'rsuite/dist/rsuite.min.css'
import './assets/css/index.css'
import '@mtes-mct/monitor-ui/assets/stylesheets/rsuite-override.css'
import { Provider } from 'react-redux'
import { store } from './redux/store.ts'

// setup sentry before starting
initSentry()

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement)
root.render(
  <React.StrictMode>
    <Provider store={store}>
      <App />
    </Provider>
  </React.StrictMode>
)

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
// reportWebVitals()
