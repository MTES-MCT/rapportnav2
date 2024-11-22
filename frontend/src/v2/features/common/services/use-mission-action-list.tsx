import { ApolloError, gql, useQuery } from '@apollo/client'
import { MissionActionOutput } from '../../common/types/mission-action-output'

export const GET_ACTION_LIST = gql`
  query GetActionList($missionId: ID!) {
    actionList(missionId: $missionId) {
      id
      missionId
      source
      summaryTags
      actionType
      status
      controlsToComplete
      isCompleteForStats
      completenessForStats {
        status
        sources
      }
      sourcesOfMissingDataForStats
      data {
        ... on MissionFishActionDataOutput {
          startDateTimeUtc
          endDateTimeUtc
          vesselId
          vesselName
          latitude
          longitude
          facade
          emitsVms
          emitsAis
          fishActionType
          licencesMatchActivity
          logbookMatchesActivity
          logbookInfractions {
            infractionType
            natinf
            comments
          }
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
          gearInfractions {
            infractionType
            natinf
            comments
          }
          speciesObservations
          speciesOnboard {
            speciesCode
            nbFish
            declaredWeight
            controlledWeight
            underSized
          }
          speciesInfractions {
            infractionType
            natinf
            comments
          }
          seizureAndDiversion
          seizureAndDiversionComments
          hasSomeGearsSeized
          hasSomeSpeciesSeized
          otherComments
          otherInfractions {
            infractionType
            natinf
            comments
          }
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
        ... on MissionEnvActionDataOutput {
          startDateTimeUtc
          endDateTimeUtc
          observations
          observationsByUnit
          actionNumberOfControls
          actionTargetType
          vehicleType
          controlsToComplete
          availableControlTypesForInfraction
          geom
          formattedControlPlans {
            theme
            subThemes
          }
          infractions {
            vesselIdentifier
            vesselType
            targetAddedByUnit
            controlTypesWithInfraction
            identityControlledPerson
            infractions {
              id
              natinfs
              controlType
              observations
              infractionType
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
        ... on MissionNavActionDataOutput {
          startDateTimeUtc
          endDateTimeUtc
          observations
          latitude
          longitude
          detectedPollution
          pollutionObservedByAuthorizedAgent
          diversionCarriedOut
          isSimpleBrewingOperationDone
          isAntiPolDeviceDeployed
          controlMethod
          vesselIdentifier
          vesselType
          vesselSize
          identityControlledPerson
          nbOfInterceptedVessels
          nbOfInterceptedMigrants
          nbOfSuspectedSmugglers
          isVesselRescue
          isPersonRescue
          isVesselNoticed
          isVesselTowed
          isInSRRorFollowedByCROSSMRCC
          numberPersonsRescued
          numberOfDeaths
          operationFollowsDEFREP
          locationDescription
          isMigrationRescue
          nbOfVesselsTrackedWithoutIntervention
          nbAssistedVesselsReturningToShore
          reason
          controlSecurity {
            id
            amountOfControls
            unitShouldConfirm
            unitHasConfirmed
            observations
            infractions {
              id
              controlType
              infractionType
              natinfs
              observations
              target {
                vesselIdentifier
                identityControlledPerson
                vesselType
                vesselSize
                companyName
                relevantCourt
                infractionType
                formalNotice
                toProcess
              }
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
              controlType
              infractionType
              natinfs
              observations
              target {
                vesselIdentifier
                identityControlledPerson
                vesselType
                vesselSize
                companyName
                relevantCourt
                infractionType
                formalNotice
                toProcess
              }
            }
          }
          controlAdministrative {
            id
            amountOfControls
            unitShouldConfirm
            unitHasConfirmed
            observations
            infractions {
              id
              controlType
              infractionType
              natinfs
              observations
              target {
                vesselIdentifier
                identityControlledPerson
                vesselType
                vesselSize
                companyName
                relevantCourt
                infractionType
                formalNotice
                toProcess
              }
            }
          }
          controlGensDeMer {
            id
            amountOfControls
            unitShouldConfirm
            unitHasConfirmed
            observations
            upToDateMedicalCheck
            knowledgeOfFrenchLawAndLanguage
            infractions {
              id
              controlType
              infractionType
              natinfs
              observations
              target {
                vesselIdentifier
                identityControlledPerson
                vesselType
                vesselSize
                companyName
                relevantCourt
                infractionType
                formalNotice
                toProcess
              }
            }
          }
        }
      }
    }
  }
`

export const useMissionActionListQuery = (
  missionId?: number
): {
  data?: MissionActionOutput[]
  loading: boolean
  error?: ApolloError
} => {
  const { loading, error, data } = useQuery<{ actionList: MissionActionOutput[] }>(GET_ACTION_LIST, {
    variables: { missionId },
    pollInterval: 500000 // 5 min
  })

  if (!missionId) return { loading: false, error: undefined, data: undefined }

  return { loading, error, data: data?.actionList }
}
