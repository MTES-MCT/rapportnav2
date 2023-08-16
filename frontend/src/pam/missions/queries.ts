import { Mission } from '../mission-types'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { authenticatedHttpClient } from '../../http-client'

export const FETCH_MISSIONS_QUERY_KEY = 'missions'

export const missionsKeys = {
  all: [FETCH_MISSIONS_QUERY_KEY] as const,
  lists: () => [...missionsKeys.all, 'list'] as const
  // list: (filters: string) => [...missionsKeys.lists(), { filters }] as const,
}

const reconcileMissions =
  (missionsFromCache: Mission[]) =>
  (missionsFromAPI: Mission[]): Mission[] => {
    return missionsFromAPI
  }

export const useMissions = (successCallback?: () => void, errorCallback?: () => void) => {
  const queryClient = useQueryClient()

  const missionsFromCache = queryClient.getQueryData(missionsKeys.lists()) as Mission[]

  const fetchMissions = async (reconcileFn?: any) => {
    let data = await authenticatedHttpClient.get('/api/v1/missions').json()
    if (reconcileFn) {
      data = reconcileFn(data)
    }
    return data
  }

  return useQuery({
    queryKey: missionsKeys.lists(),
    queryFn: () => fetchMissions(reconcileMissions(missionsFromCache))
  })
}
