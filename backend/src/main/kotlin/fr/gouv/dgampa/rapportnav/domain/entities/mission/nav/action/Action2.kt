package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FishAction
import java.time.ZonedDateTime

data class Action2(
    val id: Any?,
    val missionId: Int,
//    val type: ActionTypeEnum,
    val source: MissionSourceEnum,
    val startDateTimeUtc: ZonedDateTime?,
    val endDateTimeUtc: ZonedDateTime?,
    val data: ActionData?
) {
    companion object {
        fun fromEnvAction(envAction: EnvActionEntity, missionId: Int): Action2 {

            if (envAction is EnvActionControlEntity) {
                return Action2(
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
                        themes = envAction.themes
                    )
                )
            }
            return Action2(
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

        fun fromFishAction(fishAction: FishAction, missionId: Int): Action2 {
            return Action2(
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


        fun fromNavAction(navAction: NavAction): Action2 {
            return Action2(
                id = navAction.id.toString(),
                missionId = navAction.missionId,
                source = MissionSourceEnum.RAPPORTNAV,
                startDateTimeUtc = navAction.actionStartDateTimeUtc,
                endDateTimeUtc = navAction.actionEndDateTimeUtc,
                data = NavActionData(
                    id = navAction.id,
                    actionStartDateTimeUtc = navAction.actionStartDateTimeUtc,
                    actionEndDateTimeUtc = navAction.actionEndDateTimeUtc,
                    actionType = navAction.actionType,
                    actionControl = navAction.actionControl,
                )
            )
        }
    }
}
