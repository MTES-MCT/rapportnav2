package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories

import com.fasterxml.jackson.databind.ObjectMapper

import fr.gouv.dgampa.rapportnav.domain.entities.crew.CrewEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.ICrewRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.IDBCrewRepository
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
