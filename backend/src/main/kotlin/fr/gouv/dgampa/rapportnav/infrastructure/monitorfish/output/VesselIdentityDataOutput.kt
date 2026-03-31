package fr.gouv.cnsp.monitorfish.infrastructure.api.outputs

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.VesselEntity

data class VesselIdentityDataOutput(
    val beaconNumber: String? = null,
    val districtCode: String? = null,
    val externalReferenceNumber: String? = null,
    val flagState: CountryCode,
    val imo: String? = null,
    val internalReferenceNumber: String? = null,
    val ircs: String? = null,
    val mmsi: String? = null,
    val vesselId: Int,
    val vesselLength: Double? = null,
    val vesselName: String? = null
) {
    fun toVesselEntity() = VesselEntity(
        beaconNumber = beaconNumber,
        districtCode = districtCode,
        externalReferenceNumber = externalReferenceNumber,
        flagState = flagState,
        imo = imo,
        internalReferenceNumber = internalReferenceNumber,
        ircs = ircs,
        mmsi = mmsi,
        vesselId = vesselId,
        vesselLength = vesselLength,
        vesselName = vesselName
    )
}
