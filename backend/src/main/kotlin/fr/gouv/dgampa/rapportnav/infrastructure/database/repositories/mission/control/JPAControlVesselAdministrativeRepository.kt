package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlVesselAdministrative
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlVesselAdministrativeRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlVesselAdministrativeRepository
import org.springframework.stereotype.Repository


@Repository
class JPAControlVesselAdministrativeRepository(
    private val dbControlVesselAdministrativeRepository: IDBControlVesselAdministrativeRepository,
    private val mapper: ObjectMapper,
) : IControlVesselAdministrativeRepository {

    override fun findAllByMissionId(missionId: Int): List<ControlVesselAdministrative> {
        // TODO call correct function filtering by mission id
        TODO("Not yet implemented")
//        return dbControlVesselAdministrativeRepository.findAll().map { it.toControlVesselAdministrative() }

    }

}
