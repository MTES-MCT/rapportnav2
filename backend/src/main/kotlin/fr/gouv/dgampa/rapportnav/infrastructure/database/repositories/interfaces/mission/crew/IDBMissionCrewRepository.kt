package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBMissionCrewRepository : JpaRepository<MissionCrewModel, Int> {
    fun findByMissionId(missionId: Int): List<MissionCrewModel>

    fun findByMissionIdString(missionId: String): List<MissionCrewModel>
}
