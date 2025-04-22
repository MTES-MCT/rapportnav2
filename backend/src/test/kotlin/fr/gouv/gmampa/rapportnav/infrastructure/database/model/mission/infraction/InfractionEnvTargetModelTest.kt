package fr.gouv.gmampa.rapportnav.infrastructure.database.model.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionEnvTargetModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [InfractionEnvTargetModel::class])
class InfractionEnvTargetModelTest {

    @Test
    fun `execute should retrieve Infraction Env Target`() {
        val id = UUID.fromString("226d84bc-e6c5-4d29-8a5f-7db642f99d16");
        val infractionId = UUID.fromString("226d84bc-e6c5-4d29-8a5f-7db642f99d16");
        val infractionTargetEntity =
            InfractionEnvTargetModel(
                id = id,
                missionId = "3",
                actionId = "MyActionID",
                vesselIdentifier = "myVesselIdentifier",
                vesselType = VesselTypeEnum.FISHING.toString(),
                vesselSize = VesselSizeEnum.LESS_THAN_12m.toString(),
                identityControlledPerson = "myIdentityControlledPerson",
                infraction = InfractionModel(
                    id = infractionId,
                    missionId = "3",
                    actionId = "actionID",
                    controlType = "controlType",
                    infractionType = InfractionTypeEnum.WITH_REPORT.toString(),
                )
            ).toInfractionEnvTargetEntity()

        assertThat(infractionTargetEntity).isNotNull();
        assertThat(infractionTargetEntity.id).isEqualTo(id);
        assertThat(infractionTargetEntity.missionId).isEqualTo("3");
        assertThat(infractionTargetEntity.actionId).isEqualTo("MyActionID");
        assertThat(infractionTargetEntity.vesselType).isEqualTo(VesselTypeEnum.FISHING);
        assertThat(infractionTargetEntity.vesselSize).isEqualTo(VesselSizeEnum.LESS_THAN_12m);
        assertThat(infractionTargetEntity.vesselIdentifier).isEqualTo("myVesselIdentifier");
        assertThat(infractionTargetEntity.identityControlledPerson).isEqualTo("myIdentityControlledPerson");
    }

    @Test
    fun `execute should vessel information can be null`() {
        val id = UUID.fromString("0000-00-00-00-000000");
        val infractionId = UUID.fromString("226d84bc-e6c5-4d29-8a5f-7db642f99d16");
        val infractionTargetEntity =
            InfractionEnvTargetModel(
                id = id,
                missionId = "3",
                actionId = "MyActionID",
                identityControlledPerson = "myIdentityControlledPerson",
                infraction = InfractionModel(
                    id = infractionId,
                    missionId = "3",
                    actionId = "actionID",
                    controlType = "controlType",
                    infractionType = InfractionTypeEnum.WITH_REPORT.toString(),
                )
            ).toInfractionEnvTargetEntity()

        assertThat(infractionTargetEntity).isNotNull();
        assertThat(infractionTargetEntity.vesselType).isNull();
        assertThat(infractionTargetEntity.vesselSize).isNull();
        assertThat(infractionTargetEntity.vesselIdentifier).isNull();
    }

    @Test
    fun `execute should retrieve Infraction model from companion method`() {
        val id = UUID.fromString("0000-00-00-00-000000");
        val infractionId = UUID.fromString("0000-00-00-00-000000");
        val infraction = InfractionEnvTargetEntity(
            id = id,
            missionId = "761",
            actionId = "actionId",
            identityControlledPerson = "identityControlledPerson",
            vesselIdentifier = "myVesselIdentifier",
            vesselSize = VesselSizeEnum.FROM_24_TO_46m,
            vesselType = VesselTypeEnum.FISHING,
            infractionId = infractionId,
        )
        val model = InfractionEnvTargetModel.fromInfractionEnvTargetEntity(infraction)

        assertThat(model).isNotNull();
        assertThat(model.id).isEqualTo(id);
        assertThat(model.missionId).isEqualTo("761");
        assertThat(model.actionId).isEqualTo("actionId");
        assertThat(model.vesselType).isEqualTo(VesselTypeEnum.FISHING.toString());
        assertThat(model.vesselSize).isEqualTo(VesselSizeEnum.FROM_24_TO_46m.toString());
        assertThat(model.vesselIdentifier).isEqualTo("myVesselIdentifier");
        assertThat(model.identityControlledPerson).isEqualTo("identityControlledPerson");
    }

    @Test
    fun `execute should retrieve Infraction model from companion method with vessel value null`() {
        val id = UUID.fromString("0000-00-00-00-000000");
        val infractionId = UUID.fromString("0000-00-00-00-000000");
        val infraction = InfractionEnvTargetEntity(
            id = id,
            missionId = "761",
            actionId = "actionId",
            identityControlledPerson = "identityControlledPerson",
            infractionId = infractionId
        )
        val model = InfractionEnvTargetModel.fromInfractionEnvTargetEntity(infraction)

        assertThat(model).isNotNull();
        assertThat(model.vesselType).isNull();
        assertThat(model.vesselSize).isNull();
        assertThat(model.vesselIdentifier).isNull();
    }
}
