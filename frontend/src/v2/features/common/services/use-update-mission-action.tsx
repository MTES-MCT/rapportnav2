import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { MissionAction, MissionNavAction } from '../types/mission-action'
import { actionsKeys, missionsKeys } from './query-keys.ts'
import { v4 as uuidv4 } from 'uuid'
import { NetworkSyncStatus } from '../types/network-types.ts'
import { Mission2 } from '../types/mission-types.ts'
import { groupBy, orderBy } from 'lodash'

const useUpdateMissionActionMutation = (
  missionId: number,
  actionId?: string
): UseMutationResult<MissionAction, Error, MissionAction, unknown> => {
  const queryClient = useQueryClient()

  const updateAction = (action: MissionAction): Promise<MissionAction> =>
    axios.put(`missions/${missionId}/actions/${actionId}`, action).then(response => response.data)

  const mutation = useMutation({
    mutationFn: updateAction,
    onMutate: async (updatedAction: MissionNavAction) => {
      debugger

      // await queryClient.cancelQueries({ queryKey: actionsKeys.byId(optimisticAction.id) })
      await queryClient.cancelQueries({ queryKey: missionsKeys.byId(missionId) })

      const mission: Mission2 | undefined = queryClient.getQueryData(missionsKeys.byId(missionId))
      const previousActions = mission?.actions
      // queryClient.setQueryData(actionKeys.all(), [updatedAction, ...previousActions || []])
      debugger
      const actions = (mission?.actions ?? []).map((action: MissionAction) =>
        action.id === updatedAction.id ? updatedAction : action
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
      queryClient.setQueryData(actionsKeys.byId(updatedAction.id), updatedAction)

      // return context
      return { previousActions, action: updatedAction }
    },
    // onSuccess: (serverResponse, newAction, context) => {
    //   debugger
    //   const serverAction = serverResponse.data
    //
    //   queryClient.invalidateQueries({ queryKey: missionsKeys.byId(missionId), exact: true })
    //   queryClient.invalidateQueries({ queryKey: actionsKeys.byId(serverAction.id), exact: true })
    // },
    // onError: (_, __, context) => {
    //   debugger
    //   // Rollback for other errors
    //   console.error(`Mutation failed for action with ID: ${context.action?.id}`, _)
    // },
    //  onSettled: (data, error, variables, context)
    onSettled: (data, error, variables, context) => {
      queryClient.invalidateQueries({ queryKey: missionsKeys.byId(missionId), exact: true })
      debugger
      queryClient.invalidateQueries({ queryKey: actionsKeys.byId(context.action.id), exact: true })
    }
  })
  return mutation
}

export default useUpdateMissionActionMutation
