package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import com.fasterxml.jackson.databind.ObjectMapper

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.CrewEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.ICrewRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBCrewRepository
import org.springframework.stereotype.Repository


@Repository
class JpaCrewRepository(
    private val dbCrewRepository: IDBCrewRepository,
    private val mapper: ObjectMapper,
) : ICrewRepository {

    override fun findAllCrew(): List<CrewEntity> {
        println(message = "FDSFSDFDSFSD")
        return dbCrewRepository.findAll().map { it.toCrewEntity(mapper) }

    }

}
