package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.passenger.MissionPassengerModel
import java.time.Instant
import java.util.*

data class MissionPassengerEntity(
    val id: Int? = null,
    val missionId: Int? = null,
    val missionIdUUID: UUID? = null,
    val fullName: String,
    val organization: String? = null,
    val isIntern: Boolean? = null,
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant,
){

    fun toMissionPassengerModel(): MissionPassengerModel {
        return MissionPassengerModel(
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

    companion object {
        fun fromMissionPassengerModel(passenger: MissionPassengerModel): MissionPassengerEntity {
            return MissionPassengerEntity(
                id = passenger.id,
                missionId = passenger.missionId,
                missionIdUUID = passenger.missionIdUUID,
                fullName = passenger.fullName,
                organization = passenger.organization,
                isIntern = passenger.isIntern,
                startDateTimeUtc = passenger.startDateTimeUtc,
                endDateTimeUtc = passenger.endDateTimeUtc,
            )
        }
    }
}
