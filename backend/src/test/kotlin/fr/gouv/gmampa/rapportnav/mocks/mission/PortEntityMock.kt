package fr.gouv.gmampa.rapportnav.mocks.mission

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.PortEntity

object PortEntityMock {
    fun create(
        locode: String = "FRLEH",
        countryCode: CountryCode? = CountryCode.FR,
        name: String = "Le Havre",
        facade: String? = null,
        faoAreas: List<String> = emptyList(),
        latitude: Double? = null,
        longitude: Double? = null,
        region: String? = null,
    ): PortEntity {
        return PortEntity(
            locode = locode,
            countryCode = countryCode,
            name = name,
            facade = facade,
            faoAreas = faoAreas,
            latitude = latitude,
            longitude = longitude,
            region = region,
        )
    }
}
