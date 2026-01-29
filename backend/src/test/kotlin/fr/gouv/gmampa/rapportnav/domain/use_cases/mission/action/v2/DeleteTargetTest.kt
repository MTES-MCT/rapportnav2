package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*


@SpringBootTest(classes = [DeleteTarget::class])
@ContextConfiguration(classes = [DeleteTarget::class])
class DeleteTargetTest {

    @Autowired
    private lateinit var deleteTarget: DeleteTarget

    @MockitoBean
    private lateinit var targetRepo: ITargetRepository

    @Test
    fun `should not call repository when type is not CONTROL or id is null`() {
        val actionId = UUID.randomUUID()
        deleteTarget = DeleteTarget(targetRepo = targetRepo)
        deleteTarget.execute(actionId  = null, actionType = ActionType.INQUIRY)
        verify(targetRepo, times(0)).deleteById(actionId)

        deleteTarget.execute(actionId  = actionId, actionType = ActionType.CONTROL)
        verify(targetRepo, times(0)).deleteById(actionId)
    }


    @Test
    fun `should call delete of repository `() {
        val actionId = UUID.randomUUID()
        val actionType = ActionType.CONTROL
        deleteTarget = DeleteTarget(targetRepo = targetRepo)
        deleteTarget.execute(actionId  = actionId, actionType = actionType)
        verify(targetRepo, times(1)).deleteByActionId(actionId.toString())
    }

    @Test
    fun `should throw BackendInternalException when repository fails`() {
        val actionId = UUID.randomUUID()
        val actionType = ActionType.CONTROL
        val originalException = RuntimeException("Database error")

        `when`(targetRepo.deleteByActionId(actionId.toString())).thenAnswer { throw originalException }

        deleteTarget = DeleteTarget(targetRepo = targetRepo)

        val exception = assertThrows<BackendInternalException> {
            deleteTarget.execute(actionId = actionId, actionType = actionType)
        }
        assertThat(exception.message).contains("DeleteTarget failed for actionId=$actionId")
    }
}
