package fr.gouv.dgampa.rapportnav.infrastructure.bff.model

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FishAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import java.time.ZonedDateTime

data class Action(
    val id: Any?,
    val missionId: Int,
//    val type: ActionTypeEnum,
    val source: MissionSourceEnum,
    val startDateTimeUtc: ZonedDateTime?,
    val endDateTimeUtc: ZonedDateTime?,
    val data: ActionData?
) {

    companion object {
        fun fromEnvAction(envAction: EnvActionEntity, missionId: Int): Action {

            if (envAction is EnvActionControlEntity) {
                return Action(
                    id = envAction.id,
                    missionId = missionId,
                    source = MissionSourceEnum.MONITORENV,
                    startDateTimeUtc = envAction.actionStartDateTimeUtc,
                    endDateTimeUtc = envAction.actionEndDateTimeUtc,
                    data = EnvActionData(
                        id = envAction.id,
                        actionStartDateTimeUtc = envAction.actionStartDateTimeUtc!!,
                        actionEndDateTimeUtc = envAction.actionEndDateTimeUtc,
                        actionType = envAction.actionType,
                        observations = envAction.observations,
                        actionNumberOfControls = envAction.actionNumberOfControls,
                        actionTargetType = envAction.actionTargetType,
                        vehicleType = envAction.vehicleType,
                        infractions = envAction.infractions,
                        themes = envAction.themes,
                        isAdministrativeControl = envAction.isAdministrativeControl,
                        isComplianceWithWaterRegulationsControl = envAction.isComplianceWithWaterRegulationsControl,
                        isSafetyEquipmentAndStandardsComplianceControl = envAction.isSafetyEquipmentAndStandardsComplianceControl,
                        isSeafarersControl = envAction.isSeafarersControl,

                    )
                )
            }
            return Action(
                id = envAction.id,
                missionId = missionId,
                source = MissionSourceEnum.MONITORENV,
                startDateTimeUtc = envAction.actionStartDateTimeUtc,
                endDateTimeUtc = envAction.actionEndDateTimeUtc,
                data = EnvActionData(
                    id = envAction.id,
                    actionStartDateTimeUtc = envAction.actionStartDateTimeUtc!!,
                    actionEndDateTimeUtc = envAction.actionEndDateTimeUtc,
                    actionType = envAction.actionType,
                )
            )
        }

        fun fromFishAction(fishAction: FishAction, missionId: Int): Action {
            return Action(
                id = fishAction.id.toString(),
                missionId = missionId,
                source = MissionSourceEnum.MONITORFISH,
                startDateTimeUtc = fishAction.actionDatetimeUtc,
                endDateTimeUtc = null, // Set to null for FishAction since it doesn't have an endDateTime
                data = FishActionData(
                    id = fishAction.id.toString(),
                    missionId = fishAction.missionId,
                    actionType = fishAction.actionType,
                    vesselId = fishAction.vesselId,
                    vesselName = fishAction.vesselName,
                    internalReferenceNumber = fishAction.internalReferenceNumber,
                    externalReferenceNumber = fishAction.externalReferenceNumber,
                    ircs = fishAction.ircs,
                    flagState = fishAction.flagState,
                    districtCode = fishAction.districtCode,
                    faoAreas = fishAction.faoAreas,
                    actionDatetimeUtc = fishAction.actionDatetimeUtc,
                    emitsVms = fishAction.emitsVms,
                    emitsAis = fishAction.emitsAis,
                    flightGoals = fishAction.flightGoals,
                    logbookMatchesActivity = fishAction.logbookMatchesActivity,
                    licencesMatchActivity = fishAction.licencesMatchActivity,
                    speciesWeightControlled = fishAction.speciesWeightControlled,
                    speciesSizeControlled = fishAction.speciesSizeControlled,
                    separateStowageOfPreservedSpecies = fishAction.separateStowageOfPreservedSpecies,
                    logbookInfractions = fishAction.logbookInfractions,
                    licencesAndLogbookObservations = fishAction.licencesAndLogbookObservations,
                    gearInfractions = fishAction.gearInfractions,
                    speciesInfractions = fishAction.speciesInfractions,
                    speciesObservations = fishAction.speciesObservations,
                    seizureAndDiversion = fishAction.seizureAndDiversion,
                    otherInfractions = fishAction.otherInfractions,
                    numberOfVesselsFlownOver = fishAction.numberOfVesselsFlownOver,
                    unitWithoutOmegaGauge = fishAction.unitWithoutOmegaGauge,
                    controlQualityComments = fishAction.controlQualityComments,
                    feedbackSheetRequired = fishAction.feedbackSheetRequired,
                    userTrigram = fishAction.userTrigram,
                    segments = fishAction.segments,
                    facade = fishAction.facade,
                    longitude = fishAction.longitude,
                    latitude = fishAction.latitude,
                    portLocode = fishAction.portLocode,
                    portName = fishAction.portName,
                    vesselTargeted = fishAction.vesselTargeted,
                    seizureAndDiversionComments = fishAction.seizureAndDiversionComments,
                    otherComments = fishAction.otherComments,
                    gearOnboard = fishAction.gearOnboard,
                    speciesOnboard = fishAction.speciesOnboard,
                    controlUnits = fishAction.controlUnits,
                    isDeleted = fishAction.isDeleted,
                    hasSomeGearsSeized = fishAction.hasSomeGearsSeized,
                    hasSomeSpeciesSeized = fishAction.hasSomeSpeciesSeized
                )
            )
        }


        fun fromNavAction(navAction: NavActionEntity): Action {
            var data: ActionData? = null
            if (navAction.statusAction != null) {
                data = navAction.statusAction.toNavActionStatus()
            }
            else if (navAction.controlAction != null) {
                data = navAction.controlAction.toNavActionControl()
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

        fun sortForTimeline(allActions:  List<Action>?): List<Action>? {
            return allActions?.sortedWith(compareByDescending<Action> { it.startDateTimeUtc }
                .thenBy { it.data is NavActionStatus }
                .thenBy { (it.data as? NavActionStatus)?.isStart == false }
                .thenBy { (it.data as? NavActionStatus)?.isStart == true }
            )
        }




    }
}
