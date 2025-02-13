import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import {  MissionGeneralInfo2 } from '../../common/types/mission-types.ts'
import * as Sentry from '@sentry/react'

const useUpdateGeneralInfoMutation = (missionId?: number): UseMutationResult<MissionGeneralInfo2, Error, MissionGeneralInfo2, unknown> => {
  const queryClient = useQueryClient()


  const updateGeneralInfos = (generalInfo: MissionGeneralInfo2): Promise<MissionGeneralInfo2> =>
    axios.put(`/missions/${missionId}/general_infos`, generalInfo).then(response => response.data)

  return useMutation({
    mutationFn: updateGeneralInfos,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['mission'] })
    },
    onError: (error) => {
      console.error(error)
      Sentry.captureException(error)
    }
  })
}

export default useUpdateGeneralInfoMutation
