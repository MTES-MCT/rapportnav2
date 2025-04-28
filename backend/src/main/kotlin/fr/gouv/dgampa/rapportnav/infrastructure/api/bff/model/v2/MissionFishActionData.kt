package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import java.time.Instant

class MissionFishActionData(
    override val startDateTimeUtc: Instant? = null,
    override val endDateTimeUtc: Instant? = null,
    override val vesselId: Int? = null,
    override val vesselName: String? = null,
    override val internalReferenceNumber: String? = null,
    override val externalReferenceNumber: String? = null,
    override val districtCode: String? = null,
    override val faoAreas: List<String> = listOf(),
    override val fishActionType: MissionActionType,
    override val emitsVms: ControlCheck? = null,
    override val emitsAis: ControlCheck? = null,
    override val logbookMatchesActivity: ControlCheck? = null,
    override val licencesMatchActivity: ControlCheck? = null,
    override val speciesWeightControlled: Boolean? = null,
    override val speciesSizeControlled: Boolean? = null,
    override val separateStowageOfPreservedSpecies: ControlCheck? = null,
    override val logbookInfractions: List<LogbookInfraction> = listOf(),
    override val licencesAndLogbookObservations: String? = null,
    override val gearInfractions: List<GearInfraction> = listOf(),
    override val speciesInfractions: List<SpeciesInfraction> = listOf(),
    override val speciesObservations: String? = null,
    override val seizureAndDiversion: Boolean? = null,
    override val otherInfractions: List<OtherInfraction> = listOf(),
    override val numberOfVesselsFlownOver: Int? = null,
    override val unitWithoutOmegaGauge: Boolean? = null,
    override val controlQualityComments: String? = null,
    override val feedbackSheetRequired: Boolean? = null,
    override val userTrigram: String? = null,
    override val segments: List<FleetSegment> = listOf(),
    override val facade: String? = null,
    override val longitude: Double? = null,
    override val latitude: Double? = null,
    override val portLocode: String? = null,
    // This field is only used when fetching missions
    override var portName: String? = null,
    override val vesselTargeted: ControlCheck? = null,
    override val seizureAndDiversionComments: String? = null,
    override val otherComments: String? = null,
    override val gearOnboard: List<GearControl> = listOf(),
    override val speciesOnboard: List<SpeciesControl> = listOf(),
    override val isFromPoseidon: Boolean? = null,
    override val isDeleted: Boolean? = null,
    override val hasSomeGearsSeized: Boolean? = null,
    override val hasSomeSpeciesSeized: Boolean? = null,
    override val completedBy: String? = null,
    override val completion: Completion? = null,
    override val isAdministrativeControl: Boolean? = null,
    override val isComplianceWithWaterRegulationsControl: Boolean? = null,
    override val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    override val isSeafarersControl: Boolean? = null,
    override var observationsByUnit: String? = null,
    override var speciesQuantitySeized: Int? = null,
    override val targets: List<Target2>? = null
) : MissionActionData(
    startDateTimeUtc = startDateTimeUtc,
    endDateTimeUtc = endDateTimeUtc,
    targets = targets
), BaseMissionFishActionData {
    companion object {
        fun toMissionFishActionEntity(input: MissionAction): MissionFishActionEntity {
            val data = input.data as MissionFishActionData
            val action = MissionFishActionEntity(
                id = Integer.parseInt(input.id),
                missionId = input.missionId,
                fishActionType = MissionActionType.AIR_CONTROL,
                observationsByUnit = data.observations,
                actionDatetimeUtc = data.startDateTimeUtc,
                actionEndDatetimeUtc = data.endDateTimeUtc
            )
            return action
        }

    }
}
