package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.apikey

import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.apikey.IDBApiKeyRepository
import org.springframework.stereotype.Repository

@Repository
class JPAApiKeyRepository(
    private val repository: IDBApiKeyRepository,
): IApiKeyRepository {
    override fun findByPublicId(id: String): ApiKeyModel? {
        return repository.findByPublicId(id)
    }
}
