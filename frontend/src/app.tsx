import { Notifier } from '@mtes-mct/monitor-ui'
import * as Sentry from '@sentry/react'
import { FC, useEffect, Suspense, lazy } from 'react'
import queryClient, { persistOptions } from './query-client'
import { router } from './router/router'
import { PersistQueryClientProvider } from '@tanstack/react-query-persist-client'
import { setDefaultOptions } from 'date-fns'
import fr from 'date-fns/locale/fr'

import 'react-toastify/dist/ReactToastify.css'
import 'rsuite/dist/rsuite.min.css'
import ActionLoader from './v2/features/common/components/ui/action-loader.tsx'

// Lazy load heavy components
const UIThemeWrapper = lazy(() => import('./features/common/components/ui/ui-theme-wrapper'))
const RouterProvider = lazy(() => import('./router/router-provider'))
const ReactQueryDevtools = lazy(() =>
  import('@tanstack/react-query-devtools').then(module => ({
    default: module.ReactQueryDevtools
  }))
)
const ErrorPage = lazy(() => import('./pages/error-page.tsx'))

// Loading component
const AppLoading = () => <ActionLoader />

const App: FC = () => {
  useEffect(() => {
    setDefaultOptions({ locale: fr })
  }, [])

  // Show app immediately, don't wait for cache
  return (
    <Sentry.ErrorBoundary fallback={ErrorPage}>
      <UIThemeWrapper>
        <PersistQueryClientProvider //
          client={queryClient}
          persistOptions={persistOptions}
          onSuccess={() => {
            console.log('Persistence restored successfully')
          }}
          onError={() => {
            console.log('Persistence restored failed')
          }}
        >
          <Notifier />
          <Suspense fallback={<AppLoading />}>
            <RouterProvider router={router} />
          </Suspense>

          {/* Only load devtools in development */}
          {import.meta.env.DEV && (
            <Suspense fallback={null}>
              <ReactQueryDevtools initialIsOpen={false} />
            </Suspense>
          )}
        </PersistQueryClientProvider>
      </UIThemeWrapper>
    </Sentry.ErrorBoundary>
  )
}

export default Sentry.withProfiler(App)
