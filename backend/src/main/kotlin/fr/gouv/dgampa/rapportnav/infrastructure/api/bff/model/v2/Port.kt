package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.PortEntity

class Port(
    val locode: String,
    /** ISO Alpha-2 country code */
    val countryCode: CountryCode? = CountryCode.FR,
    val name: String,
    val facade: String? = null,
    val faoAreas: List<String> = emptyList(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val region: String? = null,
) {
    fun toPortEntity(): PortEntity {
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

    companion object {
        fun fromPortEntity(entity: PortEntity): Port {
            return Port(
                locode = entity.locode,
                countryCode = entity.countryCode,
                name = entity.name,
                facade = entity.facade,
                faoAreas = entity.faoAreas,
                latitude = entity.latitude,
                longitude = entity.longitude,
                region = entity.region,
            )
        }
    }
}
