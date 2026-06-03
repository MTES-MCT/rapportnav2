package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeSati
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeTarget
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessFishAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionDates
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import java.time.Instant
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*


@SpringBootTest(classes = [ProcessFishAction::class])
@ContextConfiguration(classes = [ProcessFishAction::class])
class ProcessFishActionTest {

    @Autowired
    private lateinit var processFishAction: ProcessFishAction

    @MockitoBean
    private lateinit var getStatusForAction: GetStatusForAction

    @MockitoBean
    private lateinit var getComputeTarget: GetComputeTarget

    @MockitoBean
    private lateinit var getMissionDates: GetMissionDates

    @MockitoBean
    private lateinit var getComputeSati: GetComputeSati

    @MockitoBean
    private lateinit var entityValidityValidator: EntityValidityValidator

    private val realValidator: EntityValidityValidator = EntityValidityValidator.createDefault()

    @Test
    fun `test execute get fish action by id`() {
        val missionId = 761
        val actionId = UUID.randomUUID()
        val action = FishActionControlMock.create(
            id = actionId.hashCode(),
        )

        val mockTarget = TargetEntityMock.create()
        `when`(getComputeTarget.execute(actionId.hashCode().toString(), true)).thenReturn(listOf(mockTarget))
        `when`(getMissionDates.execute(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(
            MissionDates(
                startDateTimeUtc = Instant.parse("2019-09-01T00:00:00Z"),
                endDateTimeUtc = Instant.parse("2019-09-10T00:00:00Z")
            )
        )
        processFishAction = ProcessFishAction(
            getComputeSati = getComputeSati,
            getComputeTarget = getComputeTarget,
            getMissionDates = getMissionDates,
            getStatusForAction = getStatusForAction,
            entityValidityValidator = realValidator
        )
        val entity = processFishAction.execute(missionId = missionId, action = action)
        val infractionIds = entity.getAllInfractions().map { it.id }.toSet()
        val mockInfractionIds = mockTarget.controls?.flatMap { it.infractions!! }?.map { it.id }?.toSet()
        assertThat(entity).isNotNull
        assertThat(entity.id).isEqualTo(actionId.hashCode())
        assertThat(infractionIds).isEqualTo(mockInfractionIds)
    }
}
