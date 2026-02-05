package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics.patrol.controlPolicies

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.controlPolicies.ComputeEnvControlPolicy
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.helpers.CountInfractions
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionEnvActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.InfractionEntityMock
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
        val mission = MissionEntity(id = 123, actions = emptyList())

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
                TargetEntityMock.create(
                    source = MissionSourceEnum.MONITORENV,
                    controls = listOf(
                        ControlEntityMock.create(
                            amountOfControls = 3,
                            hasBeenDone = true,
                            controlType = ControlType.SECURITY,
                            infractions = listOf(
                                InfractionEntityMock.create(
                                    infractionType = InfractionTypeEnum.WITH_REPORT,
                                    natinfs = listOf()
                                )
                            )
                        ),
                        ControlEntityMock.create( // not counted
                            amountOfControls = 3,
                            hasBeenDone = false,
                            controlType = ControlType.SECURITY,
                        ),
                        ControlEntityMock.create(
                            amountOfControls = 6,
                            hasBeenDone = true,
                            controlType = ControlType.NAVIGATION,
                            infractions = listOf(
                                InfractionEntityMock.create(
                                    infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                                    natinfs = listOf()
                                )
                            )
                        )
                    )
                ),
                TargetEntityMock.create( // not counted
                    source = MissionSourceEnum.RAPPORTNAV,
                    controls = listOf(
                        ControlEntityMock.create(
                            amountOfControls = 3,
                            controlType = ControlType.SECURITY,
                            infractions = listOf(
                                InfractionEntityMock.create(
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

        val mission = MissionEntity(id = 123, actions = listOf(controlAction, otherAction))

        val result = useCase.execute(mission)

        assertNotNull(result)
        assertEquals(1, result.nbControls)
        assertEquals(1, result.nbInfractionsWithRecord)
        assertEquals(1, result.nbInfractionsWithoutRecord)
    }
}
