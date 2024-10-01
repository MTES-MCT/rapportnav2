package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.infraction


import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.Infraction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.InfractionTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.InfractionsByVessel
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.assertj.core.api.Assertions.assertThat

@ExtendWith(SpringExtension::class)
class InfractionsByVesselTest {
    @Test
    fun `execute should retrieve infraction order by vessel id`() {
        val infractions = listOf(
            Infraction(
                id = "myInfraction1",
                actionId = "actionId",
                missionId = 145,
                infractionType = InfractionTypeEnum.WITH_REPORT,
                target = InfractionTarget(
                    id = "myInfraction1",
                    identityControlledPerson = "identityPerson1"
                )
            ),
            Infraction(
                id = "myInfraction2",
                actionId = "actionId",
                missionId = 145,
                infractionType = InfractionTypeEnum.WITH_REPORT,
                target = InfractionTarget(
                    id = "myInfraction2",
                    vesselIdentifier = "firstVesselIdentifier",
                    identityControlledPerson = "identityPerson2"
                )
            ),
            Infraction(
                id = "myInfraction3",
                actionId = "actionId",
                missionId = 145,
                infractionType = InfractionTypeEnum.WITH_REPORT,
                target = InfractionTarget(
                    id = "myInfraction3",
                    identityControlledPerson = "identityPerson3"
                )
            ),
            Infraction(
                id = "myInfraction4",
                actionId = "actionId",
                missionId = 145,
                infractionType = InfractionTypeEnum.WITH_REPORT,
                target = InfractionTarget(
                    id = "myInfraction4",
                    vesselIdentifier = "firstVesselIdentifier",
                    identityControlledPerson = "identityPerson4",
                )
            ),
            Infraction(
                id = "myInfraction5",
                actionId = "actionId",
                missionId = 145,
                infractionType = InfractionTypeEnum.WITH_REPORT,
                target = InfractionTarget(
                    id = "myInfraction1",
                    vesselIdentifier = "secondVesselIdentifier",
                    identityControlledPerson = "identityPerson5",
                )
            ),
            Infraction(
                id = "myInfraction5",
                actionId = "actionId",
                missionId = 145,
                infractionType = InfractionTypeEnum.WITH_REPORT,
                target = InfractionTarget(
                    id = "myInfraction5",
                    identityControlledPerson = "identityPerson1",
                )
            )
        )
        val sortedInfractions = InfractionsByVessel(infractions=infractions).groupInfractionsByVesselIdentifier(infractions);
        assertThat(sortedInfractions).isNotNull;
        assertThat(sortedInfractions.size).isEqualTo(4);
        val groupByVessel = sortedInfractions.filter { it.vesselIdentifier == "firstVesselIdentifier" }
        val groupByIdentityPerson = sortedInfractions.filter { it.identityControlledPerson == "identityPerson1" };

        assertThat(groupByVessel[0].infractions.size).isEqualTo(2);
        assertThat(groupByIdentityPerson[0].infractions.size).isEqualTo(2);


    }
}
