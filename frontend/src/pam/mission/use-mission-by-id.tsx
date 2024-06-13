import { ApolloClient, ApolloError, gql, useQuery } from '@apollo/client'
import { Mission } from '../../types/mission-types.ts'

export const GET_MISSION_BY_ID = gql`
  query GetMissionById($missionId: ID) {
    mission(missionId: $missionId) {
      id
      missionSource
      startDateTimeUtc
      endDateTimeUtc
      status
      completenessForStats {
        status
        sources
      }
      generalInfo {
        id
        distanceInNauticalMiles
        consumedGOInLiters
        consumedFuelInLiters
        serviceId
      }
      actions {
        id
        type
        source
        status
        startDateTimeUtc
        endDateTimeUtc
        completenessForStats {
          status
          sources
        }
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
            formattedControlPlans {
              themes
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
      services {
        id
        name
      }
    }
  }
`

const useMissionById = (): {
  data?: Mission
  loading: boolean
  error?: ApolloError
  client: ApolloClient<any>
} => {
  const { loading, error, data, ...rest } = useQuery(GET_MISSION_BY_ID, {
    // fetchPolicy: 'cache-only'
  })

  return { loading, error, data: data?.mission, ...rest }
}

export default useMissionById
