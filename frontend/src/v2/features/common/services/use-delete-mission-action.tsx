import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'

const useDeleteActionMutation = (missionId: number): UseMutationResult<void, Error, string, unknown> => {
  const queryClient = useQueryClient()

  const deleteAction = (actionId: string): Promise<void> =>
    axios.delete(`missions/${missionId}/actions/${actionId}`).then(response => response.data)

  const mutation = useMutation({
    mutationFn: deleteAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['actions'] })
    }
  })
  return mutation
}

export default useDeleteActionMutation
