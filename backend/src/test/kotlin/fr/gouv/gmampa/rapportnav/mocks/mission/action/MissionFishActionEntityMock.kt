package fr.gouv.gmampa.rapportnav.mocks.mission.action

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTargetTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.tags.TagEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes.ThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlUnit
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.Completion
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.ControlCheck
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FleetSegment
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FlightGoal
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.GearControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.GearInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.LogbookInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.OtherInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.SpeciesControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.SpeciesInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity2
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.FormattedEnvActionControlPlan
import org.locationtech.jts.geom.Geometry
import java.time.Instant
import java.util.UUID

object MissionFishActionEntityMock {
    fun create(
        id: Int? = 1,
        missionId: Int = 1,
        vesselId: Int? = null,
        vesselName: String? = null,
        internalReferenceNumber: String? = null,
        externalReferenceNumber: String? = null,
        ircs: String? = null,
        flagState: CountryCode? = null,
        districtCode: String? = null,
        faoAreas: List<String>? = null,
        fishActionType: MissionActionType = MissionActionType.SEA_CONTROL,
        actionDatetimeUtc: Instant? = null,
        actionEndDatetimeUtc: Instant? = null,
        emitsVms: ControlCheck? = null,
        emitsAis: ControlCheck? = null,
        flightGoals: List<FlightGoal>? = listOf(),
        logbookMatchesActivity: ControlCheck? = null,
        licencesMatchActivity: ControlCheck? = null,
        speciesWeightControlled: Boolean? = null,
        speciesSizeControlled: Boolean? = null,
        separateStowageOfPreservedSpecies: ControlCheck? = null,
        logbookInfractions: List<LogbookInfraction>? = listOf(),
        licencesAndLogbookObservations: String? = null,
        gearInfractions: List<GearInfraction>? = listOf(),
        speciesInfractions: List<SpeciesInfraction>? = listOf(),
        speciesObservations: String? = null,
        seizureAndDiversion: Boolean? = null,
        otherInfractions: List<OtherInfraction>? = listOf(),
        numberOfVesselsFlownOver: Int? = null,
        unitWithoutOmegaGauge: Boolean? = null,
        controlQualityComments: String? = null,
        feedbackSheetRequired: Boolean? = null,
        userTrigram: String? = null,
        segments: List<FleetSegment>? = listOf(),
        facade: String? = null,
        longitude: Double? = null,
        latitude: Double? = null,
        portLocode: String? = null,
        portName: String? = null,
        vesselTargeted: ControlCheck? = null,
        seizureAndDiversionComments: String? = null,
        otherComments: String? = null,
        gearOnboard: List<GearControl>? = listOf(),
        speciesOnboard: List<SpeciesControl>? = listOf(),
        isFromPoseidon: Boolean? = null,
        controlUnits: List<ControlUnit>? = listOf(),
        isDeleted: Boolean? = null,
        hasSomeGearsSeized: Boolean? = null,
        hasSomeSpeciesSeized: Boolean? = null,
        completedBy: String? = null,
        completion: Completion? = null,
        isAdministrativeControl: Boolean? = null,
        isComplianceWithWaterRegulationsControl: Boolean? = null,
        isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
        isSeafarersControl: Boolean? = null,
        observationsByUnit: String? = null,
        speciesQuantitySeized: Int? = null,
        targets: List<TargetEntity2>? = null
    ): MissionFishActionEntity {
        return MissionFishActionEntity(
            id = id,
            missionId = missionId,
            vesselId = vesselId,
            vesselName = vesselName,
            internalReferenceNumber = internalReferenceNumber,
            externalReferenceNumber = externalReferenceNumber,
            ircs = ircs,
            flagState = flagState,
            districtCode = districtCode,
            faoAreas = faoAreas,
            actionDatetimeUtc = actionDatetimeUtc,
            actionEndDatetimeUtc = actionEndDatetimeUtc,
            emitsVms = emitsVms,
            emitsAis = emitsAis,
            flightGoals = flightGoals,
            logbookMatchesActivity = logbookMatchesActivity,
            licencesMatchActivity = licencesMatchActivity,
            speciesWeightControlled = speciesWeightControlled,
            speciesSizeControlled = speciesSizeControlled,
            seizureAndDiversionComments = seizureAndDiversionComments,
            otherInfractions = otherInfractions,
            numberOfVesselsFlownOver = numberOfVesselsFlownOver,
            fishActionType = fishActionType,
            logbookInfractions = logbookInfractions,
            licencesAndLogbookObservations = licencesAndLogbookObservations,
            gearInfractions = gearInfractions,
            speciesInfractions = speciesInfractions,
            speciesObservations = speciesObservations,
            seizureAndDiversion = seizureAndDiversion,
            separateStowageOfPreservedSpecies = separateStowageOfPreservedSpecies,
            unitWithoutOmegaGauge = unitWithoutOmegaGauge,
            controlQualityComments = controlQualityComments,
            feedbackSheetRequired = feedbackSheetRequired,
            userTrigram = userTrigram,
            segments = segments,
            facade = facade,
            longitude = longitude,
            latitude = latitude,
            portLocode = portLocode,
            portName = portName,
            vesselTargeted = vesselTargeted,
            otherComments = otherComments,
            gearOnboard = gearOnboard,
            speciesOnboard = speciesOnboard,
            isFromPoseidon = isFromPoseidon,
            controlUnits = controlUnits,
            isDeleted = isDeleted,
            hasSomeGearsSeized = hasSomeGearsSeized,
            hasSomeSpeciesSeized = hasSomeSpeciesSeized,
            completedBy = completedBy,
            completion = completion,
            isAdministrativeControl = isAdministrativeControl,
            isComplianceWithWaterRegulationsControl = isComplianceWithWaterRegulationsControl,
            isSafetyEquipmentAndStandardsComplianceControl = isSafetyEquipmentAndStandardsComplianceControl,
            isSeafarersControl = isSeafarersControl,
            observationsByUnit = observationsByUnit,
            speciesQuantitySeized = speciesQuantitySeized,
            targets = targets,
        )

    }
}
