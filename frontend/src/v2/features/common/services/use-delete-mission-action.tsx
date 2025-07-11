import { useMutation, UseMutationResult } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { actionsKeys, missionsKeys } from './query-keys.ts'
import { MissionAction } from '../types/mission-action.ts'
import { Mission2 } from '../types/mission-types.ts'
import queryClient from '../../../../query-client'

const deleteAction = ({ missionId, actionId }: { missionId: string; actionId: string }): Promise<void> =>
  axios.delete(`missions/${missionId}/actions/${actionId}`).then(response => response.data)

// Offline mutation defaults
export const offlineDeleteActionMutationDefaults = {
  mutationFn: deleteAction,
  onMutate: async ({ missionId, actionId }: { missionId: string; actionId: string }) => {
    await queryClient.cancelQueries({ queryKey: missionsKeys.byId(missionId) })

    const mission: Mission2 | undefined = queryClient.getQueryData(missionsKeys.byId(missionId))
    const previousActions = mission?.actions

    const actions = (mission?.actions ?? []).filter((action: MissionAction) => action.id !== actionId)
    queryClient.setQueryData(
      missionsKeys.byId(missionId),
      (mission: Mission2 | undefined) =>
        ({
          ...mission,
          actions: actions
        }) as Mission2
    )

    queryClient.removeQueries({ queryKey: actionsKeys.byId(actionId) })

    return { previousActions }
  },
  onSettled: async (_data: any, _error: any, variables: { missionId: string; actionId: string }, _context: any) => {
    await queryClient.invalidateQueries({
      queryKey: missionsKeys.byId(variables.missionId),
      type: 'all'
    })
  },
  scope: {
    id: 'delete-action'
  }
}

// Online mutation defaults
export const onlineDeleteActionMutationDefaults = {
  mutationFn: deleteAction,
  onSuccess: async (_data: any, variables: { missionId: string; actionId: string }) => {
    await queryClient.invalidateQueries({
      queryKey: missionsKeys.byId(variables.missionId),
      type: 'all'
    })
  },
  scope: {
    id: 'delete-action'
  }
}

const useDeleteActionMutation = (missionId: string): UseMutationResult<void, Error, string, unknown> => {
  const mutation = useMutation({
    mutationKey: actionsKeys.delete(),
    mutationFn: (data: any) => deleteAction({ missionId, actionId: data.actionId })
  })
  return mutation
}

export default useDeleteActionMutation
