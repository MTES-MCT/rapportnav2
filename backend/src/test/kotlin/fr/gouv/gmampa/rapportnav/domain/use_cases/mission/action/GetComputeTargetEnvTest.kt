package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeEnvTarget
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*


@SpringBootTest(classes = [GetComputeEnvTarget::class])
@ContextConfiguration(classes = [GetComputeEnvTarget::class])
class GetComputeTargetEnvTest {

    @MockitoBean
    private lateinit var targetRepo: ITargetRepository

    @MockitoBean
    private lateinit var getComputeEnvTarget: GetComputeEnvTarget

    private fun envInfractions(externalId: String) = listOf(
        InfractionEnvEntity(
            id = externalId,
            formalNotice = FormalNoticeEnum.NO,
            infractionType = InfractionTypeEnum.WITH_REPORT,
        )
    )

    @Test
    fun `returns the existing env target without writing`() {
        val externalId = "myExternalId"
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetEntityMock.create(actionId = actionId)

        `when`(targetRepo.findByExternalId(externalId)).thenReturn(target1.toTargetModel())

        getComputeEnvTarget = Mockito.spy(GetComputeEnvTarget(targetRepo))
        val targets = getComputeEnvTarget.execute(actionId = actionId, envInfractions = envInfractions(externalId), isControl = true)

        assertThat(targets).isNotNull
        assertThat(targets?.get(0)?.actionId).isEqualTo(actionId)
        // read must not persist
        verify(targetRepo, never()).save(anyOrNull())
    }

    @Test
    fun `computes an in-memory env target without writing when none exists`() {
        val externalId = "myExternalId"
        val actionId = UUID.randomUUID().toString()

        `when`(targetRepo.findByExternalId(externalId)).thenReturn(null)

        getComputeEnvTarget = Mockito.spy(GetComputeEnvTarget(targetRepo))
        val first = getComputeEnvTarget.execute(actionId, envInfractions(externalId), isControl = true)
        val second = getComputeEnvTarget.execute(actionId, envInfractions(externalId), isControl = true)

        assertThat(first).isNotNull
        // read must not persist — the env target is materialized later, on the next update
        verify(targetRepo, never()).save(anyOrNull())
        // deterministic id derived from the MonitorEnv externalId — stable across reads
        val envTarget1 = first?.firstOrNull { it.externalData?.id == externalId }
        val envTarget2 = second?.firstOrNull { it.externalData?.id == externalId }
        assertThat(envTarget1).isNotNull
        assertThat(envTarget1?.id).isEqualTo(envTarget2?.id)
    }
}
