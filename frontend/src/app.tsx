import { ApolloClient, ApolloProvider, NormalizedCacheObject } from '@apollo/client'
import { Notifier } from '@mtes-mct/monitor-ui'
import * as Sentry from '@sentry/react'
import { CachePersistor, LocalStorageWrapper } from 'apollo3-cache-persist'
import { FC, useEffect, useState } from 'react'
import apolloClient, { apolloCache } from './apollo-client/apollo-client.ts'
import UIThemeWrapper from './features/common/components/ui/ui-theme-wrapper'
import queryClient, { persistOptions } from './query-client'
import { router } from './router/router'
import RouterProvider from './router/router-provider'
import { PersistQueryClientProvider } from '@tanstack/react-query-persist-client'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'

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
    <ApolloProvider client={client}>
      <UIThemeWrapper>
        <PersistQueryClientProvider //
          client={queryClient}
          persistOptions={persistOptions}
          onSuccess={() => {
            queryClient.resumePausedMutations().then(() => {
              console.log('Persistence restored successfully')
            })
          }}
          onError={() => {
            console.log('Persistence restored failed')
          }}
        >
          <Notifier />
          <RouterProvider router={router} />
          <ReactQueryDevtools initialIsOpen={false} />
        </PersistQueryClientProvider>
      </UIThemeWrapper>
    </ApolloProvider>
  )
}

export default Sentry.withProfiler(App)
