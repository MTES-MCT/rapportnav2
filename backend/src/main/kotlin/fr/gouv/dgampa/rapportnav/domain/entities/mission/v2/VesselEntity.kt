package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.cnsp.monitorfish.infrastructure.api.outputs.VesselIdentityDataOutput

class VesselEntity(
    val beaconNumber: String? = null,
    val districtCode: String? = null,
    val externalReferenceNumber: String? = null,
    val flagState: CountryCode? = null,
    val imo: String? = null,
    val internalReferenceNumber: String? = null,
    val ircs: String? = null,
    val mmsi: String? = null,
    val vesselId: Int,
    val vesselLength: Double? = null,
    val vesselName: String? = null
) {
    companion object {
        fun fromVesselIdentityDataOutput(data: VesselIdentityDataOutput): VesselEntity {
            return VesselEntity(
                vesselId = data.vesselId,
                vesselName = data.vesselName,
                flagState = data.flagState,
                externalReferenceNumber = data.externalReferenceNumber
            )
        }
    }
}
