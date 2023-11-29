package fr.gouv.dgampa.rapportnav.domain.entities.user

data class User(
    val id: Int? = null,
    var agentId: Int? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    var token: String? = null,
) {
    constructor(user: User): this(
        id = user.id,
        agentId = user.agentId,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email,
        password = user.password,
        token = user.token,
    )
}
var User.token: String?
    get() = this.token
    set(value) {}
