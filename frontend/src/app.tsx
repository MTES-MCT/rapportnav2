import { ApolloClient, ApolloProvider, NormalizedCacheObject } from '@apollo/client'
import { Notifier } from '@mtes-mct/monitor-ui'
import * as Sentry from '@sentry/react'
import { CachePersistor, LocalStorageWrapper } from 'apollo3-cache-persist'
import { FC, useEffect, useState } from 'react'
import apolloClient, { apolloCache } from './apollo-client/apollo-client.ts'
import UIThemeWrapper from './features/common/components/ui/ui-theme-wrapper'
import ErrorPage from './pages/error-page.tsx'
import { queryClient, persistOptions } from './query-client'
import { router } from './router/router'
import RouterProvider from './router/router-provider'
import { PersistQueryClientProvider } from '@tanstack/react-query-persist-client'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'

// import { FlagProvider } from '@unleash/proxy-client-react'
// import { IConfig } from 'unleash-proxy-client'

// const config: IConfig = {
//   url: 'http://localhost:3000/proxy', // Your front-end API URL or the Unleash proxy's URL (https://<proxy-url>/proxy)
//   clientKey: 'randomkey', // A client-side API token OR one of your proxy's designated client keys (previously known as proxy secrets)
//   refreshInterval: 60, // How often (in seconds) the client should poll the proxy for updates
//   appName: 'rapportnav2-flags', // The name of your application. It's only used for identifying your application
//   environment: 'local'
// }

const App: FC = () => {
  const [loading, setLoading] = useState(true)
  const [client, setClient] = useState<ApolloClient<NormalizedCacheObject>>(apolloClient)

  useEffect(() => {
    async function init() {
      // apollo cache
      const cache = apolloCache
      setClient(apolloClient)

      // apollo cache persist
      let newPersistor = new CachePersistor({
        cache,
        storage: new LocalStorageWrapper(window.localStorage),
        debug: true
      })
      await newPersistor.restore()

      setLoading(false) // Set loading to false after cache is restored
    }

    init().catch(console.error)
  }, [])

  if (loading) {
    // Render a loading indicator or anything you like
    return <div>Chargement...</div>
  }

  return (
    <Sentry.ErrorBoundary fallback={ErrorPage}>
      <ApolloProvider client={client}>
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
            {/*<FlagProvider config={config}>*/}
            <Notifier />
            <RouterProvider router={router} />
            {/*</FlagProvider>*/}
            <ReactQueryDevtools initialIsOpen={false} />
          </PersistQueryClientProvider>
        </UIThemeWrapper>
      </ApolloProvider>
    </Sentry.ErrorBoundary>
  )
}

export default Sentry.withProfiler(App)
