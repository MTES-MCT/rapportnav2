import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { MissionCrew } from '../types/crew-type.ts'

const useAddMultipleCrewsMutation = (missionId?: string): UseMutationResult<MissionCrew[], Error, MissionCrew[], unknown> => {

  const queryClient = useQueryClient()

  const addMultipleCrews = (crews: MissionCrew[]): Promise<MissionCrew[]> =>
    axios.post(`crews/multiple`, crews).then(response => response.data)

  const mutation = useMutation({
    mutationFn: addMultipleCrews,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['missionCrew', missionId] })
    }
  })
  return mutation
}

export default useAddMultipleCrewsMutation
