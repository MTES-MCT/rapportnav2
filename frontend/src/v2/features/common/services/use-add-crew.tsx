import { useMutation, UseMutationResult } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { MissionCrew } from '../types/crew-type.ts'

const useAddCrewMutation = (): UseMutationResult<MissionCrew, Error, MissionCrew, unknown> => {

  const addCrew = (crew: MissionCrew): Promise<MissionCrew> =>
    axios.post(`crews`, crew).then(response => response.data)

  const mutation = useMutation({
    mutationFn: addCrew,
    onSuccess: () => {
      //queryClient.invalidateQueries({ queryKey: ['mission'] })
    }
  })
  return mutation
}

export default useAddCrewMutation
