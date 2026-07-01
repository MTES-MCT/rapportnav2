package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CountryEntity
import fr.gouv.dgampa.rapportnav.infrastructure.utils.ICountryRepository

@UseCase
class GetCountries(
    private val countryRepo: ICountryRepository,
) {
    fun execute(): List<CountryEntity> {
        val countries = countryRepo.getCountries()
        val france = countries.firstOrNull { it.iso3 == "FRA" }

        val sortedCountries = countries
            .filterNot { it.iso3 == "FRA" }
            .sortedBy { it.name }

        return if(france != null) listOf(france) + sortedCountries else sortedCountries
    }
}
