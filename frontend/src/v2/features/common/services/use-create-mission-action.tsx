import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { MissionNavAction } from '../types/mission-action'
import { actionsKeys, missionsKeys } from './query-keys.ts'
import { useOnlineManager } from '../hooks/use-online-manager.tsx'
import { Mission2 } from '../types/mission-types.ts'
import { NetworkSyncStatus } from '../types/network-types.ts'
import { navigateToActionId } from '@router/routes.tsx'
import { useNavigate } from 'react-router-dom'
import queryClient from '../../../../query-client'

const createAction = async (action: MissionNavAction): Promise<MissionNavAction> =>
  axios.post(`missions/${action.missionId}/actions`, action)

queryClient.setMutationDefaults(actionsKeys.create(), {
  mutationFn: async data => {
    debugger
    return createAction(data)
  },
  onSettled: (data, error, variables, context) => {
    debugger
    queryClient.removeQueries({ queryKey: actionsKeys.create() })
    queryClient.invalidateQueries({ queryKey: actionsKeys.byId(data.data.id), exact: true })
    queryClient.invalidateQueries({ queryKey: missionsKeys.byId(data.data.missionId), exact: true })
  }
})

const useCreateMissionActionMutation = (
  missionId: number
): UseMutationResult<MissionNavAction, Error, MissionNavAction, unknown> => {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  const { isOnline } = useOnlineManager()

  const mutation = useMutation({
    mutationKey: actionsKeys.create(),
    mutationFn: createAction,
    onMutate: async (newAction: MissionNavAction) => {
      const optimisticAction = {
        ...newAction,
        networkSyncStatus: isOnline ? NetworkSyncStatus.SYNC : NetworkSyncStatus.UNSYNC
      }

      // cancel queries that should be invalidated
      await queryClient.cancelQueries({ queryKey: actionsKeys.byId(optimisticAction.id) })
      await queryClient.cancelQueries({ queryKey: missionsKeys.byId(missionId) })

      // get mission and actions from cache
      const mission: Mission2 | undefined = queryClient.getQueryData(missionsKeys.byId(missionId))
      const previousActions = mission?.actions

      // append new action in mission cache key
      queryClient.setQueryData(
        missionsKeys.byId(missionId),
        (mission: Mission2 | undefined) =>
          ({
            ...mission,
            actions: [optimisticAction, ...(previousActions || [])]
          }) as Mission2
      )

      // Set individual action query for the optimistic object
      queryClient.setQueryData(actionsKeys.byId(optimisticAction.id), optimisticAction)

      // redirect to the newly created action
      navigateToActionId(optimisticAction.id, navigate)
      debugger
      // return context
      return { previousActions, action: optimisticAction }
    },
    // Invalidate queries to fetch the latest data after mutation success
    onSuccess: (serverResponse, newAction, context) => {
      debugger
      // refetch action
      queryClient.invalidateQueries({ queryKey: actionsKeys.byId(context.action.id), exact: true })
    },
    onError: (_, __, context) => {
      debugger
      // reset action list in mission cache key
      queryClient.setQueryData(
        missionsKeys.byId(missionId),
        (mission: Mission2 | undefined) =>
          ({
            ...mission,
            actions: context.previousActions
          }) as Mission2
      )

      // remove cache key for optimistic action
      queryClient.removeQueries({ queryKey: actionsKeys.byId(context.action.id) })
    },
    onSettled: (data, error, variables, context) => {
      debugger
      queryClient.invalidateQueries({ queryKey: missionsKeys.byId(missionId), exact: true })
      queryClient.removeQueries({ queryKey: actionsKeys.create() })
    },
    scope: {
      id: 'create-action' // scope to run mutations in serial and not in parallel
    }
  })
  return mutation
}

export default useCreateMissionActionMutation
