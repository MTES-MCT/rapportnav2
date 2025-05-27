package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetCrossControlByServiceId
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.CrossControl
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/cross_controls")
class CrossControlRestController(
    private val getUserFromToken: GetUserFromToken,
    private val getCrossControlByServiceId: GetCrossControlByServiceId,
) {

    private val logger = LoggerFactory.getLogger(CrossControlRestController::class.java)

    @GetMapping
    fun getCrossControls(): List<CrossControl>? {
        return try {
            val user = getUserFromToken.execute()
            getCrossControlByServiceId.execute(serviceId = user?.serviceId)
                .sortedBy { it.startDateTimeUtc }
                .map { CrossControl.fromCrossControlEntity(it) }
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint getCrossControlByServiceId:", e)
            null
        }
    }
}
