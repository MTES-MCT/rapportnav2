import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'

const useDeleteMissionCrewMutation = (missionId?: string): UseMutationResult<boolean, Error, number, unknown> => {

  const queryClient = useQueryClient()

  const deleteCrew = (id: number): Promise<boolean> =>
    axios.delete(`crews/${id}`).then(response => response.data)

  const mutation = useMutation({
    mutationFn: deleteCrew,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['missionCrew', missionId] })
    }
  })
  return mutation
}

export default useDeleteMissionCrewMutation
