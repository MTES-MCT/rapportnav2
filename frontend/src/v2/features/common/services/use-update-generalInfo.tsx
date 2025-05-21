import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { Mission2, MissionGeneralInfo2 } from '../types/mission-types.ts'
import { missionsKeys } from './query-keys.ts'

const useUpdateGeneralInfoMutation = (
  missionId?: number
): UseMutationResult<MissionGeneralInfo2, Error, MissionGeneralInfo2, unknown> => {
  const queryClient = useQueryClient()

  const updateGeneralInfos = (generalInfo: MissionGeneralInfo2): Promise<MissionGeneralInfo2> =>
    axios.put(`/missions/${missionId}/general_infos`, generalInfo).then(response => response.data)

  return useMutation({
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
