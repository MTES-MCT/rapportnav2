package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlVesselAdministrativeModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBControlVesselAdministrativeRepository: JpaRepository<ControlVesselAdministrativeModel, Int> {
    fun findAllByMissionId(missionId: Int): List<ControlVesselAdministrativeModel>
}
