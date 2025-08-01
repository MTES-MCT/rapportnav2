import { useMutation, UseMutationResult } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { OwnerType } from '../types/owner-type.ts'
import { actionsKeys, missionsKeys, inquiriesKeys } from './query-keys.ts'
import { MissionAction } from '../types/mission-action.ts'
import { Mission2 } from '../types/mission-types.ts'
import queryClient from '../../../../query-client'

type UseDeleteActionInput = {
  ownerId: string
  ownerType: OwnerType
  actionId: string
}

const deleteAction = ({ ownerId, actionId }: UseDeleteActionInput): Promise<void> =>
  axios.delete(`owners/${ownerId}/actions/${actionId}`).then(response => response.data)

// Offline mutation defaults
export const offlineDeleteActionMutationDefaults = {
  mutationFn: deleteAction,
  onMutate: async ({ ownerId, ownerType, actionId }: UseDeleteActionInput) => {
    await queryClient.cancelQueries({
      queryKey: ownerType === OwnerType.INQUIRY ? inquiriesKeys.byId(ownerId) : missionsKeys.byId(ownerId)
    })

    const mission: Mission2 | undefined = queryClient.getQueryData(
      ownerType === OwnerType.INQUIRY ? inquiriesKeys.byId(ownerId) : missionsKeys.byId(ownerId)
    )
    const previousActions = mission?.actions

    const actions = (mission?.actions ?? []).filter((action: MissionAction) => action.id !== actionId)
    queryClient.setQueryData(
      ownerType === OwnerType.INQUIRY ? inquiriesKeys.byId(ownerId) : missionsKeys.byId(ownerId),
      (mission: Mission2 | undefined) =>
        ({
          ...mission,
          actions: actions
        }) as Mission2
    )

    queryClient.removeQueries({ queryKey: actionsKeys.byId(actionId) })

    return { previousActions }
  },
  onSettled: async (_data: any, _error: any, variables: UseDeleteActionInput, _context: any) => {
    debugger
    await queryClient.invalidateQueries({
      queryKey:
        variables.ownerType === OwnerType.INQUIRY
          ? inquiriesKeys.byId(variables.ownerId)
          : missionsKeys.byId(variables.ownerId),
      type: 'all'
    })
  },
  scope: {
    id: 'delete-action'
  }
}

// Online mutation defaults
export const onlineDeleteActionMutationDefaults = {
  mutationFn: deleteAction,
  onSuccess: async (_data: any, variables: UseDeleteActionInput) => {
    debugger
    await queryClient.invalidateQueries({
      queryKey:
        variables.ownerType === OwnerType.INQUIRY
          ? inquiriesKeys.byId(variables.ownerId)
          : missionsKeys.byId(variables.ownerId),
      type: 'all'
    })
  },
  scope: {
    id: 'delete-action'
  }
}

const useDeleteActionMutation = (): UseMutationResult<void, Error, string, unknown> => {
  const mutation = useMutation({
    mutationKey: actionsKeys.delete(),
    mutationFn: (data: any) => deleteAction(data)
  })
  return mutation
}

export default useDeleteActionMutation
