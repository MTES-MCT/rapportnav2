package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.sati.v1.adapters.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiStatusType
import java.time.Instant
import java.util.*

data class SatiListOutput(
    val id: UUID,
    val version: Int,
    var actionId: String,
    var module: SatiModuleType,
    val status: SatiStatusType,
    var startDatetimeUtc: Instant? = null,
    var endDatetimeUtc: Instant? = null,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val updatedBy: Int?
)
