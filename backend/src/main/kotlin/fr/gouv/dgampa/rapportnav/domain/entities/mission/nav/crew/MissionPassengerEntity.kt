package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.domain.utils.MissionDateUtils
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.passenger.MissionPassengerModel
import java.time.Instant
import java.time.LocalDate
import java.util.*

data class MissionPassengerEntity(
    val id: Int? = null,
    val missionId: Int? = null,
    val missionIdUUID: UUID? = null,
    val fullName: String,
    val organization: MissionPassengerOrganization? = null,
    val isIntern: Boolean? = null,
    val startDate: LocalDate,
    val endDate: LocalDate,
){

    /**
     * Validates that passenger dates fall within the mission date range.
     * Mission dates are provided as Instant and converted to LocalDate for comparison.
     */
    fun isWithinMissionDates(missionStart: Instant?, missionEnd: Instant?): Boolean {
        return MissionDateUtils.isWithinMissionDates(
            startDate = startDate,
            endDate = endDate,
            missionStart = missionStart,
            missionEnd = missionEnd
        )
    }

    fun toMissionPassengerModel(): MissionPassengerModel {
        return MissionPassengerModel(
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
        fun fromMissionPassengerModel(passenger: MissionPassengerModel): MissionPassengerEntity {
            return MissionPassengerEntity(
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
