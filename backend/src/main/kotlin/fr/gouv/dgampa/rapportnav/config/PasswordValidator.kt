package fr.gouv.dgampa.rapportnav.config

object PasswordValidator {
    private val pattern = Regex(
        pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{16,}$"
    )

    fun isStrong(password: String): Boolean = pattern.matches(password)
}
