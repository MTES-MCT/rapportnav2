package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.Optional

@UseCase
class UpdateApiKeyLastUsedAt(
    private val repo: IApiKeyRepository
) {

    private val logger = LoggerFactory.getLogger(UpdateApiKeyLastUsedAt::class.java)

    @Transactional
    fun execute(apiKey: ApiKeyEntity): Optional<ApiKeyModel> {
        val updated = repo.findById(apiKey.id).map { key ->
            repo.save(
                key.toApiKeyEntity().copy(lastUsedAt = Instant.now())
            )
        }

        return updated
    }

}
