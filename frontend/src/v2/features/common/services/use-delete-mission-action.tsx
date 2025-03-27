import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { missionsKeys } from './query-keys.ts'

const useDeleteActionMutation = (missionId: number): UseMutationResult<void, Error, string, unknown> => {
  const queryClient = useQueryClient()

  const deleteAction = (actionId: string): Promise<void> =>
    axios.delete(`missions/${missionId}/actions/${actionId}`).then(response => response.data)

  const mutation = useMutation({
    mutationFn: deleteAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: missionsKeys.byId(missionId) })
    }
  })
  return mutation
}

export default useDeleteActionMutation
