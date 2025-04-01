import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { MissionNavAction } from '../types/mission-action'
import { actionsKeys, missionsKeys } from './query-keys.ts'
import { useOnlineManager } from '../hooks/use-online-manager.tsx'
import { Mission2 } from '../types/mission-types.ts'
import { NetworkSyncStatus } from '../types/network-types.ts'
import { navigateToActionId } from '@router/routes.tsx'
import { useNavigate } from 'react-router-dom'
import { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client'
import { ActionType } from '../types/action-type.ts'
import { fetchAction } from './use-mission-action.tsx'

const useCreateMissionActionMutation = (
  missionId: string
): UseMutationResult<MissionNavAction, Error, MissionNavAction, unknown> => {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  const { isOnline } = useOnlineManager()

  const createAction = async (action: MissionNavAction): Promise<MissionNavAction> =>
    axios.post(`missions/${missionId}/actions`, action)

  queryClient.setMutationDefaults(actionsKeys.create(), {
    mutationFn: createAction,
    onMutate: async (newAction: MissionNavAction) => {
      const optimisticAction = {
        ...newAction,
        status: newAction.actionType === ActionType.STATUS ? newAction.data.status : undefined,
        networkSyncStatus: isOnline ? NetworkSyncStatus.SYNC : NetworkSyncStatus.UNSYNC
      }

      // cancel queries that should be invalidated
      await queryClient.cancelQueries({ queryKey: actionsKeys.byId(optimisticAction.id) })
      await queryClient.cancelQueries({ queryKey: missionsKeys.byId(missionId) })

      // get mission and actions from cache
      const mission: Mission2 | undefined = queryClient.getQueryData(missionsKeys.byId(missionId))
      const previousActions = mission?.actions

      // append new action in mission cache key
      queryClient.setQueryData(
        missionsKeys.byId(missionId),
        (mission: Mission2 | undefined) =>
          ({
            ...mission,
            actions: [optimisticAction, ...(previousActions || [])]
          }) as Mission2
      )

      // Set individual action query for the optimistic object
      queryClient.setQueryData(actionsKeys.byId(optimisticAction.id), optimisticAction)

      // get this query with ensureQueryData, it will connect the queryFn to this cacheKey
      // otherwise, actions created offline haev no queryFn attached and cannot be refetched
      await queryClient.ensureQueryData({
        queryKey: actionsKeys.byId(optimisticAction.id),
        queryFn: () => fetchAction({ missionId, actionId: optimisticAction.id }),
        staleTime: DYNAMIC_DATA_STALE_TIME
      })

      // redirect to the newly created action
      navigateToActionId(optimisticAction.id, navigate)

      // return context
      return { previousActions, action: optimisticAction }
    },
    onSuccess: async (serverResponse, newAction, _context) => {
      await queryClient.invalidateQueries({
        queryKey: actionsKeys.byId(newAction.id),
        type: 'all'
      })
    },
    onError: (_, action, _context) => {
      // remove cache key for optimistic action because it couldn't be inserted
      queryClient.removeQueries({ queryKey: actionsKeys.byId(action.id) })
    },
    onSettled: async (_data, _error, variables, _context) => {
      // refetch mission in any case
      await queryClient.invalidateQueries({
        queryKey: missionsKeys.byId(variables.missionId),
        type: 'all'
      })
    },
    scope: {
      id: 'create-action' // scope to run mutations in serial and not in parallel
    }
  })

  const mutation = useMutation({
    mutationKey: actionsKeys.create(),
    mutationFn: createAction
  })
  return mutation
}

export default useCreateMissionActionMutation
