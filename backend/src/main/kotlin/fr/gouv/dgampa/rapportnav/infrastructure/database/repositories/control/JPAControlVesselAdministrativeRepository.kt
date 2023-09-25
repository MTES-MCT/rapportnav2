package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.control.ControlVesselAdministrative
import fr.gouv.dgampa.rapportnav.domain.repositories.control.IControlVesselAdministrativeRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.control.IDBControlVesselAdministrativeRepository
import org.springframework.stereotype.Repository


@Repository
class JPAControlVesselAdministrativeRepository(
    private val dbControlVesselAdministrativeRepository: IDBControlVesselAdministrativeRepository,
    private val mapper: ObjectMapper,
) : IControlVesselAdministrativeRepository {

    override fun findAllByMissionId(missionId: Int): List<ControlVesselAdministrative> {
        // TODO call correct function filtering by mission id
        return dbControlVesselAdministrativeRepository.findAll().map { it.toControlVesselAdministrative() }

    }

}
