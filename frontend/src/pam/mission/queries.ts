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
        type
        source
        status
        startDateTimeUtc
        endDateTimeUtc
        data {
          ... on FishActionData {
            id
            facade
            controlsToComplete
            controlAdministrative {
              id
              amountOfControls
              unitShouldConfirm
              unitHasConfirmed
              compliantOperatingPermit
              upToDateNavigationPermit
              compliantSecurityDocuments
              observations
              infractions {
                id
                formalNotice
                observations
              }
            }
            controlGensDeMer {
              id
              amountOfControls
              unitShouldConfirm
              unitHasConfirmed
              staffOutnumbered
              upToDateMedicalCheck
              knowledgeOfFrenchLawAndLanguage
              observations
              infractions {
                id
                formalNotice
                observations
              }
            }
            controlNavigation {
              id
              amountOfControls
              unitShouldConfirm
              unitHasConfirmed
              observations
              infractions {
                id
                formalNotice
                observations
              }
            }
            controlSecurity {
              id
              amountOfControls
              unitShouldConfirm
              unitHasConfirmed
              observations
              infractions {
                id
                formalNotice
                observations
              }
            }
          }
          ... on EnvActionData {
            id
            observations
            actionNumberOfControls
            actionTargetType
            vehicleType
            controlsToComplete
            availableControlTypes
            themes {
              theme
              subThemes
            }
            infractions {
              vesselIdentifier
              vesselType
              infractions {
                id
                controlType
                observations
                formalNotice
                target {
                  formalNotice
                  companyName
                  relevantCourt
                  infractionType
                  toProcess
                  vesselType
                  vesselSize
                  vesselIdentifier
                  identityControlledPerson
                }
              }
            }
            controlAdministrative {
              id
              amountOfControls
              observations
            }
            controlSecurity {
              id
              amountOfControls
              observations
            }
            controlNavigation {
              id
              amountOfControls
              observations
            }
            controlGensDeMer {
              id
              amountOfControls
              observations
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
            latitude
            longitude
            controlMethod
            vesselIdentifier
            vesselType
            vesselSize
            observations
            identityControlledPerson
            controlAdministrative {
              id
              amountOfControls
              unitShouldConfirm
              unitHasConfirmed
              compliantOperatingPermit
              upToDateNavigationPermit
              compliantSecurityDocuments
              observations
              infractions {
                id
                formalNotice

                observations
              }
            }
            controlGensDeMer {
              id
              amountOfControls
              unitShouldConfirm
              unitHasConfirmed
              staffOutnumbered
              upToDateMedicalCheck
              knowledgeOfFrenchLawAndLanguage
              observations
              infractions {
                id
                formalNotice
                observations
              }
            }
            controlNavigation {
              id
              amountOfControls
              unitShouldConfirm
              unitHasConfirmed
              observations
              infractions {
                id
                formalNotice
                observations
              }
            }
            controlSecurity {
              id
              amountOfControls
              unitShouldConfirm
              unitHasConfirmed
              observations
              infractions {
                id
                formalNotice
                observations
              }
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

export const MUTATION_ADD_OR_UPDATE_ACTION_CONTROL = gql`
  mutation AddOrUpdateControl($controlAction: ActionControlInput!) {
    addOrUpdateControl(controlAction: $controlAction) {
      id
    }
  }
`

export const DELETE_ACTION_CONTROL = gql`
  mutation DeleteControl($id: String!) {
    deleteControl(id: $id)
  }
`

export const MUTATION_ADD_OR_UPDATE_CONTROL_GENS_DE_MER = gql`
  mutation AddOrUpdateControlGensDeMer($control: ControlGensDeMerInput!) {
    addOrUpdateControlGensDeMer(control: $control) {
      id
    }
  }
`

export const MUTATION_ADD_OR_UPDATE_CONTROL_NAVIGATION = gql`
  mutation AddOrUpdateControlNavigation($control: ControlNavigationInput!) {
    addOrUpdateControlNavigation(control: $control) {
      id
    }
  }
`

export const MUTATION_ADD_OR_UPDATE_CONTROL_SECURITY = gql`
  mutation AddOrUpdateControlSecurity($control: ControlSecurityInput!) {
    addOrUpdateControlSecurity(control: $control) {
      id
    }
  }
`

export const MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE = gql`
  mutation AddOrUpdateControlAdministrative($control: ControlAdministrativeInput!) {
    addOrUpdateControlAdministrative(control: $control) {
      id
    }
  }
`

export const DELETE_CONTROL_ADMINISTRATIVE = gql`
  mutation DeleteControlAdministrative($actionId: String!) {
    deleteControlAdministrative(actionId: $actionId)
  }
`

export const DELETE_CONTROL_NAVIGATION = gql`
  mutation DeleteControlNavigation($actionId: String!) {
    deleteControlNavigation(actionId: $actionId)
  }
`

export const DELETE_CONTROL_SECURITY = gql`
  mutation DeleteControlSecurity($actionId: String!) {
    deleteControlSecurity(actionId: $actionId)
  }
`

export const DELETE_CONTROL_GENS_DE_MER = gql`
  mutation DeleteControlGensDeMer($actionId: String!) {
    deleteControlGensDeMer(actionId: $actionId)
  }
`

export const MUTATION_ADD_OR_UPDATE_INFRACTION = gql`
  mutation AddOrUpdateInfraction($infraction: InfractionInput!) {
    addOrUpdateInfraction(infraction: $infraction) {
      id
    }
  }
`

export const MUTATION_DELETE_INFRACTION = gql`
  mutation DeleteInfraction($id: String!) {
    deleteInfraction(id: $id)
  }
`

export const MUTATION_ADD_OR_UPDATE_INFRACTION_ENV = gql`
  mutation AddOrUpdateInfractionForEnvTarget($infraction: InfractionWithNewTargetInput!) {
    addOrUpdateInfractionForEnvTarget(infraction: $infraction) {
      id
    }
  }
`

// export const MUTATION_DELETE_INFRACTION_ENV = gql`
//   mutation DeleteInfractionForEnvTarget($id: String!) {
//     deleteInfractionForEnvTarget(id: $id)
//   }
// `
