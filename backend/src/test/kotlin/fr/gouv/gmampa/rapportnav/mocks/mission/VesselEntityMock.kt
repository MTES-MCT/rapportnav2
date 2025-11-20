package fr.gouv.gmampa.rapportnav.mocks.mission

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.VesselEntity

object VesselEntityMock {
    fun create(
        vesselId: Int = 1,
        vesselLength: Double? = null,
        vesselName: String? = null,
        beaconNumber: String? = null,
        districtCode: String? = null,
        externalReferenceNumber: String? = null,
        flagState: CountryCode? = null,
        imo: String? = null,
        internalReferenceNumber: String? = null,
        ircs: String? = null,
        mmsi: String? = null,
    ): VesselEntity {
        return VesselEntity(
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
}
