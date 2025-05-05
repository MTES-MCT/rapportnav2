import { useQueryClient } from '@tanstack/react-query'
import { useEffect } from 'react'
import {
  administrationKeys,
  agentRolesKeys,
  agentServicesKeys,
  agentsKeys,
  controlUnitResourcesKeys,
  natinfsKeys
} from './query-keys.ts'
import axios from '../../../../query-client/axios.ts'
import { localStoragePersister, STATIC_DATA_GC_TIME, STATIC_DATA_STALE_TIME } from '../../../../query-client'

export function usePrefetchStaticData() {
  const queryClient = useQueryClient()

  useEffect(() => {
    const prefetchAll = async () => {
      const referenceEndpoints = [
        { key: agentsKeys.all(), url: 'agents' },
        { key: agentRolesKeys.all(), url: 'agent_roles' },
        { key: agentServicesKeys.all(), url: 'crews' },
        { key: administrationKeys.all(), url: 'administrations' },
        { key: controlUnitResourcesKeys.all(), url: 'resources' },
        { key: natinfsKeys.all(), url: 'natinfs' }
      ]

      for (const { key, url } of referenceEndpoints) {
        // no need to await the prefetch queries
        queryClient.fetchQuery({
          queryKey: key,
          queryFn: () => axios.get(url).then(response => response.data),
          staleTime: STATIC_DATA_STALE_TIME,
          gcTime: STATIC_DATA_GC_TIME,
          networkMode: 'offlineFirst',
          persister: localStoragePersister
        })
      }
    }

    // prefetch all data, no need to await
    prefetchAll()
  }, [queryClient])
}
