import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_EXCERPT } from "./use-mission-excerpt.tsx";
import { MissionGeneralInfo } from "../../../types/mission-types.ts";

export const MUTATION_ADD_OR_UPDATE_DISTANCE_CONSUMPTION = gql`
    mutation UpdateMissionGeneralInfo($info: MissionGeneralInfoInput!) {
        updateMissionGeneralInfo(info: $info) {
            id
            distanceInNauticalMiles
            consumedGOInLiters
            consumedFuelInLiters
        }
    }
`

const useAddOrUpdateDistanceConsumption = (): MutationTuple<MissionGeneralInfo, Record<string, any>> => {
    const mutation = useMutation(
        MUTATION_ADD_OR_UPDATE_DISTANCE_CONSUMPTION,
        {
            refetchQueries: [GET_MISSION_EXCERPT]
        }
    )

    return mutation
}

export default useAddOrUpdateDistanceConsumption
