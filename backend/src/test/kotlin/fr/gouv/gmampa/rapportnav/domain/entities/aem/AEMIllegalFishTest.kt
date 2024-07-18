package fr.gouv.gmampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMIllegalFish
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ExtendedFishActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZonedDateTime


@SpringBootTest(classes = [AEMIllegalFish::class])
class AEMIllegalFishTest {

    @Test
    fun `Should init illegal fish with different values`() {
        val actions = extendedFishActionEntities()
        val illegalImmigration = AEMIllegalFish(fishActions = actions);

        assertThat(illegalImmigration).isNotNull();
        assertThat(illegalImmigration.quantityOfFish).isEqualTo(0);
        assertThat(illegalImmigration.nbrOfInfraction).isEqualTo(8);
        assertThat(illegalImmigration.nbrOfHourInSea).isEqualTo(3);
        assertThat(illegalImmigration.nbrOfPolFishAction).isEqualTo(2);
        assertThat(illegalImmigration.nbrOfInfractionWithPV).isEqualTo(4);
    }

    private fun extendedFishActionEntities(): List<ExtendedFishActionEntity> {
        val actions = listOf(
            ExtendedFishActionEntityMock.create(
                controlAction = ExtendedFishActionControlEntity(
                    action = FishActionControlMock.create(
                        speciesInfractions = listOf(),
                        gearInfractions = listOf(GearInfraction(infractionType = InfractionType.WITH_RECORD)),
                        otherInfractions = listOf(OtherInfraction()),
                        logbookInfractions = listOf(LogbookInfraction(infractionType = InfractionType.WITHOUT_RECORD)),
                        actionDatetimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                        actionEndDatetimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                    )
                )
            ),
            ExtendedFishActionEntityMock.create(
                controlAction = ExtendedFishActionControlEntity(
                    action = FishActionControlMock.create(
                        logbookInfractions = listOf(LogbookInfraction()),
                        gearInfractions = listOf(GearInfraction(infractionType = InfractionType.WITH_RECORD)),
                        otherInfractions = listOf(OtherInfraction(infractionType = InfractionType.WITH_RECORD)),
                        speciesInfractions = listOf(
                            SpeciesInfraction(infractionType = InfractionType.WITH_RECORD),
                            SpeciesInfraction(infractionType = InfractionType.WITHOUT_RECORD)
                        ),
                        actionDatetimeUtc = ZonedDateTime.parse("2019-09-09T02:00:00.000+01:00"),
                        actionEndDatetimeUtc = ZonedDateTime.parse("2019-09-09T04:00:00.000+01:00"),
                    )
                )
            )
        );
        return actions
    }
}
