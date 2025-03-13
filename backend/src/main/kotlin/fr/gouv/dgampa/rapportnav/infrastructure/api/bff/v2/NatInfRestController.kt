package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetNatinfs
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.Natinf
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/natinfs")
class NatInfRestController(
    private val getNatinfs: GetNatinfs
) {
    private val logger = LoggerFactory.getLogger(NatInfRestController::class.java)

    @GetMapping
    fun getNatinfs(): List<Natinf>? {
        return try {
            getNatinfs.execute().map { Natinf.fromNatinfEntity(it) }
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint getNatinfs:", e)
            null
        }
    }
}

