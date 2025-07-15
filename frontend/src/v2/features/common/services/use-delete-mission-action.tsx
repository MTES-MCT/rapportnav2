import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { actionsKeys, missionsKeys } from './query-keys.ts'
import { MissionAction } from '../types/mission-action.ts'
import { Mission2 } from '../types/mission-types.ts'
import queryClient from '../../../../query-client'

const deleteAction = ({ missionId, actionId }: { missionId: string; actionId: string }): Promise<void> =>
  axios.delete(`missions/${missionId}/actions/${actionId}`).then(response => response.data)

queryClient.setMutationDefaults(actionsKeys.delete(), {
  mutationFn: deleteAction,
  onMutate: async ({ missionId, actionId }: { missionId: string; actionId: string }) => {
    await queryClient.cancelQueries({ queryKey: missionsKeys.byId(missionId) })

    const mission: Mission2 | undefined = queryClient.getQueryData(missionsKeys.byId(missionId))
    const previousActions = mission?.actions

    // remove action from mission action list
    const actions = (mission?.actions ?? []).filter((action: MissionAction) => action.id !== actionId)
    queryClient.setQueryData(
      missionsKeys.byId(missionId),
      (mission: Mission2 | undefined) =>
        ({
          ...mission,
          actions: actions
        }) as Mission2
    )

    // remove action from cache
    queryClient.removeQueries({ queryKey: actionsKeys.byId(actionId) })

    // return context
    return { previousActions }
  },
  onSettled: async (_data, _error, variables, _context) => {
    // refetch mission whether success or error
    await queryClient.invalidateQueries({
      queryKey: missionsKeys.byId(variables.missionId),
      type: 'all'
    })
  },
  scope: {
    id: 'delete-action'
  }
})

const useDeleteActionMutation = (missionId: string): UseMutationResult<void, Error, string, unknown> => {
  const queryClient = useQueryClient()

  const mutation = useMutation({
    mutationKey: actionsKeys.delete(),
    mutationFn: deleteAction
  })
  return mutation
}

export default useDeleteActionMutation
