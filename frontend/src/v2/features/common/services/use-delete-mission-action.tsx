import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { QUERY_KEY_GET_ACTIONS } from './use-mission-action-list.tsx'
import { actionKeys, missionKeys } from '../../../../query-client/query-keys.tsx'
import { Mission2 } from '../types/mission-types.ts'
import { MissionAction } from '../types/mission-action.ts'

const useDeleteActionMutation = (
  missionId: number,
  actionId?: string
): UseMutationResult<void, Error, string, unknown> => {
  const queryClient = useQueryClient()

  const deleteAction = (actionId: string): Promise<void> =>
    axios.delete(`missions/${missionId}/actions/${actionId}`).then(response => response.data)

  const mutation = useMutation({
    mutationFn: deleteAction,
    networkMode: 'offlineFirst',
    onMutate: action => {
      queryClient.removeQueries({ queryKey: actionKeys.detail(actionId) })

      // update action list
      queryClient.setQueryData(
        missionKeys.detail(missionId),
        (mission: Mission2 | undefined) =>
          ({
            ...mission,
            actions: (mission?.actions ?? []).filter((action: MissionAction) => action.id !== actionId)
          }) as Mission2
      )
    },
    onSettled: () => {
      // queryClient.invalidateQueries({ queryKey: actionKeys.detail(actionId) })
      queryClient.invalidateQueries({ queryKey: missionKeys.detail(missionId) })
    }
  })

  return mutation
}

export default useDeleteActionMutation
