import { gql, MutationTuple, useMutation } from '@apollo/client'
import { MissionCrew } from '../../../common/types/crew-types.ts'
import { GET_MISSION_CREW } from './use-mission-crew.tsx'
import { useParams } from 'react-router-dom'
import { GET_MISSION_EXCERPT } from './use-mission-excerpt.tsx'

export const MUTATION_DELETE_MISSION_CREW = gql`
  mutation DeleteMissionCrew($id: ID!) {
    deleteMissionCrew(id: $id)
  }
`

export type DeleteMissionCrewInput = {
  id?: string
}

const useDeleteMissionCrew = (id?: string): MutationTuple<MissionCrew, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_DELETE_MISSION_CREW, {
    refetchQueries: [
      { query: GET_MISSION_CREW, variables: { missionId } },
      { query: GET_MISSION_EXCERPT, variables: { missionId } }
    ]
  })

  return mutation
}

export default useDeleteMissionCrew
