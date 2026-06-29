package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.SatiMapper
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Sati
import java.time.Instant

class MissionFishActionData(
    override val startDateTimeUtc: Instant? = null,
    override val endDateTimeUtc: Instant? = null,
    override val vesselId: Int? = null,
    override val vesselName: String? = null,
    override val internalReferenceNumber: String? = null,
    override val externalReferenceNumber: String? = null,
    override val flagState: CountryCode? = null,
    override val districtCode: String? = null,
    override val faoAreas: List<String> = listOf(),
    override val fishActionType: MissionActionType,
    override val emitsVms: ControlCheck? = null,
    override val emitsAis: ControlCheck? = null,
    override val vmsEmissionControlBeforeArrival: ControlCheck? = null,
    override val portEntranceAndLandingAuthorized: ControlCheck? = null,
    override val logbookFilledPriorToControl: ControlCheck? = null,
    override val logbookMatchesActivity: ControlCheck? = null,
    override val licencesMatchActivity: ControlCheck? = null,
    override val speciesWeightControlled: ControlCheck? = null,
    override val speciesSizeControlled: ControlCheck? = null,
    override val separateStowageOfPreservedSpecies: ControlCheck? = null,
    override val propulsionEnginePowerControl: ControlCheck? = null,
    override val fishingLicencesMatchActivity: ControlCheck? = null,
    override val stowagePlanPresent: ControlCheck? = null,
    override val onboardWeighingPermit: ControlCheck? = null,
    override val weighingCertificateAndSystemsValid: ControlCheck? = null,
    override val underSizedSeparateStowage: ControlCheck? = null,
    override val underSizedSeparateRecording: ControlCheck? = null,
    override val minimumConservationReferenceSizeControlled: ControlCheck? = null,
    override val cratesWeighingSamplingControl: ControlCheck? = null,
    override val approvedWeighingOperatorInformation: ControlCheck? = null,
    override val holdControlledAfterUnloading: ControlCheck? = null,
    override val catchesWeighedAtLanding: ControlCheck? = null,
    override val licencesAndLogbookObservations: String? = null,
    override val speciesObservations: String? = null,
    override val seizureAndDiversion: Boolean? = null,
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
    override val speciesOnboard: List<SpeciesOnboardControl> = listOf(),
    override val discardedSpecies: List<DiscardedSpeciesControl> = listOf(),
    override val isFromPoseidon: Boolean? = null,
    override val isDeleted: Boolean? = null,
    override val hasSomeGearsSeized: Boolean? = null,
    override val hasSomeSpeciesSeized: Boolean? = null,
    override val completedBy: String? = null,
    override val completion: Completion? = null,
    override val isLastHaul: Boolean? = null,
    override val isAdministrativeControl: Boolean? = null,
    override val isComplianceWithWaterRegulationsControl: Boolean? = null,
    override val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    override val isSeafarersControl: Boolean? = null,
    override val isINNControl: Boolean? = null,
    override val isGangwayDeployed: Boolean? = null,
    override var observationsByUnit: String? = null,
    override var speciesQuantitySeized: Int? = null,
    override val targets: List<Target>? = null,
    override val fishInfractions: List<FishInfraction> = listOf(),
    override val sati: Sati? = null,
    override val hasDivingDuringOperation: Boolean? = null,
    override val incidentDuringOperation: Boolean? = null
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
                ownerId = input.ownerId,
                fishActionType = data.fishActionType,
                actionDatetimeUtc = data.startDateTimeUtc,
                actionEndDatetimeUtc = data.endDateTimeUtc,
                observationsByUnit = data.observationsByUnit,
                incidentDuringOperation = data.incidentDuringOperation,
                hasDivingDuringOperation = data.hasDivingDuringOperation,
                sati = data.sati?.let {  SatiMapper.toEntity(data.sati)}
            )
            return action
        }

    }
}
