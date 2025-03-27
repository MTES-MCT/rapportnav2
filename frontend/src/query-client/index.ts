import { QueryCache, QueryClient } from '@tanstack/react-query'
import { persistQueryClient } from '@tanstack/react-query-persist-client'
import { createSyncStoragePersister } from '@tanstack/query-sync-storage-persister'

export const queryClient = new QueryClient({
  queryCache: new QueryCache({})
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
