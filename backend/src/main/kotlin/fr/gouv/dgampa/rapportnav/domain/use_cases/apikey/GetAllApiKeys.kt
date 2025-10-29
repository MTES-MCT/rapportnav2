package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import org.slf4j.LoggerFactory

@UseCase
class GetAllApiKeys(
    private val repo: IApiKeyRepository
) {

    private val logger = LoggerFactory.getLogger(GetAllApiKeys::class.java)

    /**
     * Retrieves all API keys from the repository.
     *
     * @return A list of [ApiKeyEntity] (empty if none found).
     */
    fun execute(): List<ApiKeyEntity> {
        val models = repo.findAll().orEmpty()
        val entities = models.map { it.toApiKeyEntity() }

        logger.info("Retrieved {} API keys from repository", entities.size)
        return entities
    }
}
