import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { MissionAction } from '../types/mission-action'
import { actionsKeys, missionsKeys } from './query-keys.ts'

const useUpdateMissionActionMutation = (
  missionId: number,
  actionId?: string
): UseMutationResult<MissionAction, Error, MissionAction, unknown> => {
  const queryClient = useQueryClient()

  const updateAction = (action: MissionAction): Promise<MissionAction> =>
    axios.put(`missions/${missionId}/actions/${actionId}`, action).then(response => response.data)

  const mutation = useMutation({
    mutationFn: updateAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: actionsKeys.byId(actionId) })
      queryClient.invalidateQueries({ queryKey: missionsKeys.byId(missionId) })
    }
  })
  return mutation
}

export default useUpdateMissionActionMutation
