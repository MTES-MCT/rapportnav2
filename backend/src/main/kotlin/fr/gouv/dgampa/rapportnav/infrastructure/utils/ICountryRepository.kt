package fr.gouv.dgampa.rapportnav.infrastructure.utils

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CountryEntity

interface ICountryRepository {
    fun getCountries(): List<CountryEntity>
}
