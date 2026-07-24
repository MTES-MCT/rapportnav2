package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ComputeActionValidityAndRecomputeMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.RecomputeMissionValidation
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionEnvActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.UUID

class ComputeActionValidityAndRecomputeMissionTest {

    private val getMissionDates: GetMissionDates = mock()
    private val validator: EntityValidityValidator = mock()
    private val recomputeMissionValidation: RecomputeMissionValidation = mock()
    private val useCase = ComputeActionValidityAndRecomputeMission(getMissionDates, validator, recomputeMissionValidation)

    @BeforeEach
    fun setup() {
        whenever(validator.validateCompleteness(any(), any()))
            .thenReturn(CompletenessForStatsEntity(status = CompletenessForStatsStatusEnum.VALID))
        whenever(validator.validateCompletenessWithSource(any(), any(), any()))
            .thenReturn(CompletenessForStatsEntity(status = CompletenessForStatsStatusEnum.VALID))
    }

    @Test
    fun `computes the action validity and recomputes the nav mission`() {
        val ownerId = UUID.randomUUID()
        val action = MissionNavActionEntityMock.create(id = UUID.randomUUID())

        useCase.execute(action, ownerId)

        // validity was computed on the action (feeds the nav mission_action row's isCompleteForStats)
        assertThat(action.isCompleteForStats).isTrue()
        verify(recomputeMissionValidation).forNavMission(ownerId)
        verify(recomputeMissionValidation, never()).forEnvMission(anyOrNull())
    }

    @Test
    fun `routes the recompute to the env mission for env-sourced actions`() {
        val action = MissionEnvActionEntityMock.create(
            id = UUID.randomUUID(), missionId = 42, completion = ActionCompletionEnum.COMPLETED
        )

        useCase.execute(action, ownerId = null)

        verify(recomputeMissionValidation).forEnvMission(eq(42))
        verify(recomputeMissionValidation, never()).forNavMission(anyOrNull())
    }
}
