package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.input

import java.util.UUID

data class AdminGeneralInfosUpdateServiceInput(
    val id: Int? = null,
)

data class AdminGeneralInfosUpdateInput(
    val id: Int? = null,
    val missionId: Int? = null,
    val missionIdUUID: UUID? = null,
    val service: AdminGeneralInfosUpdateServiceInput? = null,
)
