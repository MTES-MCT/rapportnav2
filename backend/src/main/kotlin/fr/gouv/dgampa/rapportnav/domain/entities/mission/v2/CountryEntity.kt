package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import com.fasterxml.jackson.annotation.JsonProperty

data class CountryResponse(
    @field:JsonProperty("total_count")
    val total: Int? = null,
    @field:JsonProperty("results")
    val countries: List<CountryEntity>? = null,
)

data class CountryEntity(
    @field:JsonProperty("flag")
    val flag: String? = null,
    @field:JsonProperty("iso3")
    val iso3: String? = null,
    @field:JsonProperty("iso2")
    val iso2: String? = null,
    @field:JsonProperty("name_fr")
    val name: String? = null,
)

