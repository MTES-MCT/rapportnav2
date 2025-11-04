package fr.gouv.dgampa.rapportnav.config

object PasswordValidator {
    private val pattern = Regex(
        pattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#\$%^&*()-_=+\\[\\]{}|;:,.<>?-]).{16,}\$"
    )

    fun isStrong(password: String): Boolean =  pattern.matches(password)
}
