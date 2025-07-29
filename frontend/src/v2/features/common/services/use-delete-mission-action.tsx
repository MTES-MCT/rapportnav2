import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { OwnerType } from '../types/owner-type.ts'
import { inquiriesKeys, missionsKeys } from './query-keys.ts'

const useDeleteActionMutation = (
  ownerId: string,
  ownerType: OwnerType
): UseMutationResult<void, Error, string, unknown> => {
  const queryClient = useQueryClient()

  const deleteAction = (actionId: string): Promise<void> =>
    axios.delete(`owners/${ownerId}/actions/${actionId}`).then(response => response.data)

  const mutation = useMutation({
    mutationFn: deleteAction,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ownerType === OwnerType.INQUIRY ? inquiriesKeys.byId(ownerId) : missionsKeys.byId(ownerId)
      })
    },
    scope: {
      id: 'delete-action'
    }
  })
  return mutation
}

export default useDeleteActionMutation
