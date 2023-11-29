import { MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_CREW, MUTATION_ADD_OR_UPDATE_MISSION_CREW, MUTATION_DELETE_MISSION_CREW } from '../queries'
import { MissionCrew } from '../../../types/crew-types'

export type DeleteMissionCrewInput = {
  id?: string
}

const useDeleteMissionCrew = (id?: string): MutationTuple<MissionCrew, Record<string, any>> => {
  const mutation = useMutation(MUTATION_DELETE_MISSION_CREW, {
    refetchQueries: [GET_MISSION_CREW]
  })

  return mutation
}

export default useDeleteMissionCrew
