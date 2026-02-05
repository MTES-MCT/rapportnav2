package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics.patrol.controlPolicies

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.controlPolicies.ComputeNavControlPolicy
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.helpers.CountInfractions
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionEnvActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionFishActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest(classes = [ComputeNavControlPolicy::class])
class ComputeNavControlPolicyTest {
    @Autowired
    private lateinit var useCase: ComputeNavControlPolicy

    private val mockCountInfractions: CountInfractions = mock()

    @BeforeEach
    fun setup() {
        useCase = ComputeNavControlPolicy()
    }

    @Test
    fun `returns null when mission is null`() {
        val result = useCase.execute(null, ControlType.NAVIGATION)
        assertNull(result)
    }

    @Test
    fun `returns null ControlPolicyData when mission has no actions`() {
        val mission = MissionEntity(id = 123, actions = emptyList())
        val result = useCase.execute(mission, ControlType.NAVIGATION)
        assertEquals(result?.nbControls ?: 0, 0)
        assertEquals(result?.nbControlsSea ?: 0, 0)
        assertEquals(result?.nbControlsLand ?: 0, 0)
        assertEquals(result?.nbInfractionsWithRecord ?: 0, 0)
        assertEquals(result?.nbInfractionsWithoutRecord ?: 0, 0)
    }

    @Test
    fun `filters NAV actions correctly and counts`() {
        // counts because amountOfControls > 0 + right controlType + infraction
        val control1 = ControlEntityMock.create(
            controlType = ControlType.NAVIGATION,
            amountOfControls = 2,
            hasBeenDone = true,
            infractions = listOf(
                InfractionEntity(UUID.randomUUID(), infractionType = InfractionTypeEnum.WITH_REPORT)
            )
        )
        // does not count because no amountOfControls
        val control2 = ControlEntityMock.create(
            controlType = ControlType.NAVIGATION,
            amountOfControls = 0,
            hasBeenDone = true
        )
        // does not count because wrong controlType
        val control3 = ControlEntityMock.create(
            controlType = ControlType.SECURITY,
            amountOfControls = 2,
            hasBeenDone = true,
            infractions = listOf(
                InfractionEntity(UUID.randomUUID(), infractionType = InfractionTypeEnum.WITH_REPORT)
            )
        )

        val target = TargetEntityMock.create(
            targetType = TargetType.VEHICLE,
            controls = listOf(control1, control2, control3)
        )

        val navAction1 = MissionNavActionEntityMock.create(
            actionType = ActionType.CONTROL,
            controlMethod = ControlMethod.SEA,
            targets = listOf(target)
        )
        val navAction2 = MissionNavActionEntityMock.create(
            actionType = ActionType.ANTI_POLLUTION,
            controlMethod = ControlMethod.SEA,
            targets = listOf(target)
        )

        val mission = MissionEntity(
            id = 123,
            actions = listOf(navAction1, navAction2)
        )

        val result = useCase.execute(mission, ControlType.NAVIGATION)

        assertNotNull(result)
        assertEquals(1, result.nbControls)
        assertEquals(1, result.nbControlsSea)
        assertEquals(0, result.nbControlsLand)
        assertTrue(result.nbInfractionsWithRecord >= 0)
    }

    @Test
    fun `filters ENV actions with valid controls`() {
        val control1 = ControlEntityMock.create(
            controlType = ControlType.NAVIGATION,
            amountOfControls = 1
        )

        val control2 = ControlEntityMock.create(
            controlType = ControlType.NAVIGATION,
            amountOfControls = 0
        )
        val control3 = ControlEntityMock.create(
            controlType = ControlType.SECURITY,
            amountOfControls = 1
        )

        val target = TargetEntityMock.create(
            targetType = TargetType.VEHICLE,
            controls = listOf(control1, control2, control3)
        )

        val envAction1 = MissionEnvActionEntityMock.create(
            envActionType = ActionTypeEnum.CONTROL,
            targets = listOf(target)
        )
        val envAction2 = MissionEnvActionEntityMock.create(
            envActionType = ActionTypeEnum.SURVEILLANCE,
            targets = listOf(target)
        )

        val mission = MissionEntityMock.create(
            actions = listOf(envAction1, envAction2)
        )

        val result = useCase.execute(mission, ControlType.NAVIGATION)

        assertNotNull(result)
        assertEquals(1, result.nbControls)
    }

    @Test
    fun `filters FISH actions with valid controls`() {
        val validControl = ControlEntityMock.create(
            controlType = ControlType.NAVIGATION,
            amountOfControls = 2
        )

        val invalidControl = ControlEntityMock.create(
            controlType = ControlType.GENS_DE_MER, // wrong type
            amountOfControls = 3
        )

        val validTarget = TargetEntityMock.create(
            targetType = TargetType.VEHICLE,
            controls = listOf(validControl)
        )

        val invalidTarget = TargetEntityMock.create(
            targetType = TargetType.VEHICLE,
            controls = listOf(invalidControl)
        )

        // This one should be included
        val validFishAction = MissionFishActionEntityMock.create(
            fishActionType = MissionActionType.SEA_CONTROL,
            targets = listOf(validTarget)
        )

        // This one should be excluded (wrong control type)
        val invalidFishAction = MissionFishActionEntityMock.create(
            fishActionType = MissionActionType.SEA_CONTROL,
            targets = listOf(invalidTarget)
        )

        // This one should be excluded (wrong action type)
        val unrelatedFishAction = MissionFishActionEntityMock.create(
            fishActionType = MissionActionType.OBSERVATION,
            targets = listOf(validTarget)
        )

        val mission = MissionEntity(
            id = 123,
            actions = listOf(validFishAction, invalidFishAction, unrelatedFishAction)
        )

        val result = useCase.execute(mission, ControlType.NAVIGATION)

        assertNotNull(result)
        assertEquals(1, result.nbControls)
        assertTrue(result.nbControlsSea!! >= 1)
        assertEquals(0, result.nbControlsLand)
    }

    @Test
    fun `handles invalid control safely without throwing`() {
        val brokenControl = mock<ControlEntity> {
            on { controlType } doThrow(RuntimeException("Test error"))
        }

        val target = TargetEntity(
            id = UUID.randomUUID(),
            actionId = "broken",
            targetType = TargetType.VEHICLE,
            controls = listOf(brokenControl)
        )

        val envAction = MissionEnvActionEntity(
            id = UUID.randomUUID(),
            missionId = 123,
            envActionType = ActionTypeEnum.CONTROL,
            targets = listOf(target)
        )

        val mission = MissionEntity(
            id = 123,
            actions = listOf(envAction)
        )

        assertDoesNotThrow {
            val result = useCase.execute(mission, ControlType.NAVIGATION)
            assertTrue(result?.nbControls == 0)
        }
    }
}
