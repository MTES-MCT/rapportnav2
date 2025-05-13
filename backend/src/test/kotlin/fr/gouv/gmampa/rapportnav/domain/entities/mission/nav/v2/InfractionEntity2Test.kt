package fr.gouv.gmampa.rapportnav.domain.entities.mission.nav.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.v2.InfractionModel2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
class InfractionEntity2Test {

    @Test
    fun `execute should retrieve entity from Model`() {
        val model = InfractionModel2(
            id = UUID.randomUUID(),
            infractionType = InfractionTypeEnum.WITH_REPORT.toString(),
            natinfs = listOf("natInf2", "natInf3"),
            observations = "My observations"
        )

        val entity = InfractionEntity2.fromInfractionModel(model)
        assertThat(entity).isNotNull()
        assertThat(entity.id).isEqualTo(model.id)
        assertThat(entity.natinfs).isEqualTo(model.natinfs)
        assertThat(entity.observations).isEqualTo(model.observations)
        assertThat(entity.infractionType.toString()).isEqualTo(model.infractionType)
        assertThat(entity.observations).isEqualTo(model.observations)
    }

    @Test
    fun `execute should convert entity to Model`() {
        val entity = InfractionEntity2(
            id = UUID.randomUUID(),
            infractionType = InfractionTypeEnum.WITH_REPORT,
            natinfs = listOf("natInf2", "natInf3"),
            observations = "My observations"
        )

        val model = entity.toInfractionModel()
        assertThat(entity).isNotNull()
        assertThat(entity.id).isEqualTo(model.id)
        assertThat(entity.natinfs).isEqualTo(model.natinfs)
        assertThat(entity.observations).isEqualTo(model.observations)
        assertThat(entity.infractionType.toString()).isEqualTo(model.infractionType)
        assertThat(entity.observations).isEqualTo(model.observations)
    }
}
