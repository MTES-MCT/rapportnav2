import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { MissionAction, MissionNavAction } from '../types/mission-action'
import { actionsKeys, missionsKeys } from './query-keys.ts'
import { v4 as uuidv4 } from 'uuid'
import { NetworkSyncStatus } from '../types/network-types.ts'
import { Mission2 } from '../types/mission-types.ts'
import { groupBy, orderBy } from 'lodash'
import { useOnlineManager } from '../hooks/use-online-manager.tsx'

const useUpdateMissionActionMutation = (
  missionId: number,
  actionId?: string
): UseMutationResult<MissionAction, Error, MissionAction, unknown> => {
  const queryClient = useQueryClient()
  const { isOnline } = useOnlineManager()

  const updateAction = (action: MissionAction): Promise<MissionAction> =>
    axios.put(`missions/${missionId}/actions/${actionId}`, action).then(response => response.data)

  const mutation = useMutation({
    mutationFn: updateAction,
    onMutate: async (updatedAction: MissionNavAction) => {
      debugger
      let optimisticAction: MissionNavAction = {
        ...updatedAction,
        networkSyncStatus: isOnline ? NetworkSyncStatus.SYNC : NetworkSyncStatus.UNSYNC
      }

      await queryClient.cancelQueries({ queryKey: missionsKeys.byId(missionId) })

      const mission: Mission2 | undefined = queryClient.getQueryData(missionsKeys.byId(missionId))
      const previousActions = mission?.actions

      debugger
      const actions = (mission?.actions ?? []).map((action: MissionAction) =>
        action.id === optimisticAction.id ? optimisticAction : action
      )
      const sortedActions = orderBy(actions, ['data.startDateTimeUtc'], ['desc'])

      queryClient.setQueryData(
        missionsKeys.byId(missionId),
        (mission: Mission2 | undefined) =>
          ({
            ...mission,
            actions: sortedActions
          }) as Mission2
      )
      // Set individual action query for the optimistic object
      queryClient.setQueryData(actionsKeys.byId(optimisticAction.id), optimisticAction)

      // return context
      return { previousActions, action: optimisticAction }
    },
    onSettled: (data, error, variables, context) => {
      queryClient.invalidateQueries({ queryKey: missionsKeys.byId(missionId), exact: true })
      queryClient.invalidateQueries({ queryKey: actionsKeys.byId(context.action.id), exact: true })
    },
    scope: {
      id: `update-action-${actionId}`
    }
  })
  return mutation
}

export default useUpdateMissionActionMutation
