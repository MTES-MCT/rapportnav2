package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerOrganization
import java.time.LocalDate
import java.util.UUID

data class MissionPassenger(
    val id: Int? = null,
    val missionId: Int? = null,
    val missionIdUUID: UUID? = null,
    val fullName: String,
    val organization: MissionPassengerOrganization? = null,
    val isIntern: Boolean? = null,
    val startDate: LocalDate,
    val endDate: LocalDate,
) {

    companion object {
        fun fromMissionPassengerEntity(passenger: MissionPassengerEntity): MissionPassenger {
            return MissionPassenger(
                id = passenger.id,
                missionId = passenger.missionId,
                missionIdUUID = passenger.missionIdUUID,
                fullName = passenger.fullName,
                organization = passenger.organization,
                isIntern = passenger.isIntern,
                startDate = passenger.startDate,
                endDate = passenger.endDate,
            )
        }
    }

    fun toMissionPassengerEntity(missionIdUUID: UUID?= null, missionId: Int? = null): MissionPassengerEntity {
        return MissionPassengerEntity(
            id = if (id == 0 || id == null) null else id,
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
