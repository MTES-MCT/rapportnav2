package fr.gouv.gmampa.rapportnav.domain.entities.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EstablishmentEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.EstablishmentModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class EstablishmentEntityTest {

    @Test
    fun `execute should get model from entity`() {
        val entity = EstablishmentEntity(
            id = 2,
            name = "myEstablishment",
            address = "myAddress",
            isForeign = false,
            city = "Paris",
            siren = "mySiren",
            zipcode = "18733"
        )
        val response = entity.toEstablishmentModel()

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(entity.id)
        assertThat(response.name).isEqualTo(entity.name)
        assertThat(response.address).isEqualTo(entity.address)
        assertThat(response.isForeign).isEqualTo(entity.isForeign)
        assertThat(response.city).isEqualTo(entity.city)
        assertThat(response.siren).isEqualTo(entity.siren)
        assertThat(response.zipcode).isEqualTo(entity.zipcode)
    }

    @Test
    fun `execute should get entity from model`() {
        val model = EstablishmentModel(
            id = 2,
            name = "myEstablishment",
            address = "myAddress",
            isForeign = false,
            city = "Paris",
            siren = "mySiren",
            zipcode = "18733"
        )
        val response = EstablishmentEntity.fromEstablishmentModel(model)

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(model.id)
        assertThat(response.name).isEqualTo(model.name)
        assertThat(response.address).isEqualTo(model.address)
        assertThat(response.isForeign).isEqualTo(model.isForeign)
        assertThat(response.city).isEqualTo(model.city)
        assertThat(response.siren).isEqualTo(model.siren)
        assertThat(response.zipcode).isEqualTo(model.zipcode)
    }
}
