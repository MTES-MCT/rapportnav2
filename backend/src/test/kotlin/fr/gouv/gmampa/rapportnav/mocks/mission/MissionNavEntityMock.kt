package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import java.time.Instant
import java.util.UUID

object MissionNavEntityMock {
    fun create(
        id: UUID = UUID.randomUUID(),
        serviceId: Int = 1,
        openBy: String? = null,
        completedBy: String? = null,
        startDateTimeUtc: Instant = Instant.now(),
        endDateTimeUtc: Instant? = null,
        isDeleted: Boolean = false,
        missionSource: MissionSourceEnum? = MissionSourceEnum.RAPPORT_NAV,
        observationsByUnit: String? = null
    ): MissionNavEntity {
        return MissionNavEntity(
            id = id,
            serviceId = serviceId,
            openBy = openBy,
            completedBy = completedBy,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isDeleted = isDeleted,
            missionSource = missionSource,
            observationsByUnit = observationsByUnit
        )
    }
}
