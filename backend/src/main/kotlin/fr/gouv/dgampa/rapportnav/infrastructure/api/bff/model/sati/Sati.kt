package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlResource
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.SatiInspector
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.SatiVessel
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
    var inspectors: List<SatiInspector>? = emptyList(),
)
