import { gql } from '@apollo/client'

export const GET_MISSION_BY_ID = gql`
  query GetMissionById($missionId: ID) {
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
        startDateTimeUtc
        endDateTimeUtc
        data {
          ... on FishActionData {
            id
            actionType
            actionDatetimeUtc
            vesselId
            vesselName
            latitude
            longitude
            facade
            emitsVms
            emitsAis
            licencesMatchActivity
            logbookMatchesActivity
            licencesAndLogbookObservations
            gearOnboard {
              comments
              controlledMesh
              declaredMesh
              gearCode
              gearName
              gearWasControlled
              hasUncontrolledMesh
            }
            speciesObservations
            speciesOnboard {
              speciesCode
              nbFish
              declaredWeight
              controlledWeight
              underSized
            }
            seizureAndDiversion
            seizureAndDiversionComments
            hasSomeGearsSeized
            hasSomeSpeciesSeized
            otherComments
            faoAreas
            segments {
              segment
              segmentName
            }
            vesselTargeted
            unitWithoutOmegaGauge
            controlQualityComments
            feedbackSheetRequired
            userTrigram
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
                natinfs
                infractionType
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
                natinfs
                infractionType
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
                natinfs
                infractionType
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
                natinfs
                infractionType
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
            availableControlTypesForInfraction
            themes {
              theme
              subThemes
            }
            infractions {
              vesselIdentifier
              vesselType
              targetAddedByUnit
              controlTypesWithInfraction
              infractions {
                id
                controlType
                observations
                infractionType
                natinfs
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
                natinfs
                infractionType
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
                natinfs
                infractionType
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
                natinfs
                infractionType
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
                natinfs
                infractionType
                observations
              }
            }
          }
        }
      }
    }
  }
`


export const MUTATION_ADD_OR_UPDATE_CONTROL_GENS_DE_MER = gql`
  mutation AddOrUpdateControlGensDeMer($control: ControlGensDeMerInput!) {
    addOrUpdateControlGensDeMer(control: $control) {
      id
      amountOfControls
      unitShouldConfirm
      unitHasConfirmed
      staffOutnumbered
      upToDateMedicalCheck
      knowledgeOfFrenchLawAndLanguage
      observations
    }
  }
`

export const MUTATION_ADD_OR_UPDATE_CONTROL_NAVIGATION = gql`
  mutation AddOrUpdateControlNavigation($control: ControlNavigationInput!) {
    addOrUpdateControlNavigation(control: $control) {
      id
      amountOfControls
      unitShouldConfirm
      unitHasConfirmed
      observations
    }
  }
`

export const MUTATION_ADD_OR_UPDATE_CONTROL_SECURITY = gql`
  mutation AddOrUpdateControlSecurity($control: ControlSecurityInput!) {
    addOrUpdateControlSecurity(control: $control) {
      id
      amountOfControls
      unitShouldConfirm
      unitHasConfirmed
      observations
    }
  }
`

export const MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE = gql`
  mutation AddOrUpdateControlAdministrative($control: ControlAdministrativeInput!) {
    addOrUpdateControlAdministrative(control: $control) {
      id
      amountOfControls
      unitShouldConfirm
      unitHasConfirmed
      compliantOperatingPermit
      upToDateNavigationPermit
      compliantSecurityDocuments
      observations
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


// export const MUTATION_DELETE_INFRACTION_ENV = gql`
//   mutation DeleteInfractionForEnvTarget($id: String!) {
//     deleteInfractionForEnvTarget(id: $id)
//   }
// `


export const GET_AGENTS_BY_SERVICE = gql`
  query GetAgentsByServiceId($serviceId: ID!) {
    agentsByServiceId(serviceId: $serviceId) {
      id
      firstName
      lastName
    }
  }
`




