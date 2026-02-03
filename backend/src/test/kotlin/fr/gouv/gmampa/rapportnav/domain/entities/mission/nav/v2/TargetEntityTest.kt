package fr.gouv.gmampa.rapportnav.domain.entities.mission.nav.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetExternalDataEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class TargetEntityTest {

    @Test
    fun `execute should retrieve entity from Model`() {
        val model = TargetModel2(
            startDateTimeUtc = Instant.parse("2020-01-01T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2020-02-01T00:00:00Z"),
            id = UUID.randomUUID(),
            status = TargetStatusType.IN_PROCESS.toString(),
            actionId = UUID.randomUUID().toString(),
            vesselSize = VesselSizeEnum.FROM_12_TO_24m.toString(),
            vesselType = VesselTypeEnum.SAILING.toString(),
            vesselIdentifier = "My vesselIdentifier",
            agent = "My agent",
            vesselName = "My vesselName",
            identityControlledPerson = "My identityContolledPerson",
            targetType = TargetType.COMPANY,
            source = "RAPPORTNAV",
            controls = listOf(),
            externalId = "MyExternalId"
        )

        val entity = TargetEntity.fromTargetModel(model)
        assertThat(entity).isNotNull()
        assertThat(entity.id).isEqualTo(model.id)
        assertThat(entity.agent).isEqualTo(model.agent)
        assertThat(entity.actionId).isEqualTo(model.actionId)
        assertThat(entity.controls).isEqualTo(model.controls)
        assertThat(entity.targetType).isEqualTo(model.targetType)
        assertThat(entity.source.toString()).isEqualTo(model.source)
        assertThat(entity.status.toString()).isEqualTo(model.status)
        assertThat(entity.externalData?.id).isEqualTo(model.externalId)
        assertThat(entity.endDateTimeUtc).isEqualTo(model.endDateTimeUtc)
        assertThat(entity.startDateTimeUtc).isEqualTo(model.startDateTimeUtc)
        assertThat(entity.vesselIdentifier).isEqualTo(model.vesselIdentifier)
        assertThat(entity.vesselSize.toString()).isEqualTo(model.vesselSize)
        assertThat(entity.vesselType.toString()).isEqualTo(model.vesselType)
        assertThat(entity.identityControlledPerson).isEqualTo(model.identityControlledPerson)
    }

    @Test
    fun `execute should convert entity from Model`() {
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
            externalData = TargetExternalDataEntity(id = "MyExternalId"),
            source = MissionSourceEnum.RAPPORTNAV
        )

        val model = entity.toTargetModel()
        assertThat(model).isNotNull()
        assertThat(entity.id).isEqualTo(model.id)
        assertThat(entity.agent).isEqualTo(model.agent)
        assertThat(entity.actionId).isEqualTo(model.actionId)
        assertThat(entity.controls).isEqualTo(model.controls)
        assertThat(entity.targetType).isEqualTo(model.targetType)
        assertThat(entity.externalData?.id).isEqualTo(model.externalId)
        assertThat(entity.source.toString()).isEqualTo(model.source)
        assertThat(entity.status.toString()).isEqualTo(model.status)
        assertThat(entity.endDateTimeUtc).isEqualTo(model.endDateTimeUtc)
        assertThat(entity.startDateTimeUtc).isEqualTo(model.startDateTimeUtc)
        assertThat(entity.vesselIdentifier).isEqualTo(model.vesselIdentifier)
        assertThat(entity.vesselSize.toString()).isEqualTo(model.vesselSize)
        assertThat(entity.vesselType.toString()).isEqualTo(model.vesselType)
        assertThat(entity.identityControlledPerson).isEqualTo(model.identityControlledPerson)
    }

    @Test
    fun `execute should return a specific control`() {
        val targetId = UUID.randomUUID()
        val control = ControlEntity(
            id = UUID.randomUUID(),
            controlType = ControlType.GENS_DE_MER,
            amountOfControls = 1
        )

        val entity = TargetEntity(
            startDateTimeUtc = Instant.parse("2020-01-01T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2020-02-01T00:00:00Z"),
            id = targetId,
            status = TargetStatusType.IN_PROCESS,
            actionId = UUID.randomUUID().toString(),
            vesselSize = VesselSizeEnum.FROM_12_TO_24m,
            vesselType = VesselTypeEnum.SAILING,
            vesselIdentifier = "My vesselIdentifier",
            agent = "My agent",
            vesselName = "My vesselName",
            identityControlledPerson = "My identityContolledPerson",
            targetType = TargetType.COMPANY,
            controls = listOf(control)
        )

        val response = entity.getControlByType(ControlType.GENS_DE_MER)
        assertThat(response).isNotNull()
        assertThat(response).isEqualTo(control)
    }

}
