import { QueryCache, QueryClient } from '@tanstack/react-query'
import { persistQueryClient } from '@tanstack/react-query-persist-client'
import { createSyncStoragePersister } from '@tanstack/query-sync-storage-persister'

export const queryClient = new QueryClient({
  queryCache: new QueryCache({
    onError: error => {
      // https://tkdodo.eu/blog/breaking-react-querys-api-on-purpose
      debugger
    }
  }),
  defaultOptions: {
    queries: {
      retry: true,
      staleTime: 2 * 60 * 1000,
      gcTime: 1000 * 60 * 60 * 24 // 24 hours
    }
  }
})

export const localStoragePersister = createSyncStoragePersister({
  storage: window.localStorage
})
// const sessionStoragePersister = createSyncStoragePersister({ storage: window.sessionStorage })

const persistClient = persistQueryClient({
  queryClient,
  persister: localStoragePersister,
  // bump this to forcefully wipe users cache after a new deployment
  // be aware it can wipe some of their offline data, use with caution
  buster: '1'
})

export default persistClient
