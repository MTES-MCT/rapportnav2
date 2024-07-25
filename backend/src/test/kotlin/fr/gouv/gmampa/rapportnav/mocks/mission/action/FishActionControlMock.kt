package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

object FishActionControlMock {
    fun create(
        id: Int = UUID.randomUUID().hashCode(),
        missionId: Int = 1,
        actionDatetimeUtc: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 0), ZoneOffset.UTC),
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
    ): MissionAction {
        return MissionAction(
            id = id,
            missionId = missionId,
            actionDatetimeUtc = actionDatetimeUtc,
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
            completion = completion
        )
    }
}
