package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.rnip.IRnipInfractionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs.RnipInfractionInput
import fr.gouv.dgampa.rapportnav.infrastructure.rnip.outputs.RnipInfractionResultOutput
import org.slf4j.LoggerFactory

@UseCase
class SendRnipInfractions(private val rnipInfractionRepository: IRnipInfractionRepository) {

    private val logger = LoggerFactory.getLogger(SendRnipInfractions::class.java)

    fun execute(infractions: List<RnipInfractionInput>): List<RnipInfractionResultOutput> {
        val results = rnipInfractionRepository.send(infractions)
        logger.info("Sent ${infractions.size} infractions to RNIP, received ${results.size} results.")
        return results
    }
}
