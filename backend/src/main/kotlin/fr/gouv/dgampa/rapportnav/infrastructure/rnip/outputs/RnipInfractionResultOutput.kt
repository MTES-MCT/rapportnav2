package fr.gouv.dgampa.rapportnav.infrastructure.rnip.outputs

import java.util.UUID

data class RnipInfractionResultOutput(
    val id: UUID,
    val success: Boolean,
    val status: RnipLineStatus,
    val message: String? = null
)
