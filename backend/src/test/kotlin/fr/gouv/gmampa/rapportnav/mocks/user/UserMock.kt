package fr.gouv.gmampa.rapportnav.mocks.user

import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import java.time.Instant

object UserMock {
    fun create(
        id: Int? = 1,
        serviceId: Int? = null,
        firstName: String = "first_name",
        lastName: String = "last_name",
        email: String = "email@email.com",
        password: String = "password",
        token: String? = null,
        roles: List<RoleTypeEnum> = listOf(RoleTypeEnum.USER_ULAM),
        disabledAt: Instant? = null
    ): User {
        return User(
            id = id,
            serviceId = serviceId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            token = token,
            roles = roles,
            disabledAt = disabledAt
        )
    }
}
