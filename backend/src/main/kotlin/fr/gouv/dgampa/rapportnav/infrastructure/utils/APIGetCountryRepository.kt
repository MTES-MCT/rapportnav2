package fr.gouv.dgampa.rapportnav.infrastructure.utils

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CountryEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.utils.LoadJsonData
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.json.JsonMapper

@Repository
class APIGetCountryRepository<T>(
    private val mapper: JsonMapper
) : ICountryRepository {

    @Cacheable(value = ["countries"])
    override fun getCountries(): List<CountryEntity> {
        val json = LoadJsonData.loadFromPath("/json/stubs/countries.json") ?: return emptyList()
        return mapper.readValue(json, object : TypeReference<List<CountryEntity>>() {}) ?: emptyList()
    }
}
