import { gql, MutationTuple, useMutation } from '@apollo/client'
import { MissionCrew } from '../../../types/crew-types'
import { GET_MISSION_CREW } from "./use-mission-crew.tsx";
import { useParams } from "react-router-dom";

export const MUTATION_DELETE_MISSION_CREW = gql`
    mutation DeleteMissionCrew($id: ID!) {
        deleteMissionCrew(id: $id)
    }
`

export type DeleteMissionCrewInput = {
    id?: string
}

const useDeleteMissionCrew = (id?: string): MutationTuple<MissionCrew, Record<string, any>> => {
    const {missionId} = useParams()
    const mutation = useMutation(MUTATION_DELETE_MISSION_CREW, {
        refetchQueries: [
            {query: GET_MISSION_CREW, variables: {missionId}},
        ]
    })

    return mutation
}

export default useDeleteMissionCrew
