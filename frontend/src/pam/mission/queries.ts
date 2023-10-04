import { gql } from '@apollo/client'

export const GET_MISSION_BY_ID = gql`
  query GetMissionById($missionId: ID) {
    mission(missionId: $missionId) {
      id
      missionSource
      startDateTimeUtc
      endDateTimeUtc
      actions {
        id
        startDateTimeUtc
        endDateTimeUtc
        type
        status
        source
        data {
          ... on FishActionData {
            facade
          }
          ... on EnvActionData {
            observations
            actionNumberOfControls
            actionTargetType
            vehicleType
            themes {
              theme
              subThemes
            }
          }
          ... on NavActionStatus {
            id
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
            observations
            identityControlledPerson
            controlAdministrative {
              id
              confirmed
              compliantOperatingPermit
              upToDateNavigationPermit
              compliantSecurityDocuments
              observations
            }
            controlGensDeMer {
              id
              confirmed
              staffOutnumbered
              upToDateMedicalCheck
              knowledgeOfFrenchLawAndLanguage
              observations
            }
            controlNavigation {
              id
              confirmed
              observations
            }
            controlSecurity {
              id
              confirmed
              observations
            }
          }
        }
      }
    }
  }
`

export const MUTATION_ADD_OR_UPDATE_ACTION_STATUS = gql`
  mutation AddOrUpdateStatus($statusAction: ActionStatusInput!) {
    addOrUpdateStatus(statusAction: $statusAction) {
      id
    }
  }
`

export const DELETE_ACTION_STATUS = gql`
  mutation DeleteStatus($id: String!) {
    deleteStatus(id: $id)
  }
`
