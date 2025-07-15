import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { missionsKeys } from './query-keys.ts'

const useDeleteMissionMutation = (missionId?: string): UseMutationResult<void, Error, void, unknown> => {
  const queryClient = useQueryClient()

  const deleteMission = (): Promise<void> => axios.delete(`missions/${missionId}`).then(response => response.data)

  const mutation = useMutation({
    mutationFn: deleteMission,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: missionsKeys.all() })
      if (missionId) queryClient.invalidateQueries({ queryKey: missionsKeys.byId(missionId) })
    },
    scope: {
      id: 'delete-mission'
    }
  })
  return mutation
}

export default useDeleteMissionMutation
