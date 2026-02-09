package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.passenger.PassengerModel
import java.time.LocalDate
import java.util.*

data class PassengerEntity(
    val id: Int? = null,
    val missionId: Int? = null,
    val missionIdUUID: UUID? = null,
    val fullName: String,
    val organization: PassengerOrganization? = null,
    val isIntern: Boolean? = null,
    val startDate: LocalDate,
    val endDate: LocalDate,
){

    fun toPassengerModel(): PassengerModel {
        return PassengerModel(
            id = id,
            missionId = missionId,
            missionIdUUID = missionIdUUID,
            fullName = fullName,
            organization = organization.toString(),
            isIntern = isIntern,
            startDate = startDate,
            endDate = endDate,
        )
    }

    companion object {
        fun fromPassengerModel(passenger: PassengerModel): PassengerEntity {
            return PassengerEntity(
                id = passenger.id,
                missionId = passenger.missionId,
                missionIdUUID = passenger.missionIdUUID,
                fullName = passenger.fullName,
                organization = mapStringToMissionPassengerOrganization(passenger.organization),
                isIntern = passenger.isIntern,
                startDate = passenger.startDate,
                endDate = passenger.endDate,
            )
        }
    }
}
