package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.VesselEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Vessel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class VesselTest {

    @Test
    fun `execute should retrieve entity from Identity data output`() {
        val entity = VesselEntity(
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
        val response = Vessel.fromVesselEntity(entity)
        assertThat(entity).isNotNull()
        assertThat(entity.vesselId).isEqualTo(response.vesselId)
        assertThat(entity.flagState).isEqualTo(response.flagState)
        assertThat(entity.vesselName).isEqualTo(response.vesselName)
        assertThat(entity.externalReferenceNumber).isEqualTo(response.externalReferenceNumber)
    }
}
