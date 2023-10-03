import React, { useEffect, useState } from 'react'
import { ApolloClient, ApolloProvider, InMemoryCache, NormalizedCacheObject } from '@apollo/client'
import { CachePersistor, LocalStorageWrapper } from 'apollo3-cache-persist'

import { router } from './router/router'
import UIThemeWrapper from './ui/ui-theme-wrapper'
import apolloClient, { apolloCache } from './apollo-client'
import RouterProvider from './router/router-provider'

const App: React.FC = () => {
  const [loading, setLoading] = useState(true)
  const [client, setClient] = useState<ApolloClient<NormalizedCacheObject>>(apolloClient)

  useEffect(() => {
    async function init() {
      const cache = apolloCache
      let newPersistor = new CachePersistor({
        cache,
        storage: new LocalStorageWrapper(window.localStorage),
        debug: true
        // trigger: 'write'
      })

      await newPersistor.restore()
      // setPersistor(newPersistor);
      setClient(apolloClient)

      setLoading(false) // Set loading to false after cache is restored
    }

    init().catch(console.error)
  }, [])

  if (loading) {
    // Render a loading indicator or anything you like
    return <div>Loading...</div>
  }

  return (
    <ApolloProvider client={client}>
      <UIThemeWrapper>
        <RouterProvider router={router} />
      </UIThemeWrapper>
    </ApolloProvider>
  )
}

export default App
