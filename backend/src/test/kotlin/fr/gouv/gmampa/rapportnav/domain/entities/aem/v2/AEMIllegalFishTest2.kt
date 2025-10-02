package fr.gouv.gmampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMIllegalFish
import fr.gouv.dgampa.rapportnav.domain.entities.aem.v2.AEMIllegalFish2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionFishAction
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ExtendedFishActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [AEMIllegalFish2::class])
class AEMIllegalFishTest2 {

    @Test
    fun `Should init illegal fish with different values`() {
        val actions = extendedFishActionEntities()
        val illegalImmigration = AEMIllegalFish2(
            fishActions = actions,
            navActions = emptyList(),
            missionEndDateTime = Instant.parse("2019-09-09T00:00:00.000+01:00")
        )

        assertThat(illegalImmigration).isNotNull();
        assertThat(illegalImmigration.quantityOfFish).isEqualTo(6.0);
        assertThat(illegalImmigration.nbrOfHourAtSea).isEqualTo(3.0);
        assertThat(illegalImmigration.nbrOfInfraction).isEqualTo(4.0);
        assertThat(illegalImmigration.nbrOfPolFishAction).isEqualTo(2.0);
        assertThat(illegalImmigration.nbrOfInfractionWithPV).isEqualTo(4.0);
    }

    private fun extendedFishActionEntities(): List<MissionFishActionEntity> {
        val actions = listOf(
            MissionFishActionEntity(
                missionId = 761,
                id = 234,
                fishActionType = MissionActionType.SEA_CONTROL,
                speciesInfractions = listOf(),
                gearInfractions = listOf(GearInfraction(infractionType = InfractionType.WITH_RECORD)),
                otherInfractions = listOf(OtherInfraction()),
                logbookInfractions = listOf(LogbookInfraction(infractionType = InfractionType.WITHOUT_RECORD, natinf = 27688)),
                actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                actionEndDatetimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                speciesQuantitySeized = 4
            ),
            MissionFishActionEntity(
                missionId = 761,
                id = 234,
                fishActionType = MissionActionType.SEA_CONTROL,
                logbookInfractions = listOf(LogbookInfraction()),
                gearInfractions = listOf(GearInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 49558)),
                otherInfractions = listOf(OtherInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 98308)),
                speciesInfractions = listOf(
                    SpeciesInfraction(infractionType = InfractionType.WITH_RECORD),
                    SpeciesInfraction(infractionType = InfractionType.WITHOUT_RECORD, natinf = 203839)
                ),
                actionDatetimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                actionEndDatetimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                speciesQuantitySeized = 2
            )
        );
        return actions
    }
}
