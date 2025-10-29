package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyAuditRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

@UseCase
class GetApiKeyAuditLogsForPublicId(
    private val repo: IApiKeyAuditRepository
) {

    private val logger = LoggerFactory.getLogger(GetApiKeyAuditLogsForPublicId::class.java)

    /**
     * Returns audit logs for a given API key since a specific timestamp.
     *
     * @param publicId The UUID of the API key.
     * @param since The timestamp after which logs should be retrieved.
     * @return A list of audit log entries.
     */
    fun execute(publicId: UUID, since: Instant): List<ApiKeyAuditModel> {
        require(since.isBefore(Instant.now().plusSeconds(1))) {
            "The 'since' parameter cannot be in the future."
        }

        val logs = repo.findByApiKeyIdAndTimestampAfter(publicId, since)

        logger.info("Fetched {} audit logs for API key '{}' since {}", logs.size, publicId, since)
        return logs
    }
}
