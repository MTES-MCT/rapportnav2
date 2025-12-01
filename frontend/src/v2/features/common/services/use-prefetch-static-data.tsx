import { useIsRestoring, useQueryClient } from '@tanstack/react-query'
import { useEffect } from 'react'
import { STATIC_DATA_GC_TIME, STATIC_DATA_STALE_TIME } from '../../../../query-client'
import axios from '../../../../query-client/axios.ts'
import {
  administrationKeys,
  agentRolesKeys,
  agentServicesKeys,
  agentsKeys,
  natinfsKeys,
  resourcesKeys
} from './query-keys.ts'

export function usePrefetchStaticData() {
  const queryClient = useQueryClient()
  const isRestoring = useIsRestoring()

  useEffect(() => {
    if (isRestoring) return

    const prefetchAll = async () => {
      const referenceEndpoints = [
        { key: agentsKeys.all(), url: 'agents' },
        { key: agentRolesKeys.all(), url: 'agent_roles' },
        { key: agentServicesKeys.all(), url: 'crews' },
        { key: administrationKeys.all(), url: 'administrations' },
        { key: resourcesKeys.all(), url: 'resources' },
        { key: natinfsKeys.all(), url: 'natinfs' }
      ]

      for (const { key, url } of referenceEndpoints) {
        // no need to await the prefetch queries
        queryClient.prefetchQuery({
          queryKey: key,
          queryFn: () => axios.get(url).then(response => response.data),
          staleTime: STATIC_DATA_STALE_TIME,
          gcTime: STATIC_DATA_GC_TIME,
          networkMode: 'offlineFirst'
        })
      }
    }

    // prefetch all data, no need to await
    prefetchAll()
  }, [queryClient, isRestoring])
}
