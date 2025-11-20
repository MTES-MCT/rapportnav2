package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeTarget
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntity2Mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
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
    fun `test execute process target not create new target when model exist`() {
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetEntity2Mock.create(actionId = actionId)

        //Mock
        val response = listOf(target1.toTargetModel())
        `when`(targetRepo.findByActionId(actionId)).thenReturn(response)
        `when`(targetRepo.save(anyOrNull())).thenReturn(target1.toTargetModel())

        //When
        getComputeTarget = Mockito.spy(GetComputeTarget(targetRepo))
        val targets = getComputeTarget.execute(actionId, isControl = true)

        //Then
        assertThat(targets).isNotNull
        assertThat(targets?.get(0)?.actionId).isEqualTo(actionId)
    }

    @Test
    fun `test execute process should create new target if there is not model`() {
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetEntity2Mock.create(actionId = actionId)

        //Mock
        `when`(targetRepo.findByActionId(actionId)).thenReturn(listOf())
        `when`(targetRepo.save(anyOrNull())).thenReturn(target1.toTargetModel())

        //When
        getComputeTarget = Mockito.spy(GetComputeTarget(targetRepo))
        val targets = getComputeTarget.execute(actionId, isControl = true)

        //Then
        assertThat(targets).isNotNull
        assertThat(targets?.get(0)?.actionId).isEqualTo(actionId)
    }
}
