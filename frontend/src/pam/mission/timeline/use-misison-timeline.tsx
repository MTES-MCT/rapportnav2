import {ApolloError, gql, useQuery} from '@apollo/client'
import {GET_AGENTS_BY_USER_SERVICE} from '../queries'
import {Agent} from '../../../types/crew-types'
import {Mission} from "../../../types/mission-types.ts";

export const GET_MISSION_TIMELINE = gql`
    query GetMissionTimeline($missionId: ID) {
        mission(missionId: $missionId) {
            id
            startDateTimeUtc
            endDateTimeUtc
            actions {
                id
                type
                source
                status
                startDateTimeUtc
                endDateTimeUtc
                data {
                    ... on FishActionData {
                        id
                        actionDatetimeUtc
                        actionType
                        vesselId
                        vesselName
                        controlsToComplete
                        availableControlTypes
                    }
                    ... on EnvActionData {
                        id
                        actionNumberOfControls
                        actionTargetType
                        vehicleType
                        controlsToComplete
                        availableControlTypes
                        themes {
                            theme
                        }
                    }
                    ... on NavActionStatus {
                        id
                        startDateTimeUtc
                        status
                        reason
                        isStart
                        observations
                    }
                    ... on NavActionControl {
                        id
                        controlMethod
                        vesselIdentifier
                        vesselType
                        vesselSize
                    }
                }
            }
        }
    }
`

const useGetMissionTimeline = (missionId?: string): {
    data?: Mission;
    loading: boolean;
    error?: ApolloError
} | undefined => {
    if (!missionId)
        return

    const {loading, error, data} = useQuery(GET_MISSION_TIMELINE, {
        variables: {missionId}
        // fetchPolicy: 'cache-only'
    })

    return {loading, error, data: data?.mission}
}

export default useGetMissionTimeline
