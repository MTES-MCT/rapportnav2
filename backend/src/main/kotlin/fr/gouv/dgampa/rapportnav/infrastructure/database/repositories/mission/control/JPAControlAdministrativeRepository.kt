package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlAdministrativeRepository
import org.springframework.stereotype.Repository


@Repository
class JPAControlAdministrativeRepository(
    private val dbControlVesselAdministrativeRepository: IDBControlAdministrativeRepository,
    private val mapper: ObjectMapper,
) : IControlAdministrativeRepository {

    override fun findAllByMissionId(missionId: Int): List<ControlAdministrative> {
        // TODO call correct function filtering by mission id
        TODO("Not yet implemented")
//        return dbControlVesselAdministrativeRepository.findAll().map { it.toControlVesselAdministrative() }

    }

}
