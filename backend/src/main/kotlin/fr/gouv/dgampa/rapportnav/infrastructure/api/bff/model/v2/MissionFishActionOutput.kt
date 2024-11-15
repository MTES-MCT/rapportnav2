package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity


class MissionFishActionOutput(
    override val id: String,
    override val missionId: Int,
    override val actionType: ActionType,
    override val isCompleteForStats: Boolean?,
    override val status: ActionStatusType? = null,
    override val summaryTags: List<String>? = null,
    override val completenessForStats: CompletenessForStatsEntity? = null,
    override val sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    override val data: MissionFishActionDataOutput
) : MissionActionOutput(
    id = id,
    missionId = missionId,
    status = status,
    actionType = actionType,
    summaryTags = summaryTags,
    data = data
) {
    companion object {
        fun fromMissionActionEntity(action: MissionActionEntity): MissionFishActionOutput {
            val fishAction = action as MissionFishActionEntity
            return MissionFishActionOutput(
                id = fishAction.id.toString(),
                missionId = fishAction.missionId,
                actionType = fishAction.actionType,
                summaryTags = fishAction.summaryTags,
                isCompleteForStats = fishAction.isCompleteForStats,
                sourcesOfMissingDataForStats = fishAction.sourcesOfMissingDataForStats,
                data = MissionFishActionDataOutput(
                    fishActionType = fishAction.fishActionType,
                    startDateTimeUtc = fishAction.actionDatetimeUtc,
                    endDateTimeUtc = fishAction.actionEndDatetimeUtc,
                    vesselId = fishAction.vesselId,
                    vesselName = fishAction.vesselName,
                    internalReferenceNumber = fishAction.internalReferenceNumber,
                    externalReferenceNumber = fishAction.externalReferenceNumber,
                    districtCode = fishAction.districtCode,
                    faoAreas = fishAction.faoAreas,
                    emitsVms = fishAction.emitsVms,
                    emitsAis = fishAction.emitsAis,
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
                    isFromPoseidon = fishAction.isFromPoseidon,
                    isDeleted = fishAction.isDeleted,
                    hasSomeGearsSeized = fishAction.hasSomeGearsSeized,
                    hasSomeSpeciesSeized = fishAction.hasSomeSpeciesSeized,
                    completedBy = fishAction.completedBy,
                    completion = fishAction.completion,
                    isAdministrativeControl = fishAction.isAdministrativeControl,
                    isComplianceWithWaterRegulationsControl = fishAction.isComplianceWithWaterRegulationsControl,
                    isSafetyEquipmentAndStandardsComplianceControl = fishAction.isSafetyEquipmentAndStandardsComplianceControl,
                    isSeafarersControl = fishAction.isSeafarersControl,
                    observationsByUnit = fishAction.observationsByUnit,
                    speciesQuantitySeized = fishAction.speciesQuantitySeized,
                    controlSecurity = fishAction.controlSecurity,
                    controlGensDeMer = fishAction.controlGensDeMer,
                    controlNavigation = fishAction.controlNavigation,
                    controlAdministrative = fishAction.controlAdministrative
                )
            )
        }
    }
}
