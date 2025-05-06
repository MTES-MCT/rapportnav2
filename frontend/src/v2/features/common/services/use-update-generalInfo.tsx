import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { MissionGeneralInfo2 } from '../types/mission-types.ts'
import { missionsKeys } from './query-keys.ts'

const useUpdateGeneralInfoMutation = (
  missionId?: string
): UseMutationResult<MissionGeneralInfo2, Error, MissionGeneralInfo2, unknown> => {
  const queryClient = useQueryClient()

  const updateGeneralInfos = (generalInfo: MissionGeneralInfo2): Promise<MissionGeneralInfo2> =>
    axios.put(`/missions/${missionId}/general_infos`, generalInfo).then(response => response.data)

  return useMutation({
    mutationFn: updateGeneralInfos,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: missionsKeys.byId(missionId!!) })
    },
    onError: error => {
      console.error(error)
      Sentry.captureException(error)
    },
    scope: {
      id: `update-general-info-${missionId}`
    }
  })
}

export default useUpdateGeneralInfoMutation
