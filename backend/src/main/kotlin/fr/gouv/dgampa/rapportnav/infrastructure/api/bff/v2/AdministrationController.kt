package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations.GetAdministrationById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations.GetAdministrations
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.FullAdministration
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/administrations")
class AdministrationController(
    private val getAdministrations: GetAdministrations,
    private val getAdministrationById: GetAdministrationById
) {

    private val logger = LoggerFactory.getLogger(AdministrationController::class.java)

    @GetMapping("")
    fun getAll(): List<FullAdministration>? {
        try {
            return getAdministrations.execute()?: listOf()
        } catch (e: Exception) {
            logger.error("Error while fetching administrations : ${e.message}")
            return listOf()
        }
    }

    @GetMapping("{administrationId}")
    fun get(@PathVariable administrationId: Int): FullAdministration? {
        try {
            return getAdministrationById.execute(administrationId)
        } catch (e: Exception) {
            logger.error("Error while fetching administration with id $administrationId : ${e.message}")
            return null
        }
    }
}
