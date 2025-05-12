import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { MissionNavAction } from '../types/mission-action'
import { missionsKeys } from './query-keys.ts'

const useCreateMissionActionMutation = (
  missionId: number
): UseMutationResult<MissionNavAction, Error, MissionNavAction, unknown> => {
  const queryClient = useQueryClient()

  const createAction = (action: MissionNavAction): Promise<MissionNavAction> =>
    axios.post(`missions/${missionId}/actions`, action).then(response => response.data)

  const mutation = useMutation({
    mutationFn: createAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: missionsKeys.byId(missionId) })
    },
    scope: {
      id: 'create-action'
    }
  })
  return mutation
}

export default useCreateMissionActionMutation
