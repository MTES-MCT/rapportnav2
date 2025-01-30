import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { MissionAction, MissionNavAction } from '../types/mission-action'
import { QUERY_KEY_GET_ACTIONS } from './use-mission-action-list.tsx'
import { actionKeys, missionKeys } from '../../../../query-client/query-keys.tsx'
import { Mission2 } from '../types/mission-types.ts'
import AuthToken from '@features/auth/utils/token.ts'

const useUpdateMissionActionMutation = (
  missionId: number,
  actionId?: string
): UseMutationResult<MissionAction, Error, MissionAction, unknown> => {
  const queryClient = useQueryClient()

  // const updateAction = (action: MissionAction): Promise<MissionAction> =>
  //   axios.put(`missions/${missionId}/actions/${actionId}`, action).then(response => response.data)

  const updateAction = async (action: MissionNavAction): Promise<MissionNavAction> => {
    debugger
    const token = new AuthToken().get()
    const auth = token ? `Bearer ${token}` : ''
    const response = await fetch(`/api/v2/missions/${missionId}/actions/${actionId}`, {
      method: 'PUT',
      body: JSON.stringify(action),
      credentials: 'omit',
      headers: { Accept: 'application/json', 'Content-type': 'application/json', Authorization: auth }
    })
    return await response.json()
    // axios.post(`missions/${missionId}/actions`, action).then(response => response.data)
  }

  const mutation = useMutation({
    mutationFn: updateAction,
    networkMode: 'offlineFirst',
    retry: 1,
    onMutate: async (updatedAction: MissionNavAction) => {
      debugger
      // await queryClient.cancelQueries({ queryKey: actionKeys.detail(actionId) })
      // update data for action
      queryClient.setQueryData(actionKeys.detail(actionId), {
        ...updatedAction,
        isNotYetSyncWithServer: !navigator.onLine
      })

      // update data in action list
      queryClient.setQueryData(
        missionKeys.detail(missionId),
        (mission: Mission2 | undefined) =>
          ({
            ...mission,
            actions: (mission?.actions ?? []).map((action: MissionAction) =>
              action.id === updatedAction.id ? updatedAction : action
            )
          }) as Mission2
      )
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: actionKeys.detail(actionId) })
      queryClient.invalidateQueries({ queryKey: missionKeys.detail(missionId) })
    }
  })
  return mutation
}

export default useUpdateMissionActionMutation
