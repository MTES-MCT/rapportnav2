package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Infraction
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*


@ExtendWith(SpringExtension::class)
class InfractionTest {

    @Test
    fun `execute should convert into entity`() {
        val controlId = UUID.randomUUID()
        val natInfs = listOf("NatInf1", "NatInf2")

        val input = Infraction(
            natinfs = natInfs,
            observations = "My observations",
            infractionType = InfractionTypeEnum.WITH_REPORT
        )
        val entity = input.toInfractionEntity()

        assertThat(entity).isNotNull()
        assertThat(entity.id).isNotNull()
        assertThat(entity.natinfs).isEqualTo(natInfs)
        assertThat(entity.observations).isEqualTo("My observations")
        assertThat(entity.infractionType).isEqualTo(InfractionTypeEnum.WITH_REPORT)
    }


    @Test
    fun `execute should convert from target entity`() {
        val infractionId = UUID.randomUUID()

        val entity = InfractionEntity(
            id = infractionId,
            infractionType = InfractionTypeEnum.WITH_REPORT,
            natinfs = listOf(),
            observations = "My observations",
        )

        val value = Infraction.fromInfractionEntity(entity)

        assertThat(value).isNotNull()
        assertThat(value.id).isNotNull()
        assertThat(value.natinfs).isEqualTo(entity.natinfs)
        assertThat(value.observations).isEqualTo(entity.observations)
        assertThat(value.infractionType).isEqualTo(entity.infractionType)
    }
}
