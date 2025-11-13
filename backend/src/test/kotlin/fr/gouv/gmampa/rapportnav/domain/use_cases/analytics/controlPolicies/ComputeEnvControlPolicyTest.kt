package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics.controlPolicies

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.controlPolicies.ComputeEnvControlPolicy
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.helpers.CountInfractions
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionEnvActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.InfractionEntity2Mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyNoInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ComputeEnvControlPolicy::class])
class ComputeEnvControlPolicyTest {
    @Autowired
    private lateinit var useCase: ComputeEnvControlPolicy

    private val countInfractions: CountInfractions = mock()

    @BeforeEach
    fun setup() {
        useCase = ComputeEnvControlPolicy()
    }

    @Test
    fun `returns null when mission is null`() {
        val result = useCase.execute(null)
        assertNull(result)
    }

    @Test
    fun `returns empty summary when no actions`() {
        val mission = MissionEntity2(id = 123, actions = emptyList())

        val result = useCase.execute(mission)

        assertNotNull(result)
        assertEquals(0, result.nbControls)
        assertEquals(0, result.nbInfractionsWithRecord)
        assertEquals(0, result.nbInfractionsWithoutRecord)
        verifyNoInteractions(countInfractions)
    }

    @Test
    fun `returns summary with control actions only`() {
        val controlAction = MissionEnvActionEntityMock.create(
            envActionType = ActionTypeEnum.CONTROL,
            targets = listOf(
                TargetMissionMock.create(
                    source = MissionSourceEnum.MONITORENV,
                    controls = listOf(
                        ControlMock.create(
                            amountOfControls = 3,
                            hasBeenDone = true,
                            controlType = ControlType.SECURITY,
                            infractions = listOf(
                                InfractionEntity2Mock.create(
                                    infractionType = InfractionTypeEnum.WITH_REPORT,
                                    natinfs = listOf()
                                )
                            )
                        ),
                        ControlMock.create( // not counted
                            amountOfControls = 3,
                            hasBeenDone = false,
                            controlType = ControlType.SECURITY,
                        ),
                        ControlMock.create(
                            amountOfControls = 6,
                            hasBeenDone = true,
                            controlType = ControlType.NAVIGATION,
                            infractions = listOf(
                                InfractionEntity2Mock.create(
                                    infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                                    natinfs = listOf()
                                )
                            )
                        )
                    )
                ),
                TargetMissionMock.create( // not counted
                    source = MissionSourceEnum.RAPPORTNAV,
                    controls = listOf(
                        ControlMock.create(
                            amountOfControls = 3,
                            controlType = ControlType.SECURITY,
                            infractions = listOf(
                                InfractionEntity2Mock.create(
                                    infractionType = InfractionTypeEnum.WITH_REPORT,
                                    natinfs = listOf()
                                )
                            )
                        )
                    )
                )
            )
        )
        val otherAction = MissionEnvActionEntityMock.create(envActionType = ActionTypeEnum.SURVEILLANCE)

        val mission = MissionEntity2(id = 123, actions = listOf(controlAction, otherAction))

        val result = useCase.execute(mission)

        assertNotNull(result)
        assertEquals(1, result.nbControls)
        assertEquals(1, result.nbInfractionsWithRecord)
        assertEquals(1, result.nbInfractionsWithoutRecord)
    }
}
