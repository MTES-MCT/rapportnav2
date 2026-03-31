package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.cnsp.monitorfish.infrastructure.api.outputs.VesselIdentityDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output.PortDataOutput

class PortEntity(
    val locode: String,
    /** ISO Alpha-2 country code */
    val countryCode: CountryCode? = CountryCode.FR,
    val name: String,
    val facade: String? = null,
    val faoAreas: List<String> = emptyList(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val region: String? = null,
)
