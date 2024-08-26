import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { Agent, AgentRole, MissionCrew } from '../../../common/types/crew-types.ts'
import { GET_MISSION_EXCERPT } from './use-mission-excerpt.tsx'
import { GET_MISSION_CREW } from './use-mission-crew.tsx'

export const MUTATION_ADD_OR_UPDATE_MISSION_CREW = gql`
  mutation AddOrUpdateMissionCrew($crew: MissionCrewInput!) {
    addOrUpdateMissionCrew(crew: $crew) {
      id
      comment
      agent {
        id
        firstName
        lastName
      }
      role {
        id
        title
      }
    }
  }
`

export type AddOrUpdateMissionCrewInput = {
  id?: string
  agent?: Agent
  comment?: string
  role?: AgentRole
  missionId: number
}

const useAddOrUpdateMissionCrew = (): MutationTuple<MissionCrew, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_ADD_OR_UPDATE_MISSION_CREW, {
    refetchQueries: [
      { query: GET_MISSION_CREW, variables: { missionId } },
      { query: GET_MISSION_EXCERPT, variables: { missionId } }
    ]
  })

  return mutation
}

export default useAddOrUpdateMissionCrew
