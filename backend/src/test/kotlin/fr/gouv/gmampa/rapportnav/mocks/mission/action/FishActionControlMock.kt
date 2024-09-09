package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*
import com.neovisionaries.i18n.CountryCode

object FishActionControlMock {
    fun create(
        id: Int = UUID.randomUUID().hashCode(),
        missionId: Int = 1,
        actionDatetimeUtc: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 0), ZoneOffset.UTC),
        actionEndDatetimeUtc: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 13, 0), ZoneOffset.UTC),
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
        )
    }
}
