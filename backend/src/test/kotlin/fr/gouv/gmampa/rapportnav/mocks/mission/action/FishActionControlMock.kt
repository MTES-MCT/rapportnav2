package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.SpeciesControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.SpeciesInfraction
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

object FishActionControlMock {
    fun create(
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
    ): MissionAction {
        return MissionAction(
            id = UUID.randomUUID().hashCode(),
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
            speciesInfractions = speciesInfractions
        )
    }
}
