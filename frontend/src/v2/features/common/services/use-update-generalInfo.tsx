import * as Sentry from '@sentry/react'
import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { Mission2, MissionGeneralInfo2 } from '../types/mission-types.ts'
import { missionsKeys } from './query-keys.ts'

const useUpdateGeneralInfoMutation = (
  missionId: string
): UseMutationResult<MissionGeneralInfo2, Error, MissionGeneralInfo2, unknown> => {
  const queryClient = useQueryClient()

  const updateGeneralInfos = (generalInfo: MissionGeneralInfo2): Promise<MissionGeneralInfo2> =>
    axios.put(`/missions/${missionId}/general_infos`, generalInfo).then(response => response.data)

  const mutationDefault = {
    mutationFn: updateGeneralInfos,
    onMutate: async (updatedGeneralInfos: MissionGeneralInfo2) => {
      await queryClient.cancelQueries({ queryKey: missionsKeys.byId(missionId) })

      // update general infos in cache for mission
      queryClient.setQueryData(
        missionsKeys.byId(missionId),
        (mission: Mission2 | undefined) =>
          ({
            ...mission,
            generalInfos: updatedGeneralInfos
          }) as Mission2
      )
    },
    onSettled: async () => {
      await queryClient.invalidateQueries({
        queryKey: missionsKeys.byId(missionId),
        type: 'all'
      })
    },
    onError: (error: Error) => {
      console.error(error)
      Sentry.captureException(error)
    },
    scope: {
      id: `update-general-info`
    }
  }

  queryClient.setMutationDefaults(missionsKeys.update(), mutationDefault)
  const mutation = useMutation({ mutationKey: missionsKeys.update(), mutationFn: updateGeneralInfos })
  return mutation
}

export default useUpdateGeneralInfoMutation
