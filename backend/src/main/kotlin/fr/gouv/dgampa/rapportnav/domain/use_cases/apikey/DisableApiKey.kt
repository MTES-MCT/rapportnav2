package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import java.time.Instant

@UseCase
class DisableApiKey(
    private val repo: IApiKeyRepository
) {

    private val logger = LoggerFactory.getLogger(DisableApiKey::class.java)

    @Transactional
    fun execute(publicId: String): Boolean {
        val apiKey = repo.findByPublicId(publicId)?.toApiKeyEntity() ?: return false

        repo.save(
            apiKey.copy(disabledAt = Instant.now())
        )

        logger.info("Disabled API key: $publicId")
        return true
    }
}
