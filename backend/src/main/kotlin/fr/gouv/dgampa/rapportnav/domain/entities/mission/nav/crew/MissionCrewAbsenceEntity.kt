package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewAbsenceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewModel
import java.time.Instant
import java.time.LocalDate


data class MissionCrewAbsenceEntity(
    val id: Int? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val isAbsentFullMission: Boolean? = null,
    val reason: String? = null,
) {

    fun toMissionCrewAbsenceModel(crew: MissionCrewModel): MissionCrewAbsenceModel {
        return MissionCrewAbsenceModel(
            id = id,
            startDate = startDate,
            endDate = endDate,
            isAbsentFullMission = isAbsentFullMission,
            reason = reason,
            missionCrew = crew
        )
    }

    companion object {
        fun fromMissionCrewAbsenceModel(model: MissionCrewAbsenceModel): MissionCrewAbsenceEntity {
            return MissionCrewAbsenceEntity(
                id = model.id,
                startDate = model.startDate,
                endDate = model.endDate,
                isAbsentFullMission = model.isAbsentFullMission,
                reason = model.reason,
            )
        }
    }
}

