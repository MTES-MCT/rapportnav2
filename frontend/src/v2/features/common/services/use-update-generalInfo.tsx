import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { Mission2, MissionGeneralInfo2 } from '../types/mission-types.ts'
import { missionsKeys } from './query-keys.ts'
import queryClient from '../../../../query-client'

const updateGeneralInfos = (generalInfo: MissionGeneralInfo2): Promise<MissionGeneralInfo2> =>
  axios.put(`/missions/${generalInfo.missionId}/general_infos`, generalInfo).then(response => response.data)

queryClient.setMutationDefaults(missionsKeys.update(), {
  mutationFn: async data => {
    // debugger
    return updateGeneralInfos(data)
  },
  // onSuccess: async (data, variables, context) => {
  //   await queryClient.invalidateQueries({ queryKey: missionsKeys.byId(data.missionId) })
  // },
  onSettled: async (data, error, variables, context) => {
    debugger
    await queryClient.invalidateQueries({ queryKey: missionsKeys.byId(data.missionId) })
  }
})

const useUpdateGeneralInfoMutation = (
  missionId?: number
): UseMutationResult<MissionGeneralInfo2, Error, MissionGeneralInfo2, unknown> => {
  const queryClient = useQueryClient()

  return useMutation({
    mutationKey: missionsKeys.update(),
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
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: missionsKeys.byId(missionId!!) })
    },
    onError: error => {
      console.error(error)
      Sentry.captureException(error)
    },
    scope: {
      id: `update-general-info-${missionId}` // scope to run mutations in serial and not in parallel
    }
  })
}

export default useUpdateGeneralInfoMutation
