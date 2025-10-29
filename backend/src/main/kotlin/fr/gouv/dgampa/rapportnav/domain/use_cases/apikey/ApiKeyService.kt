package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class ApiKeyService(
    private val repo: IApiKeyRepository
) {
    private val encoder = BCryptPasswordEncoder()

    fun isValidApiKey(rawKey: String, masterPassword: String): Boolean {
        val publicId = rawKey.substringBefore(".")
        val stored = repo.findByPublicId(publicId) ?: return false
        return encoder.matches("$masterPassword:$rawKey", stored.hashedKey)
    }
}
