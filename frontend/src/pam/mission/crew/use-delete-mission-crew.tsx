import { gql, MutationTuple, useMutation } from '@apollo/client'
import { MissionCrew } from '../../../types/crew-types'
import { GET_MISSION_CREW } from "./use-mission-crew.tsx";

export const MUTATION_DELETE_MISSION_CREW = gql`
  mutation DeleteMissionCrew($id: ID!) {
    deleteMissionCrew(id: $id)
  }
`

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
