import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { MissionNavAction } from '../types/mission-action'

const useCreateMissionActionMutation = (
  missionId: number
): UseMutationResult<MissionNavAction, Error, MissionNavAction, unknown> => {
  const queryClient = useQueryClient()

  const createAction = (action: MissionNavAction): Promise<MissionNavAction> =>
    axios.post(`missions/${missionId}/actions`, action).then(response => response.data)

  const mutation = useMutation({
    mutationFn: createAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['actions'] })
    }
  })
  return mutation
}

export default useCreateMissionActionMutation