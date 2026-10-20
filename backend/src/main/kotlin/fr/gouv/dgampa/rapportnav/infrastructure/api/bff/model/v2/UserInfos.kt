package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum

data class UserInfos(
    val id: Int,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val serviceId: Int? = null,
    val serviceName: String? = null,
    val controlUnitId: Int? = null,
    val roles: List<RoleTypeEnum> = emptyList()
)
