import { onlineManager, useMutation, UseMutationResult } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { MissionAction, MissionNavAction } from '../types/mission-action'
import { actionsKeys, missionsKeys } from './query-keys.ts'
import { Mission2 } from '../types/mission-types.ts'
import { NetworkSyncStatus } from '../types/network-types.ts'
import queryClient, { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client'
import { ActionType } from '../types/action-type.ts'
import { fetchAction } from './use-mission-action.tsx'

type UseCreateMissionActionInput = { missionId: string; action: MissionAction }

const createAction = async ({ missionId, action }: UseCreateMissionActionInput): Promise<MissionNavAction> =>
  axios.post(`missions/${missionId}/actions`, action)

export const offlineCreateMissionActionDefaults = {
  mutationFn: createAction,
  onMutate: async (input: UseCreateMissionActionInput) => {
    const { missionId, action } = input
    const isOnline = onlineManager.isOnline()

    const optimisticAction = {
      ...action,
      status: action.actionType === ActionType.STATUS ? action.data.status : undefined,
      networkSyncStatus: isOnline ? NetworkSyncStatus.SYNC : NetworkSyncStatus.UNSYNC
    }

    // cancel queries that should be invalidated
    await queryClient.cancelQueries({ queryKey: actionsKeys.byId(optimisticAction.id) })
    await queryClient.cancelQueries({ queryKey: missionsKeys.byId(missionId) })

    // append new action in mission cache key
    await queryClient.setQueryData(
      missionsKeys.byId(missionId),
      (mission: Mission2 | undefined) =>
        ({
          ...mission,
          actions: [optimisticAction, ...(mission?.actions || [])]
        }) as Mission2
    )

    // Set individual action query for the optimistic object
    await queryClient.setQueryData(actionsKeys.byId(optimisticAction.id), optimisticAction)

    // get this query with ensureQueryData, it will connect the queryFn to this cacheKey
    // otherwise, actions created offline have no queryFn attached and cannot be refetched
    await queryClient.ensureQueryData({
      queryKey: actionsKeys.byId(optimisticAction.id),
      queryFn: () => fetchAction({ missionId, actionId: optimisticAction.id }),
      staleTime: DYNAMIC_DATA_STALE_TIME
    })

    // return context
    return { action: optimisticAction }
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
}

export const onlineCreateMissionActionDefaults = {
  mutationFn: createAction,
  onSettled: async (_data, _error, variables, _context) => {
    await queryClient.invalidateQueries({
      queryKey: missionsKeys.byId(variables.missionId),
      type: 'all'
    })
  },
  scope: {
    id: 'create-action' // scope to run mutations in serial and not in parallel
  }
}

const useCreateMissionActionMutation = (): UseMutationResult<MissionNavAction, Error, MissionNavAction, unknown> => {
  const mutation = useMutation({
    mutationKey: actionsKeys.create(),
    mutationFn: createAction
  })
  return mutation
}

export default useCreateMissionActionMutation
