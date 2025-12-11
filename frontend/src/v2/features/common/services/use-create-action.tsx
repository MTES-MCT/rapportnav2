import { onlineManager, useMutation, UseMutationResult } from '@tanstack/react-query'

import queryClient, { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client'
import axios from '../../../../query-client/axios.ts'
import { ActionInput, ActionType } from '../types/action-type.ts'
import { MissionNavAction } from '../types/mission-action.ts'
import { Mission2 } from '../types/mission-types.ts'
import { NetworkSyncStatus } from '../types/network-types.ts'
import { OwnerType } from '../types/owner-type.ts'
import { actionsKeys, inquiriesKeys, missionsKeys } from './query-keys.ts'
import { fetchAction } from './use-action.tsx'
import { ControlType } from '@common/types/control-types.ts'
import { TargetType } from '../types/target-types.ts'

const createAction = async ({ ownerId, action }: ActionInput): Promise<MissionNavAction> =>
  axios.post(`owners/${ownerId}/actions`, action).then(response => response.data)

export const offlineCreateActionDefaults = {
  mutationFn: createAction,
  onMutate: async ({ ownerId, ownerType, action }: ActionInput) => {
    const isOnline = onlineManager.isOnline()

    const optimisticAction = {
      ...action,
      networkSyncStatus: isOnline ? NetworkSyncStatus.SYNC : NetworkSyncStatus.UNSYNC,
      data: {
        targets: [
          {
            actionId: action.id,
            targetType: TargetType.DEFAULT,
            controls: [
              { controlType: ControlType.ADMINISTRATIVE },
              { controlType: ControlType.GENS_DE_MER },
              { controlType: ControlType.NAVIGATION },
              { controlType: ControlType.SECURITY }
            ]
          }
        ],
        ...action.data,
        status: action.actionType === ActionType.STATUS ? action.data.status : undefined
      }
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
    await queryClient.setQueryData(actionsKeys.byId(serverResponse?.data?.id), serverResponse?.data)
  },
  onError: (_, action, _context) => {
    // remove cache key for optimistic action because it couldn't be inserted
    queryClient.removeQueries({ queryKey: actionsKeys.byId(action.id) })
  },
  onSettled: async (_data: any, _error: any, variables: UseCreateActionInput, _context) => {
    // refetch mission in any case
    await queryClient.invalidateQueries({
      queryKey:
        variables.ownerType === OwnerType.INQUIRY
          ? inquiriesKeys.byId(variables.ownerId)
          : missionsKeys.byId(variables.ownerId),
      type: 'all'
    })
    await queryClient.invalidateQueries({
      queryKey: actionsKeys.byId(_data?.data.id)
    })
  },
  scope: {
    id: 'create-action' // scope to run mutations in serial and not in parallel
  }
}

export const onlineCreateActionDefaults = {
  mutationFn: createAction,
  onSuccess: async (serverResponse, newAction, _context) => {
    await queryClient.invalidateQueries({
      queryKey: actionsKeys.byId(serverResponse?.data?.id),
      type: 'all'
    })
  },
  onSettled: async (_data, _error, variables: ActionInput, _context) => {
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

const useCreateActionMutation = (): UseMutationResult<MissionNavAction, Error, ActionInput, unknown> => {
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
