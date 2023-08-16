package fr.gouv.dgampa.rapportnav.infrastructure.database.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.user.UserEntity
import jakarta.persistence.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Entity
@Table(name = "user", schema = "public")
data class UserModel(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", allocationSize = 1)
    var id: Int? = 0,

    @Column(name = "name")
    var name: String = "",

    @Column(name = "email", unique = true)
    var email: String = "",

    @JsonIgnore
    @Column(name = "password")
    var password: String = "",
) {


    fun toUserEntity(mapper: ObjectMapper): UserEntity = UserEntity(
        id = id,
        email = email,
        name = name,
        password = password,
    )

    companion object {
        fun fromUserEntity(user: UserEntity, mapper: ObjectMapper) = UserModel(
            id = user.id,
            name = user.name,
            email = user.email,
            password = user.password
//            password = BCryptPasswordEncoder().encode(user.password),
        )
    }
}
