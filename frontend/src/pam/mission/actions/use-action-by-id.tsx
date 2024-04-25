import { ApolloError, gql, useQuery } from '@apollo/client'
import { Action } from "../../../types/action-types.ts";
import { ActionTypeEnum, MissionSourceEnum } from "../../../types/env-mission-types.ts";

export const GET_ACTION_BY_ID = gql`
    query GetActionById($id: ID!, $missionId: ID!, $source: MissionSource!, $type: ActionType!) {
        actionById(id: $id, missionId: $missionId, source: $source, type: $type) {
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
                    geom
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
                ... on NavActionStatus {
                    id
                    startDateTimeUtc
                    status
                    reason
                    observations
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
                    isVesselRescue
                    isPersonRescue
                    locationDescription
                    latitude
                    longitude
                    numberOfDeaths
                    numberPersonsRescued
                    isVesselTowed
                    isVesselNoticed
                }
                ... on NavActionNauticalEvent {
                    id
                    startDateTimeUtc
                    endDateTimeUtc
                    observations
                }
                ... on NavActionVigimer {
                    id
                    startDateTimeUtc
                    endDateTimeUtc
                    observations
                }
                ... on NavActionAntiPollution {
                    id
                    startDateTimeUtc
                    endDateTimeUtc
                    observations
                }
                ... on NavActionBAAEMPermanence {
                    id
                    startDateTimeUtc
                    endDateTimeUtc
                    observations
                }

                ... on NavActionPublicOrder {
                    id
                    startDateTimeUtc
                    endDateTimeUtc
                    observations
                }

                ... on NavActionRepresentation {
                    id
                    startDateTimeUtc
                    endDateTimeUtc
                    observations
                }
                ... on NavActionIllegalImmigration {
                    id
                    startDateTimeUtc
                    endDateTimeUtc
                    observations
                    latitude
                    longitude
                    nbOfInterceptedVessels
                    nbOfInterceptedMigrants
                    nbOfSuspectedSmugglers
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
`

const useActionById = (
    id: string | undefined = undefined,
    missionId: string | undefined = undefined,
    source: MissionSourceEnum,
    type: ActionTypeEnum
): {
    data?: Action;
    loading: boolean;
    error?: ApolloError;
} => {
    const {loading, error, data, ...rest} = useQuery(GET_ACTION_BY_ID, {
        variables: {id, missionId, source, type},
        // fetchPolicy: 'cache-only'
    });

    if (!id || !missionId) {
        return {loading: false, error: undefined, data: undefined};
    }

    return {loading, error, data: data?.actionById, ...rest};
};

export default useActionById;
