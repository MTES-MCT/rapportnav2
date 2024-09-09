package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlSecurity
import java.time.ZonedDateTime

data class Action(
    val id: Any?,
    val missionId: Int,
    val source: MissionSourceEnum,
    val startDateTimeUtc: ZonedDateTime?,
    val endDateTimeUtc: ZonedDateTime?,
    val summaryTags: List<String>? = null,
    val data: ActionData? = null,
    val completenessForStats: CompletenessForStatsEntity? = null,
) {

    companion object {
        fun fromEnvAction(envAction: ExtendedEnvActionEntity? = null, missionId: Int): Action? {

            return when {
                envAction?.surveillanceAction != null -> {
                    val action = envAction.surveillanceAction.action
                    val completenessForStats = CompletenessForStatsEntity(
                        status = if (envAction.surveillanceAction.isCompleteForStats == true) CompletenessForStatsStatusEnum.COMPLETE else CompletenessForStatsStatusEnum.INCOMPLETE,
                        sources = envAction.surveillanceAction.sourcesOfMissingDataForStats
                    )
                    Action(
                        id = action.id,
                        missionId = missionId,
                        completenessForStats = completenessForStats,
                        source = MissionSourceEnum.MONITORENV,
                        startDateTimeUtc = action.actionStartDateTimeUtc,
                        endDateTimeUtc = action.actionEndDateTimeUtc,
                        data = action.actionStartDateTimeUtc?.let {
                            EnvActionData(
                                id = action.id,
                                actionStartDateTimeUtc = it,
                                actionEndDateTimeUtc = action.actionEndDateTimeUtc,
                                actionType = action.actionType,
                                geom = action.geom,
                                observations = action.observations,
                                observationsByUnit = action.observationsByUnit,
                                coverMissionZone = action.coverMissionZone,
                                controlPlans = action.controlPlans,
                            )
                        }
                    )
                }

                envAction?.controlAction != null -> {
                    val action = envAction.controlAction.action ?: return null
                    val completenessForStats = CompletenessForStatsEntity(
                        status = if (envAction.controlAction.isCompleteForStats == true) CompletenessForStatsStatusEnum.COMPLETE else CompletenessForStatsStatusEnum.INCOMPLETE,
                        sources = envAction.controlAction.sourcesOfMissingDataForStats
                    )
                    Action(
                        id = action.id,
                        missionId = missionId,
                        completenessForStats = completenessForStats,
                        source = MissionSourceEnum.MONITORENV,
                        startDateTimeUtc = action.actionStartDateTimeUtc,
                        endDateTimeUtc = action.actionEndDateTimeUtc,
                        data = action.actionStartDateTimeUtc?.let {
                            EnvActionData(
                                id = action.id,
                                actionStartDateTimeUtc = it,
                                actionEndDateTimeUtc = action.actionEndDateTimeUtc,
                                actionType = action.actionType,
                                geom = action.geom,
                                observations = action.observations,
                                observationsByUnit = action.observationsByUnit,
                                actionNumberOfControls = action.actionNumberOfControls,
                                actionTargetType = action.actionTargetType,
                                vehicleType = action.vehicleType,
                                infractions = action.infractions,
                                controlPlans = action.controlPlans,
                                isAdministrativeControl = action.isAdministrativeControl,
                                isComplianceWithWaterRegulationsControl = action.isComplianceWithWaterRegulationsControl,
                                isSafetyEquipmentAndStandardsComplianceControl = action.isSafetyEquipmentAndStandardsComplianceControl,
                                isSeafarersControl = action.isSeafarersControl,
                                controlAdministrative = ControlAdministrative.fromControlAdministrativeEntity(envAction.controlAction.controlAdministrative),
                                controlNavigation = ControlNavigation.fromControlNavigationEntity(envAction.controlAction.controlNavigation),
                                controlSecurity = ControlSecurity.fromControlSecurityEntity(envAction.controlAction.controlSecurity),
                                controlGensDeMer = ControlGensDeMer.fromControlGensDeMerEntity(envAction.controlAction.controlGensDeMer)
                            )
                        }
                    )
                }

                else -> null
            }
        }

        fun fromFishAction(fishAction: ExtendedFishActionEntity, missionId: Int): Action? {
            return fishAction.controlAction?.action?.let {
                val action = fishAction.controlAction.action
                val completenessForStats = CompletenessForStatsEntity(
                    status = if (fishAction.controlAction.isCompleteForStats == true) CompletenessForStatsStatusEnum.COMPLETE else CompletenessForStatsStatusEnum.INCOMPLETE,
                    sources = fishAction.controlAction.sourcesOfMissingDataForStats
                )
                return Action(
                    id = action.id.toString(),
                    missionId = missionId,
                    completenessForStats = completenessForStats,
                    source = MissionSourceEnum.MONITORFISH,
                    startDateTimeUtc = action.actionDatetimeUtc,
                    endDateTimeUtc = action.actionEndDatetimeUtc,
                    data = FishActionData(
                        id = action.id.toString(),
                        missionId = action.missionId,
                        actionType = action.actionType,
                        vesselId = action.vesselId,
                        vesselName = action.vesselName,
                        internalReferenceNumber = action.internalReferenceNumber,
                        externalReferenceNumber = action.externalReferenceNumber,
                        ircs = action.ircs,
                        flagState = action.flagState.alpha3,
                        districtCode = action.districtCode,
                        faoAreas = action.faoAreas,
                        actionDatetimeUtc = action.actionDatetimeUtc,
                        actionEndDatetimeUtc = action.actionEndDatetimeUtc,
                        observationsByUnit = action.observationsByUnit,
                        emitsVms = action.emitsVms,
                        emitsAis = action.emitsAis,
                        flightGoals = action.flightGoals,
                        logbookMatchesActivity = action.logbookMatchesActivity,
                        licencesMatchActivity = action.licencesMatchActivity,
                        speciesWeightControlled = action.speciesWeightControlled,
                        speciesSizeControlled = action.speciesSizeControlled,
                        separateStowageOfPreservedSpecies = action.separateStowageOfPreservedSpecies,
                        logbookInfractions = action.logbookInfractions,
                        licencesAndLogbookObservations = action.licencesAndLogbookObservations,
                        gearInfractions = action.gearInfractions,
                        speciesInfractions = action.speciesInfractions,
                        speciesObservations = action.speciesObservations,
                        seizureAndDiversion = action.seizureAndDiversion,
                        otherInfractions = action.otherInfractions,
                        numberOfVesselsFlownOver = action.numberOfVesselsFlownOver,
                        unitWithoutOmegaGauge = action.unitWithoutOmegaGauge,
                        controlQualityComments = action.controlQualityComments,
                        feedbackSheetRequired = action.feedbackSheetRequired,
                        userTrigram = action.userTrigram,
                        segments = action.segments,
                        facade = action.facade,
                        longitude = action.longitude,
                        latitude = action.latitude,
                        portLocode = action.portLocode,
                        portName = action.portName,
                        vesselTargeted = action.vesselTargeted,
                        seizureAndDiversionComments = action.seizureAndDiversionComments,
                        otherComments = action.otherComments,
                        gearOnboard = action.gearOnboard,
                        speciesOnboard = action.speciesOnboard,
                        controlUnits = action.controlUnits,
                        isDeleted = action.isDeleted,
                        hasSomeGearsSeized = action.hasSomeGearsSeized,
                        hasSomeSpeciesSeized = action.hasSomeSpeciesSeized,
                        isAdministrativeControl = action.isAdministrativeControl,
                        isComplianceWithWaterRegulationsControl = action.isComplianceWithWaterRegulationsControl,
                        isSafetyEquipmentAndStandardsComplianceControl = action.isSafetyEquipmentAndStandardsComplianceControl,
                        isSeafarersControl = action.isSeafarersControl,
                        // controls
                        controlAdministrative = ControlAdministrative.fromControlAdministrativeEntity(fishAction.controlAction.controlAdministrative),
                        controlNavigation = ControlNavigation.fromControlNavigationEntity(fishAction.controlAction.controlNavigation),
                        controlSecurity = ControlSecurity.fromControlSecurityEntity(fishAction.controlAction.controlSecurity),
                        controlGensDeMer = ControlGensDeMer.fromControlGensDeMerEntity(fishAction.controlAction.controlGensDeMer),
                    )
                )
            }
        }

        fun fromNavAction(navAction: NavActionEntity): Action {
            var data: ActionData? = null
            when {
                navAction.statusAction != null -> {
                    data = navAction.statusAction.toNavActionStatus()
                }

                navAction.controlAction != null -> {
                    data = navAction.controlAction.toNavActionControl()
                }

                navAction.freeNoteAction != null -> {
                    data = navAction.freeNoteAction.toNavActionFreeNote()
                }

                navAction.rescueAction != null -> {
                    data = navAction.rescueAction.toNavActionRescue()
                }

                navAction.nauticalEventAction != null -> {
                    data = navAction.nauticalEventAction.toNavActionNauticalEvent()
                }

                navAction.baaemPermanenceAction != null -> {
                    data = navAction.baaemPermanenceAction.toNavActionBAAEMPermanence()
                }

                navAction.vigimerAction != null -> {
                    data = navAction.vigimerAction.toNavActionVigimer()
                }

                navAction.antiPollutionAction != null -> {
                    data = navAction.antiPollutionAction.toNavActionAntiPollution()
                }

                navAction.publicOrderAction != null -> {
                    data = navAction.publicOrderAction.toNavActionPublicOrder()
                }

                navAction.representationAction != null -> {
                    data = navAction.representationAction.toNavActionRepresentation()
                }

                navAction.illegalImmigrationAction != null -> {
                    data = navAction.illegalImmigrationAction.toNavActionIllegalImmigration()
                }
            }
            val completenessForStats = CompletenessForStatsEntity(
                status = when (navAction.isCompleteForStats) {
                    true -> {
                        CompletenessForStatsStatusEnum.COMPLETE
                    }

                    false -> {
                        CompletenessForStatsStatusEnum.INCOMPLETE
                    }

                    else -> {
                        null
                    }
                },
                sources = navAction.sourcesOfMissingDataForStats
            )
            return Action(
                id = navAction.id,
                missionId = navAction.missionId,
                source = MissionSourceEnum.RAPPORTNAV,
                startDateTimeUtc = navAction.startDateTimeUtc,
                endDateTimeUtc = navAction.endDateTimeUtc,
                completenessForStats = completenessForStats,
                data = data,
            )
        }

    }
}
