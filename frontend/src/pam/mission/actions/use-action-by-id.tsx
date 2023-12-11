import {ApolloError, gql, useQuery} from '@apollo/client'
import {Action} from "../../../types/action-types.ts";
import {ActionTypeEnum, MissionSourceEnum} from "../../../types/env-mission-types.ts";

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
                    availableControlTypes
                    geom
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
                            infractionType
                            observations
                        }
                    }
                }
            }
        }
    }
`

const useActionById = (id?: string = undefined, missionId?: string = undefined, source: MissionSourceEnum, type: ActionTypeEnum): {
    data?: Action;
    loading: boolean;
    error?: ApolloError
} | undefined => {
    if (!id || !missionId)
        return
    const {loading, error, data, ...rest} = useQuery(GET_ACTION_BY_ID, {
        variables: {id, missionId, source, type}
        // fetchPolicy: 'cache-only'
    })

    return {loading, error, data: data?.actionById, ...rest}
}

export default useActionById
