package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository

@UseCase
class GetAllApiKeys(
    private val repo: IApiKeyRepository
) {
    fun execute(): List<ApiKeyEntity> {
        return listOf()
    }
}
