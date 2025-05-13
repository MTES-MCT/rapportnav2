import { OmitKeyof, QueryCache, QueryClient } from '@tanstack/react-query'
import { persistQueryClient, PersistQueryClientOptions } from '@tanstack/react-query-persist-client'
import { createSyncStoragePersister } from '@tanstack/query-sync-storage-persister'

// Notes about stale and cache/gc time:
// - staleTime:
//   - how long data remains "fresh" before React Query considers it stale
//   - how long is this data considered up-to-date
// - gcTime:
//   - how long inactive/unused data remains in cache before being garbage collected
//   - how long should we keep this data around if nothing is using it
// - in offlineFirst apps:
//   - make gcTime much longer than staleTime:
//   - this ensures data remains available offline even if it's considered stale
export const DYNAMIC_DATA_STALE_TIME = 1000 * 60 * 5 // 5 minutes
export const DYNAMIC_DATA_GC_TIME = 1000 * 60 * 60 * 24 * 5 // 5 days
export const STATIC_DATA_STALE_TIME = 1000 * 60 * 60 * 24 * 3 // 3 days
export const STATIC_DATA_GC_TIME = 1000 * 60 * 60 * 24 * 15 // 15 days

export const queryClient = new QueryClient({
  queryCache: new QueryCache({}),
  defaultOptions: {
    queries: {
      networkMode: 'offlineFirst',
      retry: 0,
      staleTime: STATIC_DATA_STALE_TIME,
      gcTime: STATIC_DATA_GC_TIME,
      refetchOnMount: false,
      refetchOnWindowFocus: false,
      refetchOnReconnect: false
    }
  }
})

export const localStoragePersister = createSyncStoragePersister({
  storage: window.localStorage
})

export const persistOptions: OmitKeyof<PersistQueryClientOptions, 'queryClient'> = {
  persister: localStoragePersister,
  maxAge: STATIC_DATA_GC_TIME,
  buster: 'v1',
  hydrateOptions: {
    // When recovering from localStorage on page load
    defaultOptions: {
      queries: {
        // Important: don't refetch on hydration
        gcTime: STATIC_DATA_GC_TIME
      }
    }
  }
}

export default queryClient
