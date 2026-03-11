import { createAsyncStoragePersister } from '@tanstack/query-async-storage-persister'
import { OmitKeyof, QueryCache, QueryClient } from '@tanstack/react-query'
import { PersistQueryClientOptions } from '@tanstack/react-query-persist-client'
import { logSoftError } from '@mtes-mct/monitor-ui'
import * as Sentry from '@sentry/react'
import { getOfflineMode } from '../v2/features/common/hooks/use-offline-mode.tsx'
import { actionsKeys, missionsKeys } from '../v2/features/common/services/query-keys.ts'
import {
  offlineDeleteActionMutationDefaults,
  onlineDeleteActionMutationDefaults
} from '../v2/features/common/services/use-delete-action.tsx'
import {
  offlineUpdateActionDefaults,
  onlineUpdateActionDefaults
} from '../v2/features/common/services/use-update-action.tsx'
import {
  offlineUpdateGeneralInfoMutationDefault,
  onlineUpdateGeneralInfoMutationDefault
} from '../v2/features/common/services/use-update-general-info.tsx'
import {
  offlineCreateActionDefaults,
  onlineCreateActionDefaults
} from '../v2/features/common/services/use-create-action.tsx'

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
export const DYNAMIC_DATA_STALE_TIME = 1000 * 60 * 3 // 3 minutes
export const DYNAMIC_DATA_GC_TIME = 1000 * 60 * 60 * 24 * 5 // 5 days
export const STATIC_DATA_STALE_TIME = 1000 * 60 * 60 * 24 * 3 // 3 days
export const STATIC_DATA_GC_TIME = 1000 * 60 * 60 * 24 * 15 // 15 days
export const HOURLY_TIME = 1000 * 60 * 60

/**
 * Handles API errors based on HTTP status code:
 * - 400 (Usage errors): User-correctable issues - show specific message, don't report to Sentry
 * - 500 (Server errors): Unexpected issues - report to Sentry, show generic message
 */
const handleApiError = (error: any, context: 'query' | 'mutation') => {
  const status = error?.status
  const problemDetail = error?.problemDetail
  const isUsageError = status === 400
  const isServerError = status >= 500

  // Skip Missing queryFn errors (React Query internal)
  if (/Error: Missing queryFn/i.test(error?.toString())) return

  // Always log to console for debugging
  console.error(error)

  // Report to Sentry for server errors and request errors (not usage errors)
  if (!isUsageError) {
    Sentry.captureException(error)
  }

  // Determine user message based on error type
  const defaultMessage =
    context === 'query'
      ? `Une erreur s'est produite lors du chargement des données. Si l'erreur persiste, veuillez contacter l'équipe RapportNav/SNC3.`
      : `Une erreur s'est produite lors de l'enregistrement. Si l'erreur persiste, veuillez contacter l'équipe RapportNav/SNC3.`

  const userMessage = isUsageError
    ? problemDetail?.detail || error.message || defaultMessage
    : isServerError
      ? defaultMessage
      : problemDetail?.detail || error.message || defaultMessage

  logSoftError({
    isSideWindowError: false,
    message: error.message,
    userMessage
  })
}

const queryClient = new QueryClient({
  queryCache: new QueryCache({
    onError: error => handleApiError(error, 'query')
  }),
  defaultOptions: {
    queries: {
      networkMode: 'offlineFirst',
      retry: 0,
      staleTime: STATIC_DATA_STALE_TIME,
      gcTime: STATIC_DATA_GC_TIME,
      refetchOnMount: false,
      refetchOnWindowFocus: false,
      refetchOnReconnect: false
    },
    mutations: {
      networkMode: 'online',
      retry: false,
      onError: error => handleApiError(error, 'mutation')
    }
  }
})

// register mutation defaults
// this is absolutely necessary to fire offline mutations after a page reload, otherwise they're lost
queryClient.setMutationDefaults(
  missionsKeys.update(),
  getOfflineMode() ? offlineUpdateGeneralInfoMutationDefault : onlineUpdateGeneralInfoMutationDefault
)
queryClient.setMutationDefaults(
  actionsKeys.create(),
  getOfflineMode() ? offlineCreateActionDefaults : onlineCreateActionDefaults
)
queryClient.setMutationDefaults(
  actionsKeys.update(),
  getOfflineMode() ? offlineUpdateActionDefaults : onlineUpdateActionDefaults
)
queryClient.setMutationDefaults(
  actionsKeys.delete(),
  getOfflineMode() ? offlineDeleteActionMutationDefaults : onlineDeleteActionMutationDefaults
)

export const localStoragePersister = createAsyncStoragePersister({
  storage: window.localStorage
})

export const persistOptions: OmitKeyof<PersistQueryClientOptions, 'queryClient'> = {
  persister: localStoragePersister,
  maxAge: STATIC_DATA_GC_TIME,
  buster: 'v4', // bump this to force wipe user cache
  hydrateOptions: {
    // When recovering from localStorage on page load
    defaultOptions: {
      queries: {
        // Important: don't refetch on hydration
        gcTime: STATIC_DATA_GC_TIME
      },
      mutations: {
        gcTime: DYNAMIC_DATA_GC_TIME
      }
    }
  }
}

// react-query devtools extension - enable this to debug prod
// declare global {
//   interface Window {
//     __TANSTACK_QUERY_CLIENT__: import('@tanstack/query-core').QueryClient
//   }
// }
// window.__TANSTACK_QUERY_CLIENT__ = queryClient

export default queryClient
