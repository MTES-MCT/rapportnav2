package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.EnableSati
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
    private lateinit var enableSati: EnableSati

    @MockitoBean
    private lateinit var getComputeTarget: GetComputeTarget

    @Test
    fun `test execute process target not create new target when model exist`() {
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetEntityMock.create(actionId = actionId)

        //Mock
        val response = listOf(target1.toTargetModel())
        `when`(targetRepo.findByActionId(actionId)).thenReturn(response)
        `when`(targetRepo.save(anyOrNull())).thenReturn(target1.toTargetModel())

        //When
        getComputeTarget = Mockito.spy(GetComputeTarget(enableSati, targetRepo))
        val targets = getComputeTarget.execute(actionId, isControl = true)

        //Then
        assertThat(targets).isNotNull
        assertThat(targets?.get(0)?.actionId).isEqualTo(actionId)
    }

    @Test
    fun `test execute process should create new target if there is not model`() {
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetEntityMock.create(actionId = actionId)

        //Mock
        `when`(targetRepo.findByActionId(actionId)).thenReturn(listOf())
        `when`(targetRepo.save(anyOrNull())).thenReturn(target1.toTargetModel())

        //When
        getComputeTarget = Mockito.spy(GetComputeTarget(enableSati, targetRepo))
        val targets = getComputeTarget.execute(actionId, isControl = true)

        //Then
        assertThat(targets).isNotNull
        assertThat(targets?.get(0)?.actionId).isEqualTo(actionId)
    }

    @Test
    fun `test execute should not update target source when rnip is disabled`() {
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetEntityMock.create(
            actionId = actionId,
            targetType = TargetType.DEFAULT,
            source = MissionSourceEnum.MONITORENV
        )

        //Mock
        `when`(targetRepo.findByActionId(actionId)).thenReturn(listOf(target1.toTargetModel()))
        `when`(enableSati.isRnipEnabled()).thenReturn(false)

        //When
        getComputeTarget = Mockito.spy(GetComputeTarget(enableSati, targetRepo))
        val targets = getComputeTarget.execute(actionId, isControl = true, source = MissionSourceEnum.MONITORFISH)

        //Then
        assertThat(targets?.get(0)?.source).isEqualTo(MissionSourceEnum.MONITORENV)
        verify(targetRepo, never()).save(anyOrNull())
    }

    @Test
    fun `test execute should update target source when rnip enabled and target type is default`() {
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetEntityMock.create(
            actionId = actionId,
            targetType = TargetType.DEFAULT,
            source = MissionSourceEnum.MONITORENV
        )

        //Mock
        `when`(targetRepo.findByActionId(actionId)).thenReturn(listOf(target1.toTargetModel()))
        `when`(enableSati.isRnipEnabled()).thenReturn(true)
        `when`(targetRepo.save(anyOrNull())).thenAnswer { it.arguments[0] }

        //When
        getComputeTarget = Mockito.spy(GetComputeTarget(enableSati, targetRepo))
        val targets = getComputeTarget.execute(actionId, isControl = true, source = MissionSourceEnum.MONITORFISH)

        //Then
        assertThat(targets?.get(0)?.source).isEqualTo(MissionSourceEnum.MONITORFISH)
        verify(targetRepo).save(anyOrNull())
    }

    @Test
    fun `test execute should not update target source when it already matches requested source`() {
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetEntityMock.create(
            actionId = actionId,
            targetType = TargetType.DEFAULT,
            source = MissionSourceEnum.MONITORFISH
        )

        //Mock
        `when`(targetRepo.findByActionId(actionId)).thenReturn(listOf(target1.toTargetModel()))
        `when`(enableSati.isRnipEnabled()).thenReturn(true)

        //When
        getComputeTarget = Mockito.spy(GetComputeTarget(enableSati, targetRepo))
        val targets = getComputeTarget.execute(actionId, isControl = true, source = MissionSourceEnum.MONITORFISH)

        //Then
        assertThat(targets?.get(0)?.source).isEqualTo(MissionSourceEnum.MONITORFISH)
        verify(targetRepo, never()).save(anyOrNull())
    }

    @Test
    fun `test execute should not update target source when target type is not default`() {
        val actionId = UUID.randomUUID().toString()
        val target1 = TargetEntityMock.create(
            actionId = actionId,
            targetType = TargetType.COMPANY,
            source = MissionSourceEnum.MONITORENV
        )

        //Mock
        `when`(targetRepo.findByActionId(actionId)).thenReturn(listOf(target1.toTargetModel()))
        `when`(enableSati.isRnipEnabled()).thenReturn(true)

        //When
        getComputeTarget = Mockito.spy(GetComputeTarget(enableSati, targetRepo))
        val targets = getComputeTarget.execute(actionId, isControl = true, source = MissionSourceEnum.MONITORFISH)

        //Then
        assertThat(targets?.get(0)?.source).isEqualTo(MissionSourceEnum.MONITORENV)
        verify(targetRepo, never()).save(anyOrNull())
    }
}
