package fr.gouv.gmampa.rapportnav.domain.entities.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.cnsp.monitorfish.infrastructure.api.outputs.VesselIdentityDataOutput
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.VesselEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class VesselEntityTest {

    @Test
    fun `execute should retrieve entity from Identity data output`() {
        val dataOutput = VesselIdentityDataOutput(
            beaconNumber = "",
            districtCode = "",
            externalReferenceNumber = "MyExternalReference",
            flagState =  CountryCode.FR,
            imo = "",
            internalReferenceNumber = "",
            ircs = "",
            mmsi = "",
            vesselId = 34343,
            vesselLength = 12.0,
            vesselName = "myVesselName"
        )
        val entity = VesselEntity.fromVesselIdentityDataOutput(dataOutput)
        assertThat(entity).isNotNull()
        assertThat(entity.vesselId).isEqualTo(dataOutput.vesselId)
        assertThat(entity.flagState).isEqualTo(dataOutput.flagState)
        assertThat(entity.vesselName).isEqualTo(dataOutput.vesselName)
        assertThat(entity.externalReferenceNumber).isEqualTo(dataOutput.externalReferenceNumber)
    }
}
