package fr.gouv.dgampa.rapportnav.infrastructure.database.model.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType

@Entity
@Table(name = "user", schema = "public")
class UserModel(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", allocationSize = 1)
    var id: Int? = 0,

    @Column(name = "first_name")
    var firstName: String = "",

    @Column(name = "last_name")
    var lastName: String = "",

    @Column(name = "email", unique = true)
    var email: String = "",

    @JsonIgnore
    @Column(name = "password")
    var password: String = "",

    @Column(name = "service_id")
    var serviceId: Int? = null,

    @ElementCollection(targetClass = RoleTypeEnum::class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "role")
    @Enumerated(EnumType.STRING)  // Still required for proper enum handling
    @JdbcType(PostgreSQLEnumJdbcType::class)
    var roles: List<RoleTypeEnum> = listOf()
) {


    fun toUser(mapper: ObjectMapper): User = User(
        id = id,
        serviceId = serviceId,
        email = email,
        firstName = firstName,
        lastName = lastName,
        password = password,
        roles = roles
    )

    companion object {
        fun fromUser(user: User, mapper: ObjectMapper) = UserModel(
            id = user.id,
            serviceId = user.serviceId,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            password = user.password,
            roles = user.roles
        )
    }
}
