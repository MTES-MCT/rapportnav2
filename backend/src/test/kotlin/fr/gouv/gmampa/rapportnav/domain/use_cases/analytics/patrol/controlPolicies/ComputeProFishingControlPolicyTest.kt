package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics.patrol.controlPolicies

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FishInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.controlPolicies.ComputeProFishingControlPolicy
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.helpers.CountInfractions
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionFishActionEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyNoInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ComputeProFishingControlPolicy::class])
class ComputeProFishingControlPolicyTest {
    @Autowired
    private lateinit var useCase: ComputeProFishingControlPolicy

    private val countInfractions: CountInfractions = mock()

    @BeforeEach
    fun setup() {
        useCase = ComputeProFishingControlPolicy()
    }

    @Test
    fun `returns null when mission is null`() {
        val result = useCase.computeFishingRelatedInfractions(null)
        assertNull(result)
        verifyNoInteractions(countInfractions)
    }

    @Test
    fun `returns also null when mission is null`() {
        val result = useCase.computeOtherInfractions(null)
        assertNull(result)
        verifyNoInteractions(countInfractions)
    }

    @Test
    fun `returns empty ControlPolicyData when no fish control actions`() {
        val mission = MissionEntity2(id = 123, actions = emptyList())

        val result = useCase.computeFishingRelatedInfractions(mission)

        assertNotNull(result)
        assertEquals(result.nbControls, 0)
        assertEquals(result.nbControlsSea, 0)
        assertEquals(result.nbControlsLand, 0)
        assertEquals(result.nbInfractionsWithRecord, 0)
        assertEquals(result.nbInfractionsWithoutRecord, 0)
        verifyNoInteractions(countInfractions)
    }

    @Test
    fun `returns empty ControlPolicyData when no fish control actions for other infractions`() {
        val mission = MissionEntity2(id = 123, actions = emptyList())

        val result = useCase.computeOtherInfractions(mission)

        assertNotNull(result)
        assertEquals(result.nbControls, 0)
        assertEquals(result.nbControlsSea, 0)
        assertEquals(result.nbControlsLand, 0)
        assertEquals(result.nbInfractionsWithRecord, 0)
        assertEquals(result.nbInfractionsWithoutRecord, 0)
        verifyNoInteractions(countInfractions)
    }

    @Test
    fun `computes counts for SEA and LAND fishing related controls`() {

        val seaAction = MissionFishActionEntityMock.create(
            fishActionType = MissionActionType.SEA_CONTROL,
            fishInfractions = listOf(
                FishInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 1),
                FishInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 1),
                FishInfraction(infractionType = InfractionType.WITHOUT_RECORD, natinf = 1),
                FishInfraction(infractionType = InfractionType.WITHOUT_RECORD, natinf = 1), // doesn't count
            ),
        )
        val landAction = MissionFishActionEntityMock.create(
            fishActionType = MissionActionType.LAND_CONTROL,
            fishInfractions = listOf(FishInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 1)),
        )
        val airAction = MissionFishActionEntityMock.create(
            fishActionType = MissionActionType.AIR_CONTROL,
            fishInfractions = listOf(
                FishInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 1),
                FishInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 1)
            ),
        )
        val mission = MissionEntity2(id = 123, actions = listOf(seaAction, landAction, airAction))


        val result = useCase.computeFishingRelatedInfractions(mission)

        assertNotNull(result)
        assertEquals(2, result.nbControls)
        assertEquals(1, result.nbControlsSea)
        assertEquals(1, result.nbControlsLand)
        assertEquals(3, result.nbInfractionsWithRecord)
        assertEquals(2, result.nbInfractionsWithoutRecord)
    }

}
