package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewAbsenceModel
import java.time.Instant


data class MissionCrewAbsenceEntity(
    val id: Int? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val isAbsentFullMission: Boolean? = null,
    val reason: String? = null,
) {

    fun toMissionCrewAbsenceModel(): MissionCrewAbsenceModel {
        return MissionCrewAbsenceModel(
            id = id,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isAbsentFullMission = isAbsentFullMission,
            reason = reason
        )
    }

    companion object {
        fun fromMissionCrewAbsenceModel(model: MissionCrewAbsenceModel): MissionCrewAbsenceEntity {
            return MissionCrewAbsenceEntity(
                id = model.id,
                startDateTimeUtc = model.startDateTimeUtc,
                endDateTimeUtc = model.endDateTimeUtc,
                isAbsentFullMission = model.isAbsentFullMission,
                reason = model.reason,
            )
        }
    }
}

