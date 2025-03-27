package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetMissionMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
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

    @Captor
    lateinit var deleteCaptor: ArgumentCaptor<List<TargetEntity2>>

    @Captor
    lateinit var saveCaptor: ArgumentCaptor<List<TargetEntity2>>

    @MockitoBean
    private lateinit var processMissionActionTarget: ProcessMissionActionTarget

    @Test
    fun `test execute process target`() {
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetMissionMock.create(actionId = actionId)
        val target2 = TargetMissionMock.create(actionId = actionId)
        val target3 = TargetMissionMock.create(actionId = actionId)

        //Mock
        val response = listOf(target1.toTargetModel(), target2.toTargetModel())
        `when`(targetRepo.findByActionId(actionId)).thenReturn(response)
        `when`(targetRepo.save(anyOrNull())).thenReturn(target3.toTargetModel())

        //When
        processMissionActionTarget = Mockito.spy(ProcessMissionActionTarget(targetRepo))
        val infractions = processMissionActionTarget.execute(actionId, listOf(target1, target3))
        verify(processMissionActionTarget).save(saveCaptor.capture())
        verify(processMissionActionTarget).delete(deleteCaptor.capture())


        //Then
        assertThat(infractions).isNotNull
        assertThat(2).isEqualTo(saveCaptor.value.size)
        assertThat(1).isEqualTo(deleteCaptor.value.size)
        assertThat(target2.id).isEqualTo(deleteCaptor.value.get(0).id)
    }
}
