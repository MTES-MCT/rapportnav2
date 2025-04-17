package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity


class MissionFishAction(
    override val id: String,
    override val missionId: Int,
    override val actionType: ActionType,
    override val source: MissionSourceEnum,
    override val isCompleteForStats: Boolean? = null,
    override val status: ActionStatusType? = null,
    override val summaryTags: List<String>? = null,
    override val controlsToComplete: List<ControlType>? = null,
    override val completenessForStats: CompletenessForStatsEntity? = null,
    override val sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    override val data: MissionFishActionData
) : MissionAction(
    id = id,
    missionId = missionId,
    status = status,
    actionType = actionType,
    summaryTags = summaryTags,
    source = source,
    controlsToComplete = controlsToComplete,
    data = data
) {
    companion object {
        fun fromMissionActionEntity(action: MissionActionEntity): MissionFishAction {
            val fishAction = action as MissionFishActionEntity
            return MissionFishAction(
                id = fishAction.id.toString(),
                source = fishAction.source,
                missionId = fishAction.missionId,
                actionType = fishAction.actionType,
                summaryTags = fishAction.summaryTags,
                isCompleteForStats = fishAction.isCompleteForStats,
                controlsToComplete = fishAction.controlsToComplete,
                completenessForStats = fishAction.completenessForStats,
                sourcesOfMissingDataForStats = fishAction.sourcesOfMissingDataForStats,
                data = MissionFishActionData(
                    fishActionType = fishAction.fishActionType,
                    startDateTimeUtc = fishAction.actionDatetimeUtc,
                    endDateTimeUtc = fishAction.actionEndDatetimeUtc,
                    vesselId = fishAction.vesselId,
                    vesselName = fishAction.vesselName,
                    internalReferenceNumber = fishAction.internalReferenceNumber,
                    externalReferenceNumber = fishAction.externalReferenceNumber,
                    districtCode = fishAction.districtCode,
                    faoAreas = fishAction.faoAreas?: listOf(),
                    emitsVms = fishAction.emitsVms,
                    emitsAis = fishAction.emitsAis,
                    logbookMatchesActivity = fishAction.logbookMatchesActivity,
                    licencesMatchActivity = fishAction.licencesMatchActivity,
                    speciesWeightControlled = fishAction.speciesWeightControlled,
                    speciesSizeControlled = fishAction.speciesSizeControlled,
                    separateStowageOfPreservedSpecies = fishAction.separateStowageOfPreservedSpecies,
                    logbookInfractions = fishAction.logbookInfractions?: listOf(),
                    licencesAndLogbookObservations = fishAction.licencesAndLogbookObservations,
                    gearInfractions = fishAction.gearInfractions?: listOf(),
                    speciesInfractions = fishAction.speciesInfractions?: listOf(),
                    speciesObservations = fishAction.speciesObservations,
                    seizureAndDiversion = fishAction.seizureAndDiversion,
                    otherInfractions = fishAction.otherInfractions?: listOf(),
                    numberOfVesselsFlownOver = fishAction.numberOfVesselsFlownOver,
                    unitWithoutOmegaGauge = fishAction.unitWithoutOmegaGauge,
                    controlQualityComments = fishAction.controlQualityComments,
                    feedbackSheetRequired = fishAction.feedbackSheetRequired,
                    userTrigram = fishAction.userTrigram,
                    segments = fishAction.segments?: listOf(),
                    facade = fishAction.facade,
                    longitude = fishAction.longitude,
                    latitude = fishAction.latitude,
                    portLocode = fishAction.portLocode,
                    portName = fishAction.portName,
                    vesselTargeted = fishAction.vesselTargeted,
                    seizureAndDiversionComments = fishAction.seizureAndDiversionComments,
                    otherComments = fishAction.otherComments,
                    gearOnboard = fishAction.gearOnboard?: listOf(),
                    speciesOnboard = fishAction.speciesOnboard?: listOf(),
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
                    targets = fishAction.targets?.map { Target2.fromTargetEntity(it) }?.sortedBy { it.startDateTimeUtc },
                    controlSecurity = ControlSecurity.fromControlSecurityEntity(fishAction.controlSecurity),
                    controlGensDeMer = ControlGensDeMer.fromControlGensDeMerEntity(fishAction.controlGensDeMer),
                    controlNavigation = ControlNavigation.fromControlNavigationEntity(fishAction.controlNavigation),
                    controlAdministrative = ControlAdministrative.fromControlAdministrativeEntity(fishAction.controlAdministrative)
                )
            )
        }
    }
}
