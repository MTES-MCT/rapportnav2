import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { MissionNavAction } from '../types/mission-action.ts'
import { OwnerType } from '../types/owner-type.ts'
import { inquiriesKeys, missionsKeys } from './query-keys.ts'

const useCreateActionMutation = (
  ownerId: string,
  ownerType: OwnerType
): UseMutationResult<MissionNavAction, Error, MissionNavAction, unknown> => {
  const queryClient = useQueryClient()

  const createAction = (action: MissionNavAction): Promise<MissionNavAction> =>
    axios.post(`owners/${ownerId}/actions`, action).then(response => response.data)

  const mutation = useMutation({
    mutationFn: createAction,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ownerType === OwnerType.INQUIRY ? inquiriesKeys.byId(ownerId) : missionsKeys.byId(ownerId)
      })
    },
    scope: {
      id: 'create-action'
    }
  })
  return mutation
}

export default useCreateActionMutation
