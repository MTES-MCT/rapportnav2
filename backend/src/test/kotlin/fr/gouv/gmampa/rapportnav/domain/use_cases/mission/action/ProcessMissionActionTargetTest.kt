package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntity2Mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [ProcessMissionActionTarget::class])
@ContextConfiguration(classes = [ProcessMissionActionTarget::class])
class ProcessMissionActionTargetTest {

    @MockitoBean
    private lateinit var targetRepo: ITargetRepository

    @Test
    fun `test execute process target`() {
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetEntity2Mock.create(actionId = actionId)
        val target2 = TargetEntity2Mock.create(actionId = actionId)
        val target3 = TargetEntity2Mock.create(actionId = actionId)

        val deleteCaptor = ArgumentCaptor.forClass(List::class.java) as ArgumentCaptor<List<TargetEntity2>>
        val saveCaptor = ArgumentCaptor.forClass(List::class.java) as ArgumentCaptor<List<TargetEntity2>>

        // Mock repository
        val response = listOf(target1.toTargetModel(), target2.toTargetModel())
        `when`(targetRepo.findByActionId(actionId)).thenReturn(response)
        `when`(targetRepo.save(anyOrNull())).thenReturn(target3.toTargetModel())

        // Create spy on the actual class under test
        val processMissionActionTarget = Mockito.spy(ProcessMissionActionTarget(targetRepo))

        // Execute
        val infractions = processMissionActionTarget.execute(actionId, listOf(target1, target3))

        // Verify
        verify(processMissionActionTarget).save(saveCaptor.capture())
        verify(processMissionActionTarget).delete(deleteCaptor.capture())

        // Assert
        assertThat(infractions).isNotNull()
        assertThat(saveCaptor.value.size).isEqualTo(2)
        assertThat(deleteCaptor.value.size).isEqualTo(1)
        assertThat(deleteCaptor.value[0].id).isEqualTo(target2.id)
    }
}
