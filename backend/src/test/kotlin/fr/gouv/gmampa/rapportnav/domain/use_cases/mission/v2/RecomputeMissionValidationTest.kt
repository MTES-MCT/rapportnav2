package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeNavMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.RecomputeMissionValidation
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import java.util.UUID

class RecomputeMissionValidationTest {

    private val getComputeNavMission: GetComputeNavMission = mock()
    private val getComputeEnvMission: GetComputeEnvMission = mock()
    private val useCase = RecomputeMissionValidation(getComputeNavMission, getComputeEnvMission)

    @Test
    fun `forNavMission rebuilds the nav mission by ownerId`() {
        val ownerId = UUID.randomUUID()

        useCase.forNavMission(ownerId)

        // the write path forces a full compute (never the read shortcut)
        verify(getComputeNavMission).execute(eq(ownerId), anyOrNull(), eq(true))
    }

    @Test
    fun `forNavMission is a no-op when ownerId is null`() {
        useCase.forNavMission(null)

        verifyNoInteractions(getComputeNavMission)
    }

    @Test
    fun `forEnvMission rebuilds the env mission by external id`() {
        useCase.forEnvMission(42)

        verify(getComputeEnvMission).execute(eq(42), anyOrNull(), eq(true))
    }

    @Test
    fun `forEnvMission is a no-op when missionId is null`() {
        useCase.forEnvMission(null)

        verifyNoInteractions(getComputeEnvMission)
    }

    @Test
    fun `swallows errors so a recompute failure never breaks the action write`() {
        val ownerId = UUID.randomUUID()
        whenever(getComputeNavMission.execute(anyOrNull(), anyOrNull(), anyOrNull())).thenThrow(RuntimeException("boom"))

        assertDoesNotThrow { useCase.forNavMission(ownerId) }
    }
}
