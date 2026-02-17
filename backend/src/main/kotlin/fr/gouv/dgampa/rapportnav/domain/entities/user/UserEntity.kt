package fr.gouv.dgampa.rapportnav.domain.entities.user

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.UserModel
import java.time.Instant

data class User(
    val id: Int? = null,
    var serviceId: Int? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    var token: String? = null,
    var roles: List<RoleTypeEnum>,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val disabledAt: Instant? = null,
) {
    constructor(user: User) : this(
        id = user.id,
        serviceId = user.serviceId,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email,
        password = user.password,
        token = user.token,
        roles = user.roles,
        disabledAt = user.disabledAt,
    )
    companion object {
        fun fromUserModel(user: UserModel) = User(
            id = user.id,
            serviceId = user.serviceId,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            password = user.password,
            roles = user.roles,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
            disabledAt = user.disabledAt,
        )
    }
}

var User.token: String?
    get() = this.token
    set(value) {}


