package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlAdministrativeModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBControlAdministrativeRepository: JpaRepository<ControlAdministrativeModel, Int> {
    fun findAllByMissionId(missionId: Int): List<ControlAdministrativeModel>

    fun save(control: ControlAdministrative): ControlAdministrative
}
