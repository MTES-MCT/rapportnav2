package fr.gouv.gmampa.rapportnav.infrastructure.monitorfish.output

import com.neovisionaries.i18n.CountryCode
import fr.gouv.cnsp.monitorfish.infrastructure.api.outputs.VesselIdentityDataOutput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class VesselIdentityDataOutputTest {

    @Test
    fun `toVesselEntity should convert data output to entity with all fields`() {
        val dataOutput = VesselIdentityDataOutput(
            beaconNumber = "beacon123",
            districtCode = "district456",
            externalReferenceNumber = "MyExternalReference",
            flagState = CountryCode.FR,
            imo = "imo789",
            internalReferenceNumber = "internal123",
            ircs = "ircs456",
            mmsi = "mmsi789",
            vesselId = 34343,
            vesselLength = 12.0,
            vesselName = "myVesselName"
        )

        val entity = dataOutput.toVesselEntity()

        assertThat(entity).isNotNull()
        assertThat(entity.vesselId).isEqualTo(dataOutput.vesselId)
        assertThat(entity.flagState).isEqualTo(dataOutput.flagState)
        assertThat(entity.vesselName).isEqualTo(dataOutput.vesselName)
        assertThat(entity.externalReferenceNumber).isEqualTo(dataOutput.externalReferenceNumber)
        assertThat(entity.beaconNumber).isEqualTo(dataOutput.beaconNumber)
        assertThat(entity.districtCode).isEqualTo(dataOutput.districtCode)
        assertThat(entity.imo).isEqualTo(dataOutput.imo)
        assertThat(entity.internalReferenceNumber).isEqualTo(dataOutput.internalReferenceNumber)
        assertThat(entity.ircs).isEqualTo(dataOutput.ircs)
        assertThat(entity.mmsi).isEqualTo(dataOutput.mmsi)
        assertThat(entity.vesselLength).isEqualTo(dataOutput.vesselLength)
    }
}
