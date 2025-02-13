package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitResourceType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetControlUnitResources
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnit
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.StationData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v2/resources")
class ControlUnitResourceRestController(
    private val getControlUnitResources: GetControlUnitResources
) {
    @GetMapping
    fun getAll(): List<ControlUnitResourceEnv>? {
        return getControlUnitResources.execute()
    }
}
