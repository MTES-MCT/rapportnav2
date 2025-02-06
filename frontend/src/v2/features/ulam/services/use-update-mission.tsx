import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { Mission, MissionULAMGeneralInfoInitial } from '../../common/types/mission-types.ts'
import * as Sentry from '@sentry/react'
import { MissionGeneralInfo } from '@common/types/mission-types.ts'

const useUpdateMissionMutation = (missionId: string): UseMutationResult<Mission, Error, MissionGen, unknown> => {
  const queryClient = useQueryClient()


  const createMission = (mission: MissionULAMGeneralInfoInitial): Promise<Mission> =>
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
