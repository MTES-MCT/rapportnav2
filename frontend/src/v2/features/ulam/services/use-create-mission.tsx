import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { Mission, Mission2, MissionGeneralInfo2 } from '../../common/types/mission-types.ts'
import * as Sentry from '@sentry/react'
import { missionsKeys } from '../../common/services/query-keys.ts'

const useCreateMissionMutation = (): UseMutationResult<Mission2, Error, MissionGeneralInfo2, unknown> => {
  const queryClient = useQueryClient()

  const createMission = (mission: MissionGeneralInfo2): Promise<Mission2> =>
    axios.post(`/missions`, mission).then(response => response.data)

  return useMutation({
    mutationFn: createMission,
    onSuccess: (data: Mission2) => {
      queryClient.invalidateQueries({ queryKey: missionsKeys.byId(data.id) })
    },
    onError: error => {
      console.error(error)
      Sentry.captureException(error)
    },
    scope: {
      id: 'create-mission'
    }
  })
}

export default useCreateMissionMutation
