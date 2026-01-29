package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.IEnvAdministrationRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.FullAdministration
import org.slf4j.LoggerFactory

@UseCase
class GetAdministrations(private val administrationRepository: IEnvAdministrationRepository) {

    private val logger = LoggerFactory.getLogger(GetAdministrations::class.java)

    fun execute(): List<FullAdministration> {
        val dataOutputs = administrationRepository.findAll()
        logger.info("Found ${dataOutputs.size} administrations from env.")

        return dataOutputs.map { FullAdministration.fromFullAdministrationDataOutput(it) }
    }
}
