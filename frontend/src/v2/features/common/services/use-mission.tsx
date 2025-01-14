import { skipToken, useQuery, useQueryClient } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { Mission2 } from '../types/mission-types'
import { actionKeys, missionKeys } from '../../../../query-client/query-keys.tsx'
import { useEffect, useRef } from 'react'

const useGetMissionQuery = (missionId?: number) => {
  const queryClient = useQueryClient()
  const updatedActionsRef = useRef<Set<number>>(new Set()) // Store updated action IDs

  const fetchMission = (): Promise<Mission2> => axios.get(`missions/${missionId}`).then(response => response.data)

  const query = useQuery<Mission2>({
    queryKey: missionKeys.detail(missionId),
    queryFn: missionId ? fetchMission : skipToken,
    networkMode: 'offlineFirst',
    staleTime: 2 * 60 * 1000
  })

  useEffect(() => {
    if (query.data?.actions) {
      query.data.actions.forEach(action => {
        if (!updatedActionsRef.current.has(action.id)) {
          const cacheData = queryClient.getQueryData(missionKeys.detail(missionId))
          debugger
          queryClient.setQueryData(actionKeys.detail(action.id), action)
          updatedActionsRef.current.add(action.id) // Mark this action as updated
        }
      })
    }
  }, [query.data, queryClient])

  return query
}

export default useGetMissionQuery
