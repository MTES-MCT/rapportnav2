import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { MissionEnv, MissionULAMGeneralInfoInitial } from '../../common/types/mission-types.ts'
import * as Sentry from '@sentry/react'

const useCreateMissionMutation = (): UseMutationResult<MissionEnv, Error, MissionULAMGeneralInfoInitial, unknown> => {
  const queryClient = useQueryClient()


  const createMission = (mission: MissionULAMGeneralInfoInitial): Promise<MissionEnv> =>
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
