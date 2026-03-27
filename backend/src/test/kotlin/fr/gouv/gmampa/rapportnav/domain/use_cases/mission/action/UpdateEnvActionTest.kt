package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.UpdateEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.MissionDatesOutput
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvActionData
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [UpdateEnvAction::class])
@ContextConfiguration(classes = [UpdateEnvAction::class])
class UpdateEnvActionTest {

    @MockitoBean
    private lateinit var patchEnvAction: PatchEnvAction

    @MockitoBean
    private lateinit var processMissionActionTarget: ProcessMissionActionTarget

    @MockitoBean
    private lateinit var entityValidityValidator: EntityValidityValidator

    @MockitoBean
    private lateinit var getMissionDates: GetMissionDates

    private val validResult = CompletenessForStatsEntity(
        status = CompletenessForStatsStatusEnum.COMPLETE,
        errors = emptyList()
    )

    @Test
    fun `test execute update env action`() {
        val actionId = UUID.randomUUID().toString()
        val input = MissionEnvAction(
            id = actionId,
            missionId = 761,
            actionType = ActionType.CONTROL,
            source = MissionSourceEnum.RAPPORT_NAV,
            data = getEnvActionData(),
        )

        `when`(entityValidityValidator.validate(any(), eq(ValidateThrowsBeforeSave::class.java))).thenReturn(validResult)
        `when`(patchEnvAction.execute(anyOrNull())).thenReturn(null)
        `when`(
            processMissionActionTarget.execute(
                anyOrNull(),
                anyOrNull()
            )
        ).thenReturn(listOf(TargetEntityMock.create()))
        `when`(getMissionDates.execute(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(
            MissionDatesOutput(
                startDateTimeUtc = Instant.parse("2019-09-01T00:00:00Z"),
                endDateTimeUtc = Instant.parse("2019-09-10T00:00:00Z")
            )
        )

        val updateEnvAction = UpdateEnvAction(
            patchEnvAction = patchEnvAction,
            processMissionActionTarget = processMissionActionTarget,
            entityValidityValidator = entityValidityValidator,
            getMissionDates = getMissionDates
        )

        val response = updateEnvAction.execute(actionId, input)
        assertThat(response).isNotNull
    }

    private fun getEnvActionData() = MissionEnvActionData(
        observations = "MyObservations",
        startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
    )
}
