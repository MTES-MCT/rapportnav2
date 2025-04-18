import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { MissionAction, MissionNavAction } from '../types/mission-action'
import { actionsKeys, missionsKeys } from './query-keys.ts'
import { useOnlineManager } from '../hooks/use-online-manager.tsx'
import { Mission2 } from '../types/mission-types.ts'
import { NetworkSyncStatus } from '../types/network-types.ts'
import { v4 as uuidv4 } from 'uuid'

const useCreateMissionActionMutation = (
  missionId: number
): UseMutationResult<MissionNavAction, Error, MissionNavAction, unknown> => {
  const queryClient = useQueryClient()

  const { isOnline } = useOnlineManager()

  const createAction = async (action: MissionNavAction): Promise<MissionNavAction> =>
    axios.post(`missions/${missionId}/actions`, action)

  const mutation = useMutation({
    mutationFn: createAction,
    onMutate: async (newAction: MissionNavAction) => {
      debugger
      const id = uuidv4() // Generate a UUID locally
      const optimisticAction = { ...newAction, id, networkSyncStatus: NetworkSyncStatus.UNSYNC }

      // to me on monday: it seems to fail here
      // await queryClient.cancelQueries({ queryKey: actionsKeys.byId(optimisticAction.id) })
      await queryClient.cancelQueries({ queryKey: missionsKeys.byId(missionId) })

      const mission: Mission2 | undefined = queryClient.getQueryData(missionsKeys.byId(missionId))
      const previousActions = mission?.actions

      debugger
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

      // return context
      return { previousActions, action: optimisticAction }
    },
    // Invalidate queries to fetch the latest data after mutation success
    onSuccess: (serverResponse, newAction, context) => {
      debugger
      if (navigator.onLine) {
        const serverAction = serverResponse.data

        // set new action with final id in cache
        queryClient.setQueryData(actionsKeys.byId(serverAction.id), {
          ...serverAction,
          networkSyncStatus: NetworkSyncStatus.SYNC
        })

        // remove the old individual action query if the ID changed
        if (context.action.id !== serverAction.id) {
          queryClient.removeQueries({ queryKey: actionsKeys.byId(context.action.id) })
        }

        // update action list
        // queryClient.setQueryData(
        //   missionsKeys.byId(missionId),
        //   (mission: Mission2 | undefined) =>
        //     ({
        //       ...mission,
        //       actions: (mission?.actions ?? []).map((action: MissionAction) =>
        //         action.id === context.action.id ? serverAction : action
        //       )
        //     }) as Mission2
        // )
        // refetch mission and action
        queryClient.invalidateQueries({ queryKey: missionsKeys.byId(missionId), exact: true })
        queryClient.invalidateQueries({ queryKey: actionsKeys.byId(serverAction.id), exact: true })
      }
    },
    onError: (_, __, context) => {
      debugger
      if (!navigator.onLine) {
        console.log('Offline: Action will remain unsynced until next synchronization.')
      } else {
        // Rollback for other errors
        console.error(`Mutation failed for action with ID: ${context.action?.id}`, _)

        // reset action list
        queryClient.setQueryData(
          missionsKeys.byId(missionId),
          (mission: Mission2 | undefined) =>
            ({
              ...mission,
              actions: context.previousActions
            }) as Mission2
        )

        // remvove cache key
        queryClient.removeQueries({ queryKey: actionsKeys.byId(context.action.id) })
      }
    },
    // Clean-up actions regardless of success or failure
    // onSettled: (data, error, variables, context) => {
    onSettled: (_: any) => {
      // queryClient.invalidateQueries({ queryKey: actionKeys.detail(), exact: true })
      // queryClient.invalidateQueries({ queryKey: actionsKeys.byId(missionId), exact: true })
    }
  })
  return mutation
}

export default useCreateMissionActionMutation
