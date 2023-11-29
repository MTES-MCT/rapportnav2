import { MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_CREW, MUTATION_ADD_OR_UPDATE_MISSION_CREW } from '../queries'
import { Agent, MissionCrew, AgentRole } from '../../../types/crew-types'

export type AddOrUpdateMissionCrewInput = {
  id?: string
  agent?: Agent
  missionId?: string
  comment?: string
  role?: AgentRole
}

const useAddOrUpdateMissionCrew = (): MutationTuple<MissionCrew, Record<string, any>> => {
  const mutation = useMutation(MUTATION_ADD_OR_UPDATE_MISSION_CREW, {
    refetchQueries: [GET_MISSION_CREW]
  })

  return mutation
}

export default useAddOrUpdateMissionCrew
