import { gql } from '@apollo/client'

export const GET_MISSION_BY_ID = gql`
  query GetMissionById($missionId: ID) {
    missionById(missionId: $missionId) {
      id
      missionSource
      startDateTimeUtc
      endDateTimeUtc
      actions {
        navAction {
          id
          actionStartDateTimeUtc
          actionEndDateTimeUtc
          actionType
          actionControl {
            controlsVesselAdministrative {
              id
            }
            controlsGensDeMer {
              id
            }
            controlsNavigationRules {
              id
            }
            controlsEquipmentAndSecurity {
              id
            }
          }
        }
        fishAction {
          id
          actionDatetimeUtc
          actionType
        }
        envAction {
          id
          actionStartDateTimeUtc
          actionEndDateTimeUtc
          actionType
          themes {
            theme
            subThemes
          }
          geom
          actionNumberOfControls
          actionTargetType
          vehicleType
          observations
        }
      }
    }
  }
`
