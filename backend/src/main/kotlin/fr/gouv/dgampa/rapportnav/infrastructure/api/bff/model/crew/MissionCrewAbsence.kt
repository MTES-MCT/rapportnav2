package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewAbsenceEntity
import java.time.LocalDate

data class MissionCrewAbsence(
    val id: Int? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val isAbsentFullMission: Boolean? = null,
    val reason: String? = null,
) {

    companion object {
        fun fromMissionCrewAbsenceEntity(crew: MissionCrewAbsenceEntity): MissionCrewAbsence {
            return MissionCrewAbsence(
                id = crew.id,
                startDate = crew.startDate,
                endDate = crew.endDate,
                isAbsentFullMission = crew.isAbsentFullMission,
                reason = crew.reason,
            )
        }
    }

    fun toMissionCrewAbsenceEntity(): MissionCrewAbsenceEntity {
        return MissionCrewAbsenceEntity(
            id = if (id == 0 || id == null) null else id,
            startDate = startDate,
            endDate = endDate,
            isAbsentFullMission = isAbsentFullMission,
            reason = reason,
        )
    }
}
