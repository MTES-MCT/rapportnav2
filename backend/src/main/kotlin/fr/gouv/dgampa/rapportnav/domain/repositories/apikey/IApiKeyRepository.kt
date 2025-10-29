package fr.gouv.dgampa.rapportnav.domain.repositories.apikey

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel

interface IApiKeyRepository {
    fun findByPublicId(id: String): ApiKeyModel?
}
