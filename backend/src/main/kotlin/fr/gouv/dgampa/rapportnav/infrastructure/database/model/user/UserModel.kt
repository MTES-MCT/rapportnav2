package fr.gouv.dgampa.rapportnav.infrastructure.database.model.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import jakarta.persistence.*

@Entity
@Table(name = "user", schema = "public")
data class UserModel(
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

    @Column(name = "agent_id")
    var agentId: Int? = 0,
) {


    fun toUser(mapper: ObjectMapper): User = User(
        id = id,
        agentId = agentId,
        email = email,
        firstName = firstName,
        lastName = lastName,
        password = password,
    )

    companion object {
        fun fromUser(user: User, mapper: ObjectMapper) = UserModel(
            id = user.id,
            agentId = user.agentId,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            password = user.password,
        )
    }
}
