package fr.gouv.dgampa.rapportnav.domain.repositories.apikey

import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionBAAEMPermanenceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionBAAEMPermanenceModel
import java.util.Optional
import java.util.UUID

interface IApiKeyRepository {
    fun findByPublicId(id: String): ApiKeyModel?
    fun findById(id: UUID): Optional<ApiKeyModel>
    fun findAll(): List<ApiKeyModel>
    fun save(key: ApiKeyModel): ApiKeyModel
}
