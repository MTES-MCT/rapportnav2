package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import java.time.ZonedDateTime
import java.util.*

object FishActionControlMock {
    fun create(
        missionId: Int,
        actionDatetimeUtc: ZonedDateTime,
        actionType: MissionActionType = MissionActionType.SEA_CONTROL,
        isDeleted: Boolean = false,
        hasSomeGearsSeized: Boolean = false,
        hasSomeSpeciesSeized: Boolean = false,
    ): MissionAction {
        return MissionAction(
            id = UUID.randomUUID().hashCode(),
            missionId = missionId,
            actionDatetimeUtc = actionDatetimeUtc,
            actionType = actionType,
            isDeleted = isDeleted,
            hasSomeGearsSeized = hasSomeGearsSeized,
            hasSomeSpeciesSeized = hasSomeSpeciesSeized
            // Set other properties to their default values or mocks as needed
        )
    }
}
