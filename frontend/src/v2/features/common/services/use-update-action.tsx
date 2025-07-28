import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { MissionAction } from '../types/mission-action.ts'
import { OwnerType } from '../types/owner-type.ts'
import { actionsKeys, inquiriesKeys, missionsKeys } from './query-keys.ts'

const useUpdateMissionActionMutation = (
  ownerId: string,
  ownerType: OwnerType,
  actionId?: string
): UseMutationResult<MissionAction, Error, MissionAction, unknown> => {
  const queryClient = useQueryClient()

  const updateAction = (action: MissionAction): Promise<MissionAction> =>
    axios.put(`owners/${ownerId}/actions/${actionId}`, action).then(response => response.data)

  const mutation = useMutation({
    mutationFn: updateAction,
    onSuccess: () => {
      if (actionId) queryClient.invalidateQueries({ queryKey: actionsKeys.byId(actionId) })
      queryClient.invalidateQueries({
        queryKey: ownerType === OwnerType.INQUIRY ? inquiriesKeys.byId(ownerId) : missionsKeys.byId(ownerId)
      })
    },
    scope: {
      id: `update-action-${actionId}`
    }
  })
  return mutation
}

export default useUpdateMissionActionMutation
