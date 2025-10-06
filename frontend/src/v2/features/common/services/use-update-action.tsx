import { onlineManager, useMutation, UseMutationResult } from '@tanstack/react-query'
import { orderBy } from 'lodash'
import queryClient, { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client'
import axios from '../../../../query-client/axios.ts'
import { MissionAction, MissionNavAction } from '../types/mission-action.ts'
import { Mission2 } from '../types/mission-types.ts'
import { NetworkSyncStatus } from '../types/network-types.ts'
import { OwnerType } from '../types/owner-type.ts'
import { actionsKeys, inquiriesKeys, missionsKeys } from './query-keys.ts'
import { fetchAction } from './use-action.tsx'

type UseUpdateActionInput = {
  ownerId: string
  ownerType: OwnerType
  action: MissionAction
}

const updateAction = ({ ownerId, action }: UseUpdateActionInput): Promise<MissionAction> =>
  axios.put(`owners/${ownerId}/actions/${action.id}`, action).then(response => response.data)

export const offlineUpdateActionDefaults = {
  mutationFn: updateAction,
  onMutate: async ({ ownerId, ownerType, action }: UseUpdateActionInput) => {
    // create optimistic action
    const isOnline = onlineManager.isOnline()
    let optimisticAction: MissionNavAction = {
      ...action,
      networkSyncStatus: isOnline ? NetworkSyncStatus.SYNC : NetworkSyncStatus.UNSYNC
    }

    await queryClient.cancelQueries({
      queryKey: ownerType === OwnerType.INQUIRY ? inquiriesKeys.byId(ownerId) : missionsKeys.byId(ownerId)
    })

    queryClient.setQueryData<Mission2>(
      ownerType === OwnerType.INQUIRY ? inquiriesKeys.byId(ownerId) : missionsKeys.byId(ownerId),
      old => {
        if (!old) return old
        // insert optimistic action at the right place in mission cache
        const actions = (old?.actions ?? []).map((action: MissionAction) =>
          action.id === optimisticAction.id ? optimisticAction : action
        )
        // sort the list again in case startDateTimes have changed
        const sortedActions = orderBy(actions, ['data.startDateTimeUtc'], ['desc'])
        return {
          ...old,
          actions: sortedActions
        }
      }
    )
    // set individual action query for the optimistic object
    queryClient.setQueryData(actionsKeys.byId(optimisticAction.id), optimisticAction)

    // ✅ Cancel existing 'create' mutations with same action id
    const cleanupMutations = (mutationKey, actionId, keepLatest = false) => {
      const mutations = queryClient
        .getMutationCache()
        .findAll({ mutationKey })
        .filter(m => m.state.status === 'pending' && m.state.variables?.action?.id === actionId)

      const toCleanup = keepLatest ? mutations.sort((a, b) => b.mutationId - a.mutationId).slice(1) : mutations

      toCleanup.forEach(m => (m.destroy?.(), mutationCache.remove(m as any)))
    }

    const mutationCache = queryClient.getMutationCache()
    cleanupMutations(actionsKeys.create(), optimisticAction.id)
    cleanupMutations(actionsKeys.update(), optimisticAction.id, true)

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
    await queryClient.setQueryData(actionsKeys.byId(serverResponse?.id), serverResponse)
  },
  onSettled: async (_data, _error, variables: UseUpdateActionInput, _context) => {
    await queryClient.invalidateQueries({
      queryKey: actionsKeys.byId(variables.action?.id)
    })

    await queryClient.invalidateQueries({
      queryKey:
        variables.ownerType === OwnerType.INQUIRY
          ? inquiriesKeys.byId(variables.ownerId)
          : missionsKeys.byId(variables.ownerId),
      type: 'all'
    })
  },
  scope: {
    id: `update-action` // scope to run mutations in serial and not in parallel
  }
}

export const onlineUpdateActionDefaults = {
  mutationFn: updateAction,
  onSettled: async (_data, _error, variables: UseUpdateActionInput, _context) => {
    await queryClient.invalidateQueries({
      queryKey: actionsKeys.byId(variables.action?.id),
      type: 'all'
    })
    await queryClient.invalidateQueries({
      queryKey:
        variables.ownerType === OwnerType.INQUIRY
          ? inquiriesKeys.byId(variables.ownerId)
          : missionsKeys.byId(variables.ownerId),
      type: 'all'
    })
  },
  scope: {
    id: `update-action` // scope to run mutations in serial and not in parallel
  }
}

const useUpdateActionMutation = (): UseMutationResult<MissionAction, Error, MissionAction, unknown> => {
  const mutation = useMutation({
    mutationKey: actionsKeys.update(),
    mutationFn: updateAction,
    scope: {
      id: `update-action`
    }
  })
  return mutation
}

export default useUpdateActionMutation
