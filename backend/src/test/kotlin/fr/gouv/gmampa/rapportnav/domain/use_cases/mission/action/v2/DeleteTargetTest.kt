package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ICrossControlRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
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
        deleteTarget.execute(actionId  = null, actionType = ActionType.CROSS_CONTROL)
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

}
