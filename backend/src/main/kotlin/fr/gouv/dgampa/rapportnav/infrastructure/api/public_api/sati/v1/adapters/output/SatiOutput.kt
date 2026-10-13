package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.sati.v1.adapters.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlResource
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiStatusType
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.SatiInspector
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.SatiVessel
import java.time.Instant
import java.util.*

data class SatiOutput(
    val id: UUID,
    val version: Int,
    val status: SatiStatusType,
    var actionId: String,
    var module: SatiModuleType,
    var resource: ControlResource? = null,
    var vessel: SatiVessel? = null,
    var startDatetimeUtc: Instant? = null,
    var endDatetimeUtc: Instant? = null,
    var principalInspector: SatiInspector? = null,
    var otherInspectors: List<SatiInspector>? = emptyList(),
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val updatedBy: Int?
)
