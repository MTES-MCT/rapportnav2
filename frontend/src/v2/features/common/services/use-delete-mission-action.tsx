import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { actionsKeys, missionsKeys } from './query-keys.ts'
import { MissionAction, MissionNavAction } from '../types/mission-action.ts'
import { Mission2 } from '../types/mission-types.ts'
import { orderBy } from 'lodash'

const useDeleteActionMutation = (missionId: number): UseMutationResult<void, Error, string, unknown> => {
  const queryClient = useQueryClient()

  const deleteAction = (actionId: string): Promise<void> =>
    axios.delete(`missions/${missionId}/actions/${actionId}`).then(response => response.data)

  const mutation = useMutation({
    mutationFn: deleteAction,
    onMutate: async (updatedAction: MissionNavAction) => {
      await queryClient.cancelQueries({ queryKey: missionsKeys.byId(missionId) })

      const mission: Mission2 | undefined = queryClient.getQueryData(missionsKeys.byId(missionId))
      const previousActions = mission?.actions

      debugger
      // remove action from mission action list
      const actions = (mission?.actions ?? []).filter((action: MissionAction) => action.id !== updatedAction.id)
      queryClient.setQueryData(
        missionsKeys.byId(missionId),
        (mission: Mission2 | undefined) =>
          ({
            ...mission,
            actions: actions
          }) as Mission2
      )

      // remove action from cache
      queryClient.removeQueries({ queryKey: actionsKeys.byId(updatedAction.id) })

      // return context
      return { previousActions, action: updatedAction }
    },
    onSettled: () => {
      // refetch mission whether success or error
      queryClient.invalidateQueries({ queryKey: missionsKeys.byId(missionId) })
    }
  })
  return mutation
}

export default useDeleteActionMutation
