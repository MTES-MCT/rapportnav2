import { QueryCache, QueryClient } from '@tanstack/react-query'
import { persistQueryClient } from '@tanstack/react-query-persist-client'
import { createSyncStoragePersister } from '@tanstack/query-sync-storage-persister'
import { logSoftError } from '@mtes-mct/monitor-ui'

export const queryClient = new QueryClient({
  queryCache: new QueryCache({
    onError: error => {
      // https://tkdodo.eu/blog/breaking-react-querys-api-on-purpose
      debugger
      logSoftError({
        isSideWindowError: false,
        message: error.message,
        userMessage: `Une erreur s'est produite lors de l'enregistrement. Si l'erreur persiste, veuillez contacter l'équipe RapportNav/SNC3.`
      })
    }
  }),
  defaultOptions: {
    queries: {
      retry: true,
      staleTime: 2 * 60 * 1000,
      gcTime: 1000 * 60 * 60 * 24 // 24 hours
    },
    mutations: {
      networkMode: 'offlineFirst',
      retry: 1
    }
  }
})

export const localStoragePersister = createSyncStoragePersister({
  storage: window.localStorage
})

const persistClient = persistQueryClient({
  queryClient,
  persister: localStoragePersister,
  // bump this to forcefully wipe users cache after a new deployment
  // be aware it can wipe some of their offline data, use with caution
  buster: '1'
})

export default persistClient
