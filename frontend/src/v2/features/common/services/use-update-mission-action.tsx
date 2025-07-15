import { onlineManager, useMutation, UseMutationResult } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { MissionAction, MissionNavAction } from '../types/mission-action'
import { actionsKeys, missionsKeys } from './query-keys.ts'
import { NetworkSyncStatus } from '../types/network-types.ts'
import { Mission2 } from '../types/mission-types.ts'
import { orderBy } from 'lodash'
import queryClient from '../../../../query-client'

const updateAction = (action: MissionAction): Promise<MissionAction> =>
  axios.put(`missions/${action.missionId}/actions/${action.id}`, action).then(response => response.data)

queryClient.setMutationDefaults(actionsKeys.update(), {
  mutationFn: updateAction,
  onMutate: async (updatedAction: MissionNavAction) => {
    // create optimistic action
    const isOnline = onlineManager.isOnline()
    let optimisticAction: MissionNavAction = {
      ...updatedAction,
      networkSyncStatus: isOnline ? NetworkSyncStatus.SYNC : NetworkSyncStatus.UNSYNC
    }

    await queryClient.cancelQueries({ queryKey: missionsKeys.byId(updatedAction.missionId) })

    // fetch mission and actions from cache
    const mission: Mission2 | undefined = queryClient.getQueryData(missionsKeys.byId(updatedAction.missionId))
    const previousActions = mission?.actions

    // insert optimistic action at the right place in mission cache
    const actions = (mission?.actions ?? []).map((action: MissionAction) =>
      action.id === optimisticAction.id ? optimisticAction : action
    )
    // sort the list again in case startDateTimes have changed
    const sortedActions = orderBy(actions, ['data.startDateTimeUtc'], ['desc'])

    // update mission cache
    queryClient.setQueryData(
      missionsKeys.byId(updatedAction.missionId),
      (mission: Mission2 | undefined) =>
        ({
          ...mission,
          actions: sortedActions
        }) as Mission2
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
    return { previousActions, action: optimisticAction }
  },

  onSettled: async (_data, _error, variables, _context) => {
    const { missionId, id: actionId } = variables

    try {
      await queryClient.invalidateQueries({
        queryKey: actionsKeys.byId(actionId),
        refetchType: 'all'
      })
    } catch (error) {
      // error because the action has been created online and cache key has no updater function
      // it's ok to ignore
    }

    await queryClient.invalidateQueries({
      queryKey: missionsKeys.byId(missionId),
      type: 'all'
    })
  },
  scope: {
    id: `update-action` // scope to run mutations in serial and not in parallel
  }
})

const useUpdateMissionActionMutation = (
  missionId: number,
  actionId?: string
): UseMutationResult<MissionAction, Error, MissionAction, unknown> => {
  const mutation = useMutation({
    mutationKey: actionsKeys.update(),
    mutationFn: updateAction
  })
  return mutation
}

export default useUpdateMissionActionMutation
