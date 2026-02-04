package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output

import com.fasterxml.jackson.annotation.JsonInclude
import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlUnit
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import java.time.ZonedDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MissionActionInfractionDataOutput(
    val infractionType: InfractionType,
    // This field is used to control the Threat CheckTreePicker
    val threats: List<ThreatHierarchyDataOutput>? = null,
    val natinf: Int? = null,
    val natinfDescription: String? = null,
    val threat: String? = null,
    val threatCharacterization: String? = null,
    val comments: String? = null,
) {
    companion object {
        fun fromInfractionWithThreatHierarchy(infraction: Infraction) =
            MissionActionInfractionDataOutput(
                infractionType = infraction.infractionType!!,
                threats =
                    listOf(
                        InfractionThreatCharacterizationDataOutput.fromInfraction(infraction),
                    ),
                comments = infraction.comments,
            )

        fun fromInfraction(infraction: Infraction) =
            MissionActionInfractionDataOutput(
                infractionType = infraction.infractionType!!,
                natinf = infraction.natinf,
                natinfDescription = infraction.natinfDescription,
                threat = infraction.threat ?: "Famille inconnue",
                threatCharacterization = infraction.threatCharacterization ?: "Type inconnu",
                comments = infraction.comments,
            )
    }

    fun toFishInfraction() =
        FishInfraction(
            infractionType = infractionType,
            natinf = natinf,
            natinfDescription = natinfDescription,
            threat = threat,
            threatCharacterization = threatCharacterization,
            comments = comments,
        )
}


data class MissionActionDataOutput(
    val id: Int? = null,
    val vesselId: Int? = null,
    val vesselName: String? = null,
    val internalReferenceNumber: String? = null,
    val externalReferenceNumber: String? = null,
    val ircs: String? = null,
    val flagState: CountryCode,
    val districtCode: String? = null,
    val faoAreas: List<String> = listOf(),
    val flightGoals: List<FlightGoal> = listOf(),
    val missionId: Int,
    val actionType: MissionActionType,
    val actionDatetimeUtc: ZonedDateTime,
    val actionEndDatetimeUtc: ZonedDateTime? = null,
    val emitsVms: ControlCheck? = null,
    val emitsAis: ControlCheck? = null,
    val logbookMatchesActivity: ControlCheck? = null,
    val licencesMatchActivity: ControlCheck? = null,
    val speciesWeightControlled: Boolean? = null,
    val speciesSizeControlled: Boolean? = null,
    val separateStowageOfPreservedSpecies: ControlCheck? = null,
    val licencesAndLogbookObservations: String? = null,
    val infractions: List<MissionActionInfractionDataOutput> = listOf(),
    val speciesObservations: String? = null,
    val seizureAndDiversion: Boolean? = null,
    val numberOfVesselsFlownOver: Int? = null,
    val unitWithoutOmegaGauge: Boolean? = null,
    val controlQualityComments: String? = null,
    val segments: List<FleetSegment> = listOf(),
    val facade: String? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val portLocode: String? = null,
    val portName: String? = null,
    val seizureAndDiversionComments: String? = null,
    val otherComments: String? = null,
    val gearOnboard: List<GearControl> = listOf(),
    val speciesOnboard: List<SpeciesControl> = listOf(),
    val controlUnits: List<ControlUnit> = listOf(),
    val userTrigram: String,
    val vesselTargeted: ControlCheck? = null,
    val hasSomeGearsSeized: Boolean,
    val hasSomeSpeciesSeized: Boolean,
    val speciesQuantitySeized: Int? = null,
    val completedBy: String? = null,
    val completion: Completion,
    val isFromPoseidon: Boolean,
    val isAdministrativeControl: Boolean? = null,
    val isComplianceWithWaterRegulationsControl: Boolean? = null,
    val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    val isSeafarersControl: Boolean? = null,
    val observationsByUnit: String? = null,
    val isDeleted: Boolean,
) {
    fun toMissionAction(): MissionAction {
        return MissionAction(
            id = this.id,
            missionId = this.missionId,
            infractions = this.infractions.map { it.toFishInfraction() },
            vesselId = this.vesselId,
            vesselName = this.vesselName,
            internalReferenceNumber = this.internalReferenceNumber,
            externalReferenceNumber = this.externalReferenceNumber,
            ircs = this.ircs,
            flagState = this.flagState,
            districtCode = this.districtCode,
            faoAreas = this.faoAreas,
            actionType = this.actionType,
            actionDatetimeUtc = this.actionDatetimeUtc.toInstant(),
            actionEndDatetimeUtc = this.actionEndDatetimeUtc?.toInstant(),
            emitsVms = this.emitsVms,
            emitsAis = this.emitsAis,
            flightGoals = this.flightGoals,
            logbookMatchesActivity = this.logbookMatchesActivity,
            licencesMatchActivity = this.licencesMatchActivity,
            speciesWeightControlled = this.speciesWeightControlled,
            speciesSizeControlled = this.speciesSizeControlled,
            separateStowageOfPreservedSpecies = this.separateStowageOfPreservedSpecies,
            licencesAndLogbookObservations = this.licencesAndLogbookObservations,
            speciesObservations = this.speciesObservations,
            seizureAndDiversion = this.seizureAndDiversion,
            numberOfVesselsFlownOver = this.numberOfVesselsFlownOver,
            unitWithoutOmegaGauge = this.unitWithoutOmegaGauge,
            controlQualityComments = this.controlQualityComments,
            userTrigram = this.userTrigram,
            segments = this.segments,
            facade = this.facade,
            longitude = this.longitude,
            latitude = this.latitude,
            portLocode = this.portLocode,
            portName = this.portName,
            vesselTargeted = this.vesselTargeted,
            seizureAndDiversionComments = this.seizureAndDiversionComments,
            otherComments = this.otherComments,
            gearOnboard = this.gearOnboard,
            speciesOnboard = this.speciesOnboard,
            isFromPoseidon = this.isFromPoseidon,
            controlUnits = this.controlUnits,
            isDeleted = isDeleted,
            hasSomeGearsSeized = this.hasSomeGearsSeized,
            hasSomeSpeciesSeized = this.hasSomeSpeciesSeized,
            completedBy = this.completedBy,
            completion = this.completion,
            isAdministrativeControl = this.isAdministrativeControl,
            isComplianceWithWaterRegulationsControl = this.isComplianceWithWaterRegulationsControl,
            isSafetyEquipmentAndStandardsComplianceControl = this.isSafetyEquipmentAndStandardsComplianceControl,
            isSeafarersControl = this.isSeafarersControl,
            observationsByUnit = this.observationsByUnit,
            speciesQuantitySeized = this.speciesQuantitySeized
        )
    }
}
