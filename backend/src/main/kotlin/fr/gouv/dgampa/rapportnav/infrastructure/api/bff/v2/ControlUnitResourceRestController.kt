package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetControlUnitResources
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v2/resources")
class ControlUnitResourceRestController(
    private val getControlUnitResources: GetControlUnitResources
) {
    fun getAll(): List<ControlUnitResourceEnv>? {
        return getControlUnitResources.execute()
    }
}
