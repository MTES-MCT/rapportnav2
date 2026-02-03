package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetModelMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.UUID

@SpringBootTest(classes = [ProcessMissionActionTarget::class])
class ProcessMissionActionTargetTest {
    @Autowired
    private lateinit var useCase: ProcessMissionActionTarget

    @MockitoBean
    private lateinit var repository: ITargetRepository

    @Test
    fun `no changes - nothing to save or delete`() {
        val actionId = "A123"

        val model = TargetEntityMock.create().toTargetModel()
        val entity = TargetEntity.fromTargetModel(model)

        whenever(repository.findByActionId(actionId)).thenReturn(listOf(model))
        whenever(repository.save(any())).thenAnswer { model }

        val result = useCase.execute(actionId, listOf(entity))

        assertEquals(1, result?.size ?: 0)
        assertEquals(entity.id, result?.get(0)?.id)
        assertEquals(entity.actionId, result?.get(0)?.actionId)
        verify(repository).findByActionId(actionId)
        verify(repository, never()).deleteById(any())
    }

    @Test
    fun `execute should detect new targets to save`() {
        val repo = mock<ITargetRepository>()
        val useCase = ProcessMissionActionTarget(repo)

        val dbTarget = TargetModelMock.create(actionId = "action1")
        val incoming = TargetEntityMock.create()

        whenever(repo.findByActionId("action1")).thenReturn(listOf(dbTarget))
        whenever(repo.save(any())).thenReturn(TargetModelMock.create())

        useCase.execute("action1", listOf(incoming))

        verify(repo).save(incoming.toTargetModel())
    }

    @Test
    fun `save should map saved targets`() {
        val repo = mock<ITargetRepository>()
        val useCase = ProcessMissionActionTarget(repo)

        val incoming = TargetEntityMock.create()
        whenever(repo.save(any())).thenReturn(TargetModelMock.create(id = UUID.fromString("f97ae22e-6949-4b67-8e79-40267c7c380e")))

        val result = useCase.save(listOf(incoming))

        assertEquals(UUID.fromString("f97ae22e-6949-4b67-8e79-40267c7c380e"), result?.first()?.id)
        verify(repo).save(incoming.toTargetModel())
    }

    @Test
    fun `execute should not save when target list is empty`() {
        val repo = mock<ITargetRepository>()
        val useCase = ProcessMissionActionTarget(repo)
        whenever(repo.findByActionId("action1")).thenReturn(listOf())
        useCase.save(emptyList())
        verify(repository, never()).save(any())
    }

    @Test
    fun `targets removed from input are deleted`() {
        val actionId = "A123"

        val dbModel1 = TargetModelMock.create(id = UUID.fromString("f97ae22e-6949-4b67-8e79-40267c7c380e"))
        val dbModel2 = TargetModelMock.create(id = UUID.fromString("64b02a4c-6f7f-449d-9db5-3fb662d4969e"))
        val dbEntity1 = TargetEntity.fromTargetModel(dbModel1)

        // Only T1 remains → T2 must be deleted
        whenever(repository.findByActionId(actionId)).thenReturn(listOf(dbModel1, dbModel2))

        whenever(repository.save(any())).thenAnswer { dbModel1 }

        val result = useCase.execute(actionId, listOf(dbEntity1))

        assertEquals(1, result?.size ?: 0)
        assertEquals(dbEntity1.id, result?.get(0)?.id)
        assertEquals(dbEntity1.actionId, result?.get(0)?.actionId)
        verify(repository).deleteById(UUID.fromString("64b02a4c-6f7f-449d-9db5-3fb662d4969e"))
        verify(repository).save(any())
    }

    @Test
    fun `save() correctly maps and persists targets`() {
        val model = TargetModelMock.create()
        val entity = TargetEntity.fromTargetModel(model)

        whenever(repository.save(any())).thenReturn(model)

        val result = useCase.save(listOf(entity))

        assertEquals(1, result?.size ?: 0)
        assertEquals(entity.id, result?.get(0)?.id)
        assertEquals(entity.actionId, result?.get(0)?.actionId)
        verify(repository).save(entity.toTargetModel())
    }

    @Test
    fun `delete() calls repository deleteById for each item`() {
        val t1 = TargetEntityMock.create(id = UUID.fromString("f97ae22e-6949-4b67-8e79-40267c7c380e"))
        val t2 = TargetEntityMock.create(id = UUID.fromString("64b02a4c-6f7f-449d-9db5-3fb662d4969e"))

        useCase.delete(listOf(t1, t2))

        verify(repository).deleteById(UUID.fromString("f97ae22e-6949-4b67-8e79-40267c7c380e"))
        verify(repository).deleteById(UUID.fromString("64b02a4c-6f7f-449d-9db5-3fb662d4969e"))
    }

    @Test
    fun `execute should not delete when target list is empty`() {
        val repo = mock<ITargetRepository>()
        val useCase = ProcessMissionActionTarget(repo)
        whenever(repo.findByActionId("action1")).thenReturn(listOf())
        useCase.execute("action1", emptyList())
        verify(repository, never()).deleteById(any())
    }
}
