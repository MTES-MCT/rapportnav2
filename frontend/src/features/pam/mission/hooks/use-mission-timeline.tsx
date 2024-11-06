import { ApolloError, FetchPolicy, gql, useQuery } from '@apollo/client'
import { Mission } from '../../../common/types/mission-types.ts'

export const GET_MISSION_TIMELINE = gql`
  query GetMissionTimeline($missionId: ID) {
    mission(missionId: $missionId) {
      id
      startDateTimeUtc
      endDateTimeUtc
      status
      completenessForStats {
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
        completenessForStats {
          status
          sources
        }
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
            formattedControlPlans {
              theme
              subThemes
            }
          }
          ... on NavActionFreeNote {
            id
            startDateTimeUtc
            observations
          }
          ... on NavActionRescue {
            id
            startDateTimeUtc
            observations
            isPersonRescue
            isVesselRescue
          }
          ... on NavActionNauticalEvent {
            id
            startDateTimeUtc
            observations
          }
          ... on NavActionVigimer {
            id
            startDateTimeUtc
            observations
          }
          ... on NavActionBAAEMPermanence {
            id
            startDateTimeUtc
            observations
          }
          ... on NavActionPublicOrder {
            id
            startDateTimeUtc
            observations
          }
          ... on NavActionRepresentation {
            id
            startDateTimeUtc
            observations
          }
          ... on NavActionIllegalImmigration {
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
  missionId?: string,
  fetchPolicy: FetchPolicy = 'cache-first'
): {
  data?: Mission
  loading: boolean
  error?: ApolloError
} => {
  const { loading, error, data } = useQuery(GET_MISSION_TIMELINE, {
    variables: { missionId },
    fetchPolicy: fetchPolicy,
    pollInterval: 500000 // 5 min
  })

  if (!missionId) {
    return { loading: false, error: undefined, data: undefined }
  }

  return { loading, error, data: data?.mission }
}

export default useGetMissionTimeline
