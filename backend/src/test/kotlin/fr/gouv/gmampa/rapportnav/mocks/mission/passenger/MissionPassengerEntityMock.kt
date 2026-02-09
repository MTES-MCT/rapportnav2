package fr.gouv.gmampa.rapportnav.mocks.mission.passenger

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.PassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.PassengerOrganization
import java.time.LocalDate
import java.util.UUID

object MissionPassengerEntityMock {
    fun create(
        id: Int? = null,
        missionId: Int? = null,
        missionIdUUID: UUID? = null,
        fullName: String = "Bob Random",
        organization: PassengerOrganization? = null,
        isIntern: Boolean? = null,
        startDate: LocalDate = LocalDate.parse("2022-01-02"),
        endDate: LocalDate = LocalDate.parse("2022-01-03"),
    ): PassengerEntity {
        return PassengerEntity(
            id = id,
            missionId = missionId,
            missionIdUUID = missionIdUUID,
            fullName = fullName,
            organization = organization,
            isIntern = isIntern,
            startDate = startDate,
            endDate = endDate,
        )
    }
}
