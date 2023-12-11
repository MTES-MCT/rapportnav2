import {ApolloError, gql, useQuery} from '@apollo/client'
import {MissionCrew} from '../../../types/crew-types'
import {Mission} from "../../../types/mission-types.ts";

export const GET_MISSION_EXCERPT = gql`
    query GetMissionExcerpt($missionId: ID) {
        mission(missionId: $missionId) {
            id
            missionSource
            startDateTimeUtc
            endDateTimeUtc
            generalInfo {
                id
                distanceInNauticalMiles
                consumedGOInLiters
                consumedFuelInLiters
            }
            actions {
                id
                type
                source
                status
            }
        }
    }
`


const useMissionExcerpt = (missionId?: string): { data?: Mission; loading: boolean; error?: ApolloError } => {
    const {loading, error, data} = useQuery(GET_MISSION_EXCERPT, {
        variables: {missionId}
        // fetchPolicy: 'cache-only'
    })

    return {loading, error, data: data?.mission}
}

export default useMissionExcerpt
