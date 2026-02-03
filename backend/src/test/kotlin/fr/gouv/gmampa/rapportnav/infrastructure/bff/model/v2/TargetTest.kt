package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetExternalDataEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Target
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.TargetExternalData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class TargetTest {
    @Test
    fun `execute should convert into entity`() {
        val input = Target(
            startDateTimeUtc = Instant.parse("2020-01-01T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2020-02-01T00:00:00Z"),
            id = UUID.randomUUID(),
            status = TargetStatusType.IN_PROCESS,
            actionId = UUID.randomUUID().toString(),
            vesselSize = VesselSizeEnum.FROM_12_TO_24m,
            vesselType = VesselTypeEnum.SAILING,
            vesselIdentifier = "My vesselIdentifier",
            agent = "My agent",
            vesselName = "My vesselName",
            identityControlledPerson = "My identityContolledPerson",
            targetType = TargetType.COMPANY,
            controls = listOf(),
            externalData = TargetExternalData(id = "myExternalData")
        )

        val entity = input.toTargetEntity()
        assertThat(entity).isNotNull()
        assertThat(entity.id).isEqualTo(input.id)
        assertThat(entity.agent).isEqualTo(input.agent)
        assertThat(entity.status).isEqualTo(input.status)
        assertThat(entity.actionId).isEqualTo(input.actionId)
        assertThat(entity.controls).isEqualTo(input.controls)
        assertThat(entity.targetType).isEqualTo(input.targetType)
        assertThat(entity.vesselSize).isEqualTo(input.vesselSize)
        assertThat(entity.vesselType).isEqualTo(input.vesselType)
        assertThat(entity.endDateTimeUtc).isEqualTo(input.endDateTimeUtc)
        assertThat(entity.startDateTimeUtc).isEqualTo(input.startDateTimeUtc)
        assertThat(entity.vesselIdentifier).isEqualTo(input.vesselIdentifier)
        assertThat(entity.externalData?.id).isEqualTo(input.externalData?.id)
        assertThat(entity.identityControlledPerson).isEqualTo(input.identityControlledPerson)
    }


    @Test
    fun `execute should convert from entity`() {
        val entity = TargetEntity(
            startDateTimeUtc = Instant.parse("2020-01-01T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2020-02-01T00:00:00Z"),
            id = UUID.randomUUID(),
            status = TargetStatusType.IN_PROCESS,
            actionId = UUID.randomUUID().toString(),
            vesselSize = VesselSizeEnum.FROM_12_TO_24m,
            vesselType = VesselTypeEnum.SAILING,
            vesselIdentifier = "My vesselIdentifier",
            agent = "My agent",
            vesselName = "My vesselName",
            identityControlledPerson = "My identityContolledPerson",
            targetType = TargetType.COMPANY,
            controls = listOf(),
            externalData = TargetExternalDataEntity(id = "myExternalData")
        )

        val value = Target.fromTargetEntity(entity)
        assertThat(value).isNotNull()
        assertThat(entity.id).isEqualTo(value.id)
        assertThat(entity.agent).isEqualTo(value.agent)
        assertThat(entity.status).isEqualTo(value.status)
        assertThat(entity.actionId).isEqualTo(value.actionId)
        assertThat(entity.controls).isEqualTo(value.controls)
        assertThat(entity.targetType).isEqualTo(value.targetType)
        assertThat(entity.vesselSize).isEqualTo(value.vesselSize)
        assertThat(entity.vesselType).isEqualTo(value.vesselType)
        assertThat(entity.endDateTimeUtc).isEqualTo(value.endDateTimeUtc)
        assertThat(entity.startDateTimeUtc).isEqualTo(value.startDateTimeUtc)
        assertThat(entity.vesselIdentifier).isEqualTo(value.vesselIdentifier)
        assertThat(entity.externalData?.id).isEqualTo(value.externalData?.id)
        assertThat(entity.identityControlledPerson).isEqualTo(value.identityControlledPerson)
    }
}
