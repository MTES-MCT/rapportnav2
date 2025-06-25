package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionCrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionActionCrossControl
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetMissionMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [ProcessNavAction::class])
@ContextConfiguration(classes = [ProcessNavAction::class])
class ProcessNavActionTest {

    @Autowired
    private lateinit var processNavAction: ProcessNavAction

    @MockitoBean
    private lateinit var getStatusForAction: GetStatusForAction2

    @MockitoBean
    private lateinit var getComputeTarget: GetComputeTarget

    @MockitoBean
    private lateinit var getComputeCrossControl: GetComputeCrossControl

    @Test
    fun `test execute get nav action by id`() {
        val missionId = 761
        val actionId = UUID.randomUUID()
        val crossControlId = UUID.randomUUID()
        val action = MissionActionModel(
            id = actionId,
            missionId = missionId,
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true,
            actionType = ActionType.CONTROL,
            crossControlId = crossControlId
        )

        val controlControl = CrossControlEntity(
            id = crossControlId
        )

        val mockTarget = TargetMissionMock.create()
        `when`(getComputeTarget.execute(actionId.toString(), true)).thenReturn(listOf(mockTarget))
        `when`(getComputeCrossControl.execute(crossControlId)).thenReturn(controlControl)
        processNavAction = ProcessNavAction(
            getComputeTarget = getComputeTarget,
            getStatusForAction = getStatusForAction,
            getComputeCrossControl = getComputeCrossControl
        )

        val entity = processNavAction.execute(action = action)
        val infractionIds = entity.getInfractions().map { it.id }.toSet()
        val mockInfractionIds = mockTarget.controls?.flatMap { it.infractions!! }?.map { it.id }?.toSet()
        assertThat(entity).isNotNull
        assertThat(entity.id).isEqualTo(actionId)
        assertThat(infractionIds).isEqualTo(mockInfractionIds)
        assertThat(entity.crossControl?.id).isEqualTo(crossControlId)
    }
}
