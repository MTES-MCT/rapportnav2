package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetControlUnitResources
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v2/resources")
class ControlUnitResourceRestController(
    private val getControlUnitResources: GetControlUnitResources
) {
    private val logger = LoggerFactory.getLogger(ControlUnitResourceRestController::class.java)
    @GetMapping
    fun getAll(): List<ControlUnitResourceEnv>? {
        try {
            return getControlUnitResources.execute()?: listOf()
        } catch (e: Exception) {
            logger.error("Error while fetching Control unit : ${e.message}")
            return listOf()
        }
    }

}
