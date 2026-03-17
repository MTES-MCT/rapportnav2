package fr.gouv.gmampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMIllegalFish
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant


@SpringBootTest(classes = [AEMIllegalFish::class])
class AEMIllegalFishTest {

    @Test
    fun `Should init illegal fish with different values`() {
        val actions = listOf(
            MissionFishActionEntity(
                missionId = 761,
                id = 234,
                fishActionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                actionEndDatetimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                speciesQuantitySeized = 4,
                fishInfractions = listOf(
                    FishInfraction(natinf = 1, infractionType = InfractionType.WITH_RECORD),
                    FishInfraction(natinf = 1, infractionType = InfractionType.WITH_RECORD),
                    FishInfraction(natinf = 1, infractionType = InfractionType.WITH_RECORD),
                ),
            ),
            MissionFishActionEntity(
                missionId = 761,
                id = 235,
                fishActionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                actionEndDatetimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                speciesQuantitySeized = 2,
                fishInfractions = listOf(FishInfraction(natinf = 1)),
            )
        )
        val result = AEMIllegalFish(
            fishActions = actions,
            navActions = emptyList(),
            missionEndDateTime = Instant.parse("2019-09-09T00:00:00.000+01:00")
        )

        assertThat(result).isNotNull()
        assertThat(result.quantityOfFish).isEqualTo(6.0)
        assertThat(result.nbrOfHourAtSea).isEqualTo(3.0)
        assertThat(result.nbrOfInfraction).isEqualTo(4.0)
        assertThat(result.nbrOfPolFishAction).isEqualTo(2.0)
        assertThat(result.nbrOfInfractionWithPV).isEqualTo(3.0)
    }

    @Test
    fun `getNbrOfHourAtSea should filter LAND_CONTROL and SEA_CONTROL and handle null cases`() {
        val actions = listOf(
            null, // null action -> filtered
            MissionFishActionEntity( // SEA_CONTROL 2h -> included
                missionId = 761, id = 1,
                fishActionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                actionEndDatetimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
            ),
            MissionFishActionEntity( // LAND_CONTROL 1h -> included
                missionId = 761, id = 2,
                fishActionType = MissionActionType.LAND_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T03:00:00.000+01:00"),
                actionEndDatetimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
            ),
            MissionFishActionEntity( // AIR_CONTROL 3h -> NOT included
                missionId = 761, id = 3,
                fishActionType = MissionActionType.AIR_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T05:00:00.000+01:00"),
                actionEndDatetimeUtc = Instant.parse("2019-09-09T08:00:00.000+01:00"),
            ),
            MissionFishActionEntity( // SEA_CONTROL null dates -> 0h
                missionId = 761, id = 4,
                fishActionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                actionEndDatetimeUtc = null,
            ),
            MissionFishActionEntity( // SEA_CONTROL null start -> 0h
                missionId = 761, id = 5,
                fishActionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = null,
                actionEndDatetimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
            )
        )
        // SEA_CONTROL(2h) + LAND_CONTROL(1h) + null dates(0h) = 3h
        val result = AEMIllegalFish.getNbrOfHourAtSea(actions, emptyList(), null)
        assertThat(result).isEqualTo(3.0)
    }

    @Test
    fun `helper methods should handle null actions and filter correctly`() {
        val actions = listOf(
            null, // null action -> filtered out by all methods
            MissionFishActionEntity(
                missionId = 761, id = 1,
                fishActionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                speciesQuantitySeized = 5,
                seizureAndDiversion = true,
                fishInfractions = listOf(
                    FishInfraction(natinf = 1, infractionType = InfractionType.WITH_RECORD), // infraction: yes, withPV: yes
                    FishInfraction(natinf = null, infractionType = InfractionType.WITH_RECORD), // infraction: no (null natinf), withPV: yes
                    FishInfraction(natinf = 2, infractionType = InfractionType.WITHOUT_RECORD), // infraction: yes, withPV: no
                    FishInfraction(natinf = 3, infractionType = InfractionType.PENDING), // infraction: yes, withPV: no
                ),
            ),
            MissionFishActionEntity(
                missionId = 761, id = 2,
                fishActionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                speciesQuantitySeized = null, // uses 0
                seizureAndDiversion = false, // doesn't count
                fishInfractions = listOf(
                    FishInfraction(natinf = 4, infractionType = InfractionType.WITH_RECORD), // infraction: yes, withPV: yes
                ),
            ),
            MissionFishActionEntity(
                missionId = 761, id = 3,
                fishActionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                speciesQuantitySeized = 3,
                seizureAndDiversion = null, // doesn't count
            ),
            MissionFishActionEntity(
                missionId = 761, id = 4,
                fishActionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                seizureAndDiversion = true, // counts
            )
        )

        // getNbrOfInfraction: natinf != null -> 1 + 2 + 3 + 4 = 4 infractions
        assertThat(AEMIllegalFish.getNbrOfInfraction(actions)).isEqualTo(4.0)

        // getNbrOfInfractionWithPV: WITH_RECORD -> 2 (action1) + 1 (action2) = 3
        assertThat(AEMIllegalFish.getNbrOfInfractionWithPV(actions)).isEqualTo(3.0)

        // getQuantityOfFish: 5 + 0 + 3 + 0 = 8
        assertThat(AEMIllegalFish.getQuantityOfFish(actions)).isEqualTo(8.0)

        // getNbrOfSeizureAndDiversionVessel: true only -> 2 (action1 + action4)
        assertThat(AEMIllegalFish.getNbrOfSeizureAndDiversionVessel(actions)).isEqualTo(2.0)
    }

    @Test
    fun `should filter only SEA_CONTROL for nbrOfPolFishAction`() {
        val actions = listOf(
            null,
            MissionFishActionEntity(missionId = 761, id = 1, fishActionType = MissionActionType.LAND_CONTROL, actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00")),
            MissionFishActionEntity(missionId = 761, id = 2, fishActionType = MissionActionType.AIR_CONTROL, actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00")),
            MissionFishActionEntity(missionId = 761, id = 3, fishActionType = MissionActionType.SEA_CONTROL, actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00")),
            MissionFishActionEntity(missionId = 761, id = 4, fishActionType = MissionActionType.SEA_CONTROL, actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00")),
        )
        val result = AEMIllegalFish(fishActions = actions, navActions = emptyList(), missionEndDateTime = null)

        assertThat(result.nbrOfPolFishAction).isEqualTo(2.0) // Only SEA_CONTROL
        assertThat(result.nbrOfTargetedVessel).isEqualTo(2.0)
    }

    @Test
    fun `should return zeros for empty action list`() {
        val result = AEMIllegalFish(fishActions = emptyList(), navActions = emptyList(), missionEndDateTime = null)

        assertThat(result.nbrOfHourAtSea).isEqualTo(0.0)
        assertThat(result.nbrOfPolFishAction).isEqualTo(0.0)
        assertThat(result.nbrOfTargetedVessel).isEqualTo(0.0)
        assertThat(result.nbrOfInfraction).isEqualTo(0.0)
        assertThat(result.nbrOfInfractionWithPV).isEqualTo(0.0)
        assertThat(result.nbrOfSeizureAndDiversionVessel).isEqualTo(0.0)
        assertThat(result.quantityOfFish).isEqualTo(0.0)
    }
}