package fr.gouv.dgampa.rapportnav.infrastructure.utils

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CountryEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CountryResponse
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.json.JsonMapper
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIGetCountryRepository<T>(
    private val mapper: JsonMapper,
    private val clientFactory: HttpClientFactory,
) : ICountryRepository {
    val url = "https://data.enseignementsup-recherche.gouv.fr/api/explore/v2.1/catalog/datasets/curiexplore-pays/exports/json?select=name_fr%2C%20iso3%2C%20iso2%2C%20flag&refine=world%3A%22True%22"
    private val logger: Logger = LoggerFactory.getLogger(APIGetCountryRepository::class.java)

    @Cacheable(value = ["countries"])
    override fun getCountries(): List<CountryEntity> {
        logger.info("Fetching countries Referential from URL: $url")

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build()

        val response = clientFactory.create().send(request, HttpResponse.BodyHandlers.ofString())
        logger.info("APICountryRepository::getCountries Response received, Status code: ${response.statusCode()}")

        if (response.statusCode() !in 200..299) {
            throw BackendInternalException(
                message = "APICountryRepository.getCountries failed with status ${response.statusCode()}",
                originalException = RuntimeException(response.body())
            )
        }
        return mapper.readValue(response.body(), object : TypeReference<List<CountryEntity>>() {}) ?: emptyList()
    }
}
