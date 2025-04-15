package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import java.time.Instant

data class MissionNavInput(
    val id: Int? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val isDeleted: Boolean? = false,
    val observationsByUnit: String? = null,
) {
    companion object {
        fun fromMissionEntity(entity: MissionEntity): MissionNavInput {
            return MissionNavInput(
                id = entity.id,
                startDateTimeUtc = entity.startDateTimeUtc,
                endDateTimeUtc = entity.endDateTimeUtc,
                isDeleted = entity.isDeleted,
                observationsByUnit = entity.observationsByUnit
            )
        }
    }

    fun toMissionNavEntity(missionFromDb: MissionEntity): MissionNavEntity {
        return MissionNavEntity(
            id = missionFromDb.id,
            controlUnits = missionFromDb.controlUnits.map { it.id },
            openBy = missionFromDb.openBy,
            completedBy = missionFromDb.completedBy,
            startDateTimeUtc = startDateTimeUtc ?: missionFromDb.startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isDeleted = isDeleted ?: missionFromDb.isDeleted,
            missionSource = missionFromDb.missionSource,
            observationsByUnit = observationsByUnit,
            controlUnitIdOwner = missionFromDb.controlUnitIdOwner!!
        )
    }
}
