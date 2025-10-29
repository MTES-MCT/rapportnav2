package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.apikey

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBApiKeyRepository : JpaRepository<ApiKeyModel, UUID> {
    fun findByPublicId(id: String): ApiKeyModel?
    fun save(key: ApiKeyModel): ApiKeyModel
}
