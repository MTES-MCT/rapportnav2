import { onlineManager, useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { MissionAction, MissionNavAction } from '../types/mission-action.ts'
import { OwnerType } from '../types/owner-type.ts'
import { actionsKeys, inquiriesKeys, missionsKeys } from './query-keys.ts'
import { ActionType } from '../types/action-type.ts'
import { NetworkSyncStatus } from '../types/network-types.ts'
import queryClient, { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client'
import { Mission2 } from '../types/mission-types.ts'
import { fetchAction } from './use-action.tsx'

type UseCreateActionInput = { ownerId: string; ownerType: OwnerType; action: MissionAction }

const createAction = async ({ ownerId, action }: UseCreateActionInput): Promise<MissionNavAction> =>
  axios.post(`owners/${ownerId}/actions`, action)

export const offlineCreateActionDefaults = {
  mutationFn: createAction,
  onMutate: async ({ ownerId, ownerType, action }: UseCreateActionInput) => {
    debugger
    const isOnline = onlineManager.isOnline()

    const optimisticAction = {
      ...action,
      status: action.actionType === ActionType.STATUS ? action.data.status : undefined,
      networkSyncStatus: isOnline ? NetworkSyncStatus.SYNC : NetworkSyncStatus.UNSYNC
    }

    // cancel queries that should be invalidated
    await queryClient.cancelQueries({ queryKey: actionsKeys.byId(optimisticAction.id) })
    await queryClient.cancelQueries({
      queryKey: ownerType === OwnerType.INQUIRY ? inquiriesKeys.byId(ownerId) : missionsKeys.byId(ownerId)
    })

    // append new action in mission cache key
    await queryClient.setQueryData(
      ownerType === OwnerType.INQUIRY ? inquiriesKeys.byId(ownerId) : missionsKeys.byId(ownerId),
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
      queryFn: () => fetchAction({ ownerId, actionId: optimisticAction.id }),
      staleTime: DYNAMIC_DATA_STALE_TIME
    })

    // return context
    return { action: optimisticAction }
  },
  onSuccess: async (serverResponse, newAction, _context) => {
    debugger
    await queryClient.invalidateQueries({
      queryKey: actionsKeys.byId(newAction.id),
      type: 'all'
    })
  },
  onError: (_, action, _context) => {
    // remove cache key for optimistic action because it couldn't be inserted
    queryClient.removeQueries({ queryKey: actionsKeys.byId(action.id) })
  },
  onSettled: async (_data, _error, variables: UseCreateActionInput, _context) => {
    // refetch mission in any case
    debugger
    await queryClient.invalidateQueries({
      queryKey:
        variables.ownerType === OwnerType.INQUIRY
          ? inquiriesKeys.byId(variables.ownerId)
          : missionsKeys.byId(variables.ownerId),
      type: 'all'
    })
  },
  scope: {
    id: 'create-action' // scope to run mutations in serial and not in parallel
  }
}

export const onlineCreateActionDefaults = {
  mutationFn: createAction,
  onSettled: async (_data, _error, variables: UseCreateActionInput, _context) => {
    debugger
    await queryClient.invalidateQueries({
      queryKey:
        variables.ownerType === OwnerType.INQUIRY
          ? inquiriesKeys.byId(variables.ownerId)
          : missionsKeys.byId(variables.ownerId),
      type: 'all'
    })
  },
  scope: {
    id: 'create-action' // scope to run mutations in serial and not in parallel
  }
}

const useCreateActionMutation = (): UseMutationResult<MissionNavAction, Error, MissionNavAction, unknown> => {
  const queryClient = useQueryClient()

  const mutation = useMutation({
    mutationKey: actionsKeys.create(),
    mutationFn: createAction,
    scope: {
      id: 'create-action'
    }
  })
  return mutation
}

export default useCreateActionMutation
