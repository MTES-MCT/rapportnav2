package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.VesselEntity

class Vessel(
    val externalReferenceNumber: String? = null,
    val flagState: CountryCode,
    val internalReferenceNumber: String? = null,
    val vesselId: Int,
    val vesselName: String? = null
) {
    companion object {
        fun fromVesselEntity(entity: VesselEntity): Vessel {
            return Vessel(
                vesselId = entity.vesselId,
                vesselName = entity.vesselName,
                flagState = entity.flagState,
                externalReferenceNumber = entity.externalReferenceNumber
            )
        }
    }
}
