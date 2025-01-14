import { onlineManager, useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'
import { v4 as uuidv4 } from 'uuid'

import axios from '../../../../query-client/axios'
import { MissionAction, MissionNavAction } from '../types/mission-action'
import { actionKeys, missionKeys } from '../../../../query-client/query-keys.tsx'
import { useOnlineManager } from '../hooks/use-online-manager.tsx'
import { Mission2 } from '../types/mission-types.ts'
import AuthToken from '@features/auth/utils/token.ts'
import { SyncStatus } from '../types/network-types.ts'

const useCreateMissionActionMutation = (
  missionId: number
): UseMutationResult<MissionNavAction, Error, MissionNavAction, unknown> => {
  const queryClient = useQueryClient()

  const { isOnline } = useOnlineManager()

  const createAction = async (action: MissionNavAction): Promise<MissionNavAction> => {
    debugger
    const token = new AuthToken().get()
    const auth = token ? `Bearer ${token}` : ''
    const response = await fetch(`/api/v2/missions/${missionId}/actions`, {
      method: 'POST',
      body: JSON.stringify(action),
      credentials: 'omit',
      headers: { Accept: 'application/json', 'Content-type': 'application/json', Authorization: auth }
    })
    return await response.json()
    // axios.post(`missions/${missionId}/actions`, action).then(response => response.data)
  }

  const mutation = useMutation({
    mutationFn: createAction,
    networkMode: 'offlineFirst',
    retry: 1,
    onMutate: async (newAction: MissionNavAction) => {
      debugger
      // to me on monday: it seems to fail here
      // await queryClient.cancelQueries({ queryKey: actionKeys.all(), exact: true })

      // const id = uuidv4() // Generate a UUID locally
      const optimisticAction = { ...newAction, syncStatus: SyncStatus.UNSYNC }

      const mission: Mission2 | undefined = queryClient.getQueryData(missionKeys.detail(missionId))
      const previousActions = mission?.actions
      // queryClient.setQueryData(actionKeys.all(), [newAction, ...previousActions || []])
      debugger
      queryClient.setQueryData(
        missionKeys.detail(missionId),
        (mission: Mission2 | undefined) =>
          ({
            ...mission,
            actions: [optimisticAction, ...(previousActions || [])]
          }) as Mission2
      )
      // Set individual action query for the optimistic object
      queryClient.setQueryData(actionKeys.detail(newAction.id), optimisticAction)

      // return context
      return { previousActions, action: optimisticAction }
    },
    // Invalidate queries to fetch the latest data after mutation success
    onSuccess: (serverResponse, newAction, context) => {
      debugger
      if (navigator.onLine) {
        const { id: serverId } = serverResponse

        // set new action with final id in cache
        queryClient.setQueryData(actionKeys.detail(serverId), {
          ...serverResponse,
          isNotYetSyncWithServer: !navigator.onLine
        })

        // remove the old individual action query if the ID changed
        if (context.action.id !== serverId) {
          queryClient.removeQueries({ queryKey: actionKeys.detail(context.action.id) })
        }

        // update action list
        queryClient.setQueryData(
          missionKeys.detail(missionId),
          (mission: Mission2 | undefined) =>
            ({
              ...mission,
              actions: (mission?.actions ?? []).map((action: MissionAction) =>
                action.id === newAction.id ? serverResponse : action
              )
            }) as Mission2
        )

        // refetch mission
        // queryClient.invalidateQueries({ queryKey: missionKeys.detail(missionId), exact: true })
      }
    },
    onError: (_, __, context) => {
      debugger
      if (!navigator.onLine) {
        console.log('Offline: Action will remain unsynced until next synchronization.')
      } else {
        // Rollback for other errors
        console.error(`Mutation failed for action with ID: ${context.id}`, _)

        // reset action list
        queryClient.setQueryData(
          missionKeys.detail(missionId),
          (mission: Mission2 | undefined) =>
            ({
              ...mission,
              actions: context.previousActions
            }) as Mission2
        )
      }
    },
    // Clean-up actions regardless of success or failure
    // onSettled: (data, error, variables, context) => {
    onSettled: (_: any) => {
      debugger
      // queryClient.invalidateQueries({ queryKey: actionKeys.detail(), exact: true })
      queryClient.invalidateQueries({ queryKey: missionKeys.detail(missionId), exact: true })
    }
  })

  // useEffect(() => {
  //   if ((!mutation.isIdle && mutation.isPaused) || mutation.isSuccess) {
  //     if (!isOnline) {
  //       const { action } = mutation.context
  //       queryClient.setQueryData(
  //         missionKeys.detail(missionId),
  //         (mission: Mission2 | undefined) =>
  //           ({
  //             ...mission,
  //             actions: [action, ...(mission?.actions || [])]
  //           }) as Mission2
  //       )
  //       debugger
  //     } else {
  //       debugger
  //     }
  //   }
  // }, [mutation.isIdle, mutation.isPaused, mutation.isSuccess, mutation.context])

  return mutation
}

export default useCreateMissionActionMutation
