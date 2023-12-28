import { gql, MutationTuple, useMutation } from '@apollo/client'
import { Agent, AgentRole, MissionCrew } from '../../../types/crew-types'
import { GET_MISSION_CREW } from "./use-mission-crew.tsx";

export const MUTATION_ADD_OR_UPDATE_MISSION_CREW = gql`
    mutation AddOrUpdateMissionCrew($crew: MissionCrewInput!) {
        addOrUpdateMissionCrew(crew: $crew) {
            id
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
