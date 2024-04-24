package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlSecurity
import java.time.ZonedDateTime

data class Action(
    val id: Any?,
    val missionId: Int,
//    val type: ActionTypeEnum,
    val source: MissionSourceEnum,
    val startDateTimeUtc: ZonedDateTime?,
    val endDateTimeUtc: ZonedDateTime?,
    val summaryTags: List<String>? = null,
    val data: ActionData?
) {

    companion object {
        fun fromEnvAction(envAction: ExtendedEnvActionEntity? = null, missionId: Int): Action? {

            return when {
                envAction?.surveillanceAction != null -> {
                    val action = envAction.surveillanceAction.action
                    Action(
                        id = action.id,
                        missionId = missionId,
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
                                themes = action.themes,
                                coverMissionZone = action.coverMissionZone
                            )
                        }
                    )
                }
                envAction?.controlAction != null -> {
                    val action = envAction.controlAction.action ?: return null
                    Action(
                        id = action.id,
                        missionId = missionId,
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
                                actionNumberOfControls = action.actionNumberOfControls,
                                actionTargetType = action.actionTargetType,
                                vehicleType = action.vehicleType,
                                infractions = action.infractions,
                                themes = action.themes,
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
                val action = fishAction.controlAction?.action
                return Action(
                    id = action.id.toString(),
                    missionId = missionId,
                    source = MissionSourceEnum.MONITORFISH,
                    startDateTimeUtc = action.actionDatetimeUtc,
                    endDateTimeUtc = null, // Set to null for FishAction since it doesn't have an endDateTime
                    data = FishActionData(
                        id = action.id.toString(),
                        missionId = action.missionId,
                        actionType = action.actionType,
                        vesselId = action.vesselId,
                        vesselName = action.vesselName,
                        internalReferenceNumber = action.internalReferenceNumber,
                        externalReferenceNumber = action.externalReferenceNumber,
                        ircs = action.ircs,
                        flagState = action.flagState,
                        districtCode = action.districtCode,
                        faoAreas = action.faoAreas,
                        actionDatetimeUtc = action.actionDatetimeUtc,
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
                        controlAdministrative = ControlAdministrative.fromControlAdministrativeEntity(fishAction.controlAction?.controlAdministrative),
                        controlNavigation = ControlNavigation.fromControlNavigationEntity(fishAction.controlAction?.controlNavigation),
                        controlSecurity = ControlSecurity.fromControlSecurityEntity(fishAction.controlAction?.controlSecurity),
                        controlGensDeMer = ControlGensDeMer.fromControlGensDeMerEntity(fishAction.controlAction?.controlGensDeMer),
                    )
                )
            }
        }

        fun fromNavAction(navAction: NavActionEntity): Action? {
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
                navAction.baaemPermanenceAction!= null -> {
                    data = navAction.baaemPermanenceAction.toNavActionBAAEMPermanence()
                }
                navAction.vigimerAction != null -> {
                    data = navAction.vigimerAction.toNavActionVigimer()
                }
                navAction.antiPollutionAction != null -> {
                    data = navAction.antiPollutionAction.toNavActionAntiPollution()
                }
            }
            return Action(
                id = navAction.id,
                missionId = navAction.missionId,
                source = MissionSourceEnum.RAPPORTNAV,
                startDateTimeUtc = navAction.startDateTimeUtc,
                endDateTimeUtc = navAction.endDateTimeUtc,
                data = data
            )
        }

    }
}
