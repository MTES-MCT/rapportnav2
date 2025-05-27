package fr.gouv.cnsp.monitorfish.infrastructure.api.outputs

import com.neovisionaries.i18n.CountryCode

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
)
