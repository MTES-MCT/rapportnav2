import { onlineManager, useMutation, UseMutationResult } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { MissionAction, MissionNavAction } from '../types/mission-action.ts'
import { OwnerType } from '../types/owner-type.ts'
import { actionsKeys, inquiriesKeys, missionsKeys } from './query-keys.ts'
import { NetworkSyncStatus } from '../types/network-types.ts'
import queryClient from '../../../../query-client'
import { Mission2 } from '../types/mission-types.ts'
import { orderBy } from 'lodash'

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
    debugger
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
    const mutationCache = queryClient.getMutationCache()
    const pendingCreateMutations = mutationCache
      .findAll({ mutationKey: actionsKeys.create() })
      .filter(mutation => mutation.state.status === 'pending' && mutation.state.variables?.id === optimisticAction.id)

    for (const mutation of pendingCreateMutations) {
      // ✅ 1. Cancel retries
      mutation.destroy?.()

      // ✅ 2. Remove mutation from queue
      mutationCache.remove(mutation as any) // cast because remove expects Mutation<any, any, any, any>
    }

    // return context
    return { action: optimisticAction }
  },

  onSettled: async (_data, _error, variables: UseUpdateActionInput, _context) => {
    try {
      debugger
      await queryClient.invalidateQueries({
        queryKey: actionsKeys.byId(variables.action?.id),
        refetchType: 'all'
      })
    } catch (error) {
      // error because the action has been created online and cache key has no updater function
      // it's ok to ignore
    }

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
    debugger
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
