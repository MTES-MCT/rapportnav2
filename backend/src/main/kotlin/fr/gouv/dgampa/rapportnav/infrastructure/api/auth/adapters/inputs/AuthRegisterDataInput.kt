package fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs

import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum

data class AuthRegisterDataInput(
    val id: Int?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val serviceId: Int? = null,
    val roles: List<RoleTypeEnum>?
)
