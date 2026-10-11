package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeTarget
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


@SpringBootTest(classes = [GetComputeTarget::class])
@ContextConfiguration(classes = [GetComputeTarget::class])
class GetComputeTargetTest {

    @MockitoBean
    private lateinit var targetRepo: ITargetRepository

    @MockitoBean
    private lateinit var getComputeTarget: GetComputeTarget

    @Test
    fun `returns the existing target without writing`() {
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetEntityMock.create(actionId = actionId)

        `when`(targetRepo.findByActionId(actionId)).thenReturn(listOf(target1.toTargetModel()))

        getComputeTarget = Mockito.spy(GetComputeTarget(targetRepo))
        val targets = getComputeTarget.execute(actionId, isControl = true)

        assertThat(targets).isNotNull
        assertThat(targets?.get(0)?.actionId).isEqualTo(actionId)
        // read must not persist
        verify(targetRepo, never()).save(anyOrNull())
    }

    @Test
    fun `computes an in-memory target without writing when none exists`() {
        val actionId = UUID.randomUUID().toString()

        `when`(targetRepo.findByActionId(actionId)).thenReturn(listOf())

        getComputeTarget = Mockito.spy(GetComputeTarget(targetRepo))
        val first = getComputeTarget.execute(actionId, isControl = true)
        val second = getComputeTarget.execute(actionId, isControl = true)

        assertThat(first).isNotNull
        assertThat(first?.get(0)?.actionId).isEqualTo(actionId)
        // read must not persist — the target is materialized later, on the next update
        verify(targetRepo, never()).save(anyOrNull())
        // deterministic id: stable across reads so a later PUT matches instead of churning
        assertThat(first?.get(0)?.id).isEqualTo(second?.get(0)?.id)
    }
}
