package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeEnvTarget
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetMissionMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
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

    @Test
    fun `test execute process target not create new env target when model exist`() {
        val externalId = "myExternalId"
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetMissionMock.create(actionId = actionId)
        val envInfractions = listOf(
            InfractionEntity(
                id = externalId,
                toProcess = false,
                formalNotice = FormalNoticeEnum.NO,
                infractionType = InfractionTypeEnum.WITH_REPORT,
            )
        )

        //Mock
        val response = target1.toTargetModel()
        `when`(targetRepo.findByExternalId(externalId)).thenReturn(response)
        `when`(targetRepo.save(anyOrNull())).thenReturn(target1.toTargetModel())

        //When
        getComputeEnvTarget = Mockito.spy(GetComputeEnvTarget(targetRepo))
        val targets = getComputeEnvTarget.execute(actionId = actionId, envInfractions = envInfractions, isControl = true)

        //Then
        assertThat(targets).isNotNull
        assertThat(targets?.get(0)?.actionId).isEqualTo(actionId)
    }

    @Test
    fun `test execute process should create new env target if there is not model`() {
        val externalId = "myExternalId"
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetMissionMock.create(actionId = actionId)
        val envInfractions = listOf(
            InfractionEntity(
                id = externalId,
                toProcess = false,
                formalNotice = FormalNoticeEnum.NO,
                infractionType = InfractionTypeEnum.WITH_REPORT,
            )
        )

        //Mock
        `when`(targetRepo.findByExternalId(actionId)).thenReturn(null)
        `when`(targetRepo.save(anyOrNull())).thenReturn(target1.toTargetModel())

        //When
        getComputeEnvTarget = Mockito.spy(GetComputeEnvTarget(targetRepo))
        val targets = getComputeEnvTarget.execute(actionId, envInfractions, isControl = true)

        //Then
        assertThat(targets).isNotNull
        assertThat(targets?.get(0)?.actionId).isEqualTo(actionId)
    }
}
