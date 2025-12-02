package fr.gouv.gmampa.rapportnav.mocks.mission.passenger

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerOrganization
import java.time.Instant
import java.util.UUID

object MissionPassengerEntityMock {
    fun create(
        id: Int? = null,
        missionId: Int? = null,
        missionIdUUID: UUID? = null,
        fullName: String = "Bob Random",
        organization: MissionPassengerOrganization? = null,
        isIntern: Boolean? = null,
        startDateTimeUtc: Instant = Instant.parse("2022-01-02T12:00:01Z"),
        endDateTimeUtc: Instant = Instant.parse("2022-01-03T12:00:01Z"),
    ): MissionPassengerEntity {
        return MissionPassengerEntity(
            id = id,
            missionId = missionId,
            missionIdUUID = missionIdUUID,
            fullName = fullName,
            organization = organization,
            isIntern = isIntern,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
        )
    }
}
