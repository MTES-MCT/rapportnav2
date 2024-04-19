import { ApolloError, gql, useQuery } from '@apollo/client'
import { Mission } from '../../../types/mission-types.ts'

export const GET_MISSION_TIMELINE = gql`
  query GetMissionTimeline($missionId: ID) {
    mission(missionId: $missionId) {
      id
      startDateTimeUtc
      endDateTimeUtc
      reportStatus {
        status
        sources
      }
      actions {
        id
        type
        source
        status
        summaryTags
        startDateTimeUtc
        endDateTimeUtc
        isCompleteForStats
        data {
          ... on FishActionData {
            id
            actionDatetimeUtc
            actionType
            vesselId
            vesselName
            controlsToComplete
          }
          ... on EnvActionData {
            id
            actionNumberOfControls
            actionTargetType
            vehicleType
            controlsToComplete
            themes {
              theme
            }
          }
          ... on NavActionFreeNote {
            id
            startDateTimeUtc
            observations
          }
          ... on NavActionStatus {
            id
            startDateTimeUtc
            status
            reason
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

const useGetMissionTimeline = (
  missionId?: string
): {
  data?: Mission
  loading: boolean
  error?: ApolloError
} => {
  const { loading, error, data } = useQuery(GET_MISSION_TIMELINE, {
    variables: { missionId }
  })

  if (!missionId) {
    return { loading: false, error: undefined, data: undefined }
  }

  return { loading, error, data: data?.mission }
}

export default useGetMissionTimeline
