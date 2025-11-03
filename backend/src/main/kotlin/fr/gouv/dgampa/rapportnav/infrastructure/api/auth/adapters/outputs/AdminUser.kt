package fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import java.time.Instant

data class AdminUser(
    val id: Int? = null,
    var serviceId: Int? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    var roles: List<RoleTypeEnum>,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
) {
    companion object {
        fun fromUserEntity(user: User) = AdminUser(
            id = user.id,
            serviceId = user.serviceId,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            roles = user.roles
        )
    }
}

