package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.IEnvAdministrationRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.FullAdministration
import org.slf4j.LoggerFactory

@UseCase
class GetAdministrationById(
    private val administrationRepository: IEnvAdministrationRepository
) {

    private val logger = LoggerFactory.getLogger(GetAdministrationById::class.java)

    fun execute(administrationId: Int): FullAdministration? {
        val output = administrationRepository.findById(administrationId)

        return if (output != null) {
            FullAdministration.fromFullAdministrationDataOutput(output)
        } else {
            logger.info("Administration by id : $administrationId return empty result")
            null
        }
    }
}
