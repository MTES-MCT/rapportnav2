package fr.gouv.gmampa.rapportnav.mocks.mission.action

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import java.time.Instant
import java.util.*

object FishActionControlMock {
    fun create(
        id: Int = UUID.randomUUID().hashCode(),
        missionId: Int = 1,
        actionDatetimeUtc: Instant = Instant.parse("2022-01-02T12:00:01Z"),
        actionEndDatetimeUtc: Instant? = Instant.parse("2022-01-02T13:00:01Z"),
        actionType: MissionActionType = MissionActionType.SEA_CONTROL,
        isDeleted: Boolean = false,
        hasSomeGearsSeized: Boolean = false,
        hasSomeSpeciesSeized: Boolean = false,
        portLocode: String? = "LR",
        portName: String? = "La Rochelle",
        latitude: Double? = 52.14,
        longitude: Double? = 14.3,
        vesselId: Int? = 314,
        vesselName: String? = "Le Pi",
        speciesOnboard: List<SpeciesControl> = listOf(),
        speciesInfractions: List<SpeciesInfraction> = listOf(),
        completion: Completion = Completion.COMPLETED,
        gearInfractions: List<GearInfraction> = listOf(),
        logbookInfractions: List<LogbookInfraction> = listOf(),
        otherInfractions: List<OtherInfraction> = listOf(),
        userTrigram: String = "TKT",
        flagState: CountryCode = CountryCode.FR,
        seizureAndDiversion: Boolean? = null,
        speciesQuantitySeized: Int? = null,
        isAdministrativeControl: Boolean? = null,
        isComplianceWithWaterRegulationsControl: Boolean? = null,
        isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
        isSeafarersControl: Boolean? = null,
        externalReferenceNumber: String? = "AC 1435",
    ): MissionAction {
        return MissionAction(
            id = id,
            missionId = missionId,
            actionDatetimeUtc = actionDatetimeUtc,
            actionEndDatetimeUtc = actionEndDatetimeUtc,
            actionType = actionType,
            portLocode = portLocode,
            portName = portName,
            isDeleted = isDeleted,
            hasSomeGearsSeized = hasSomeGearsSeized,
            hasSomeSpeciesSeized = hasSomeSpeciesSeized,
            latitude = latitude,
            longitude = longitude,
            vesselId = vesselId,
            vesselName = vesselName,
            speciesOnboard = speciesOnboard,
            speciesInfractions = speciesInfractions,
            completion = completion,
            gearInfractions = gearInfractions,
            logbookInfractions = logbookInfractions,
            otherInfractions = otherInfractions,
            flagState = flagState,
            userTrigram = userTrigram,
            isFromPoseidon = false,
            seizureAndDiversion = seizureAndDiversion,
            speciesQuantitySeized = speciesQuantitySeized,
            isSeafarersControl = isSeafarersControl,
            isAdministrativeControl = isAdministrativeControl,
            isComplianceWithWaterRegulationsControl = isComplianceWithWaterRegulationsControl,
            isSafetyEquipmentAndStandardsComplianceControl = isSafetyEquipmentAndStandardsComplianceControl,
            externalReferenceNumber = externalReferenceNumber,
            )
    }
}
