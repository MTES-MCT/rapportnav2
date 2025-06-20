import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { MissionAction, MissionNavAction } from '../types/mission-action'
import { actionsKeys, crossControlsKeys, missionsKeys } from './query-keys.ts'
import { NetworkSyncStatus } from '../types/network-types.ts'
import { Mission2 } from '../types/mission-types.ts'
import { orderBy } from 'lodash'
import { useOnlineManager } from '../hooks/use-online-manager.tsx'
import queryClient from '../../../../query-client'

const updateAction = (action: MissionAction): Promise<MissionAction> =>
  axios.put(`missions/${action.missionId}/actions/${action.id}`, action).then(response => response.data)

queryClient.setMutationDefaults(actionsKeys.update(), {
  mutationFn: async data => {
    debugger
    return updateAction(data)
  },
  onSuccess: async (data, variables, context) => {
    debugger
    await queryClient.invalidateQueries({ queryKey: actionsKeys.byId(data.id) })
    await queryClient.invalidateQueries({ queryKey: missionsKeys.byId(data.missionId) })
    // queryClient.removeQueries({ queryKey: actionsKeys.updateAction() })
  },
  onSettled: async (data, error, variables, context) => {
    debugger
    await queryClient.invalidateQueries({ queryKey: actionsKeys.byId(data.id) })
    await queryClient.invalidateQueries({ queryKey: missionsKeys.byId(data.missionId) })
    // queryClient.removeQueries({ queryKey: actionsKeys.updateAction() })
  }
})

const useUpdateMissionActionMutation = (
  missionId: number,
  actionId?: string
): UseMutationResult<MissionAction, Error, MissionAction, unknown> => {
  const queryClient = useQueryClient()
  const { isOnline } = useOnlineManager()

  const mutation = useMutation({
    mutationKey: actionsKeys.update(),
    mutationFn: updateAction,
    onMutate: async (updatedAction: MissionNavAction) => {
      // create optimistic action
      let optimisticAction: MissionNavAction = {
        ...updatedAction,
        networkSyncStatus: isOnline ? NetworkSyncStatus.SYNC : NetworkSyncStatus.UNSYNC
      }

      await queryClient.cancelQueries({ queryKey: missionsKeys.byId(missionId) })

      // fetch mission and actions from cache
      const mission: Mission2 | undefined = queryClient.getQueryData(missionsKeys.byId(missionId))
      const previousActions = mission?.actions

      // insert optimistic action at the right place in mission cache
      const actions = (mission?.actions ?? []).map((action: MissionAction) =>
        action.id === optimisticAction.id ? optimisticAction : action
      )
      // sort the list again in case startDateTimes have changed
      const sortedActions = orderBy(actions, ['data.startDateTimeUtc'], ['desc'])

      // update mission cache
      queryClient.setQueryData(
        missionsKeys.byId(missionId),
        (mission: Mission2 | undefined) =>
          ({
            ...mission,
            actions: sortedActions
          }) as Mission2
      )
      // set individual action query for the optimistic object
      queryClient.setQueryData(actionsKeys.byId(optimisticAction.id), optimisticAction)

      // return context
      return { previousActions, action: optimisticAction }
    },
    onSettled: (data, error, variables, context) => {
      // invalidate mission and action whatever happens
      queryClient.invalidateQueries({ queryKey: crossControlsKeys.all() })
      queryClient.invalidateQueries({ queryKey: missionsKeys.byId(missionId), exact: true })
      queryClient.invalidateQueries({ queryKey: actionsKeys.byId(context.action.id), exact: true })
    },
    scope: {
      id: `update-action-${actionId}` // scope to run mutations in serial and not in parallel
    }
  })
  return mutation
}

export default useUpdateMissionActionMutation
