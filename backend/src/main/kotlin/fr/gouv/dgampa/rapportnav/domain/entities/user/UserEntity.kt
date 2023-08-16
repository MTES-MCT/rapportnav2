package fr.gouv.dgampa.rapportnav.domain.entities.user

data class UserEntity(
    val id: Int?,
    val name: String,
    val email: String,
    val password: String

) {
}
