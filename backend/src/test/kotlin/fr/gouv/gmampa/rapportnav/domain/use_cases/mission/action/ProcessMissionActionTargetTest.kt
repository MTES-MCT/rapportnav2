package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntity2Mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
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
    fun `test execute process target saves new and deletes removed`() {
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetEntity2Mock.create(actionId = actionId)
        val target2 = TargetEntity2Mock.create(actionId = actionId)
        val target3 = TargetEntity2Mock.create(actionId = actionId)

        // Mock repository - database has target1 and target2
        val response = listOf(target1.toTargetModel(), target2.toTargetModel())
        `when`(targetRepo.findByActionId(actionId)).thenReturn(response)
        `when`(targetRepo.save(anyOrNull())).thenReturn(target3.toTargetModel())

        val processMissionActionTarget = ProcessMissionActionTarget(targetRepo)

        // Execute with target1 and target3 (target2 removed, target3 added)
        val result = processMissionActionTarget.execute(actionId, listOf(target1, target3))

        // Verify repository interactions
        verify(targetRepo).findByActionId(actionId)
        verify(targetRepo).deleteById(target2.id) // target2 was removed
        verify(targetRepo).save(target3.toTargetModel()) // target3 is new

        // Assert result
        assertThat(result).isNotNull()
    }
}
