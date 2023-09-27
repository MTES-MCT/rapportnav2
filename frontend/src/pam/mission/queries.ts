import { gql } from '@apollo/client'

export const GET_MISSION_BY_ID = gql`
  query GetMissionById($missionId: ID) {
    missionById(missionId: $missionId) {
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
          ... on NavActionData {
            id
            actionType
            statusAction {
              status
              reason
              isStart
              observations
            }
            controlAction {
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
  }
`
