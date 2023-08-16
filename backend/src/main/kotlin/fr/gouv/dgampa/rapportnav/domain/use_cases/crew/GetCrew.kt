package fr.gouv.dgampa.rapportnav.domain.use_cases.crew // ktlint-disable package-name

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.crew.CrewEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.ICrewRepository
import org.slf4j.LoggerFactory

@UseCase
class GetCrew(private val crewRepository: ICrewRepository) {
    private val logger = LoggerFactory.getLogger(GetCrew::class.java)

    fun execute(): List<CrewEntity> {
        val crew = crewRepository.findAllCrew()

        logger.info("Found ${crew.size} crew ")

        return crew
    }
}
