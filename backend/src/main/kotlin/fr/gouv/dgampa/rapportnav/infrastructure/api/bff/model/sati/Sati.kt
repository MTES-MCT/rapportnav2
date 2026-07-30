package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlResource
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import java.time.Instant
import java.util.*

data class Sati(
    var id: UUID? = null,
    var actionId: String,
    var module: SatiModuleType,
    var resource: ControlResource? = null,
    var vessel: SatiVessel? = null,
    var startDatetimeUtc: Instant? = null,
    var endDatetimeUtc: Instant? = null,
    var principalInspector: SatiInspector? = null,
    var otherInspectors: List<SatiInspector>? = emptyList(),
)
