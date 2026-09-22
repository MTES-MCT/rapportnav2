package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.sati.v1.adapters.input

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiStatusType
import jakarta.validation.constraints.NotNull

data class SatiStatusPatchInput(
    @field:NotNull(message = "status is required")
    val status: SatiStatusType?
)
