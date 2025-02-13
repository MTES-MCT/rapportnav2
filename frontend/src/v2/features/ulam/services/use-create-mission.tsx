import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { Mission, MissionGeneralInfo2 } from '../../common/types/mission-types.ts'
import * as Sentry from '@sentry/react'

const useCreateMissionMutation = (): UseMutationResult<Mission, Error, MissionGeneralInfo2, unknown> => {
  const queryClient = useQueryClient()


  const createMission = (mission: MissionGeneralInfo2): Promise<Mission> =>
    axios.post(`/missions`, mission).then(response => response.data)

  return useMutation({
    mutationFn: createMission,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['mission'] })
    },
    onError: (error) => {
      console.error(error)
      Sentry.captureException(error)
    }
  })
}

export default useCreateMissionMutation
