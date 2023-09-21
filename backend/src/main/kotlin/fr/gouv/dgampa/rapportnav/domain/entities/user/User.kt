package fr.gouv.dgampa.rapportnav.domain.entities.user

data class User(
    val id: Int? = null,
    val name: String,
    val email: String,
    val password: String,
    var token: String? = null
) {
}
var User.token: String?
    get() = this.token
    set(value) {}
