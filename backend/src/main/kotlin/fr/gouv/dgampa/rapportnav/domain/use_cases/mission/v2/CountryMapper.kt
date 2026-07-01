package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CountryEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Country

object CountryMapper {
    fun toCountry(entity: CountryEntity): Country {
        return Country(
            iso2 = entity.iso2,
            iso3 = entity.iso3,
            name = entity.name,
            flag = entity.flag,
        )
    }

    fun toCountries(entities: List<CountryEntity>): List<Country> {
        return entities.map(::toCountry)
    }
}
