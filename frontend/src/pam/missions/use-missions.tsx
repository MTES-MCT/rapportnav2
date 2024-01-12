import { ApolloError, gql, useQuery } from '@apollo/client'
import { Mission } from "../../types/mission-types.ts";

export const GET_MISSIONS = gql`
    query GetMissions {
        missions {
            id
            missionSource
            startDateTimeUtc
            endDateTimeUtc
            openBy
        }
    }
`


const useMissions = (): { data?: Mission[]; loading: boolean; error?: ApolloError } => {
    const {loading, error, data} = useQuery(GET_MISSIONS, {
        // fetchPolicy: 'cache-only'
    })

    return {loading, error, data: data?.missions}
}

export default useMissions
