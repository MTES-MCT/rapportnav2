package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.DeleteInquiry
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.DeleteNavAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.DeleteTarget
import org.assertj.core.api.Assertions.assertThatNoException
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doNothing
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*


@SpringBootTest(classes = [DeleteNavAction::class])
@ContextConfiguration(classes = [DeleteNavAction::class])
class DeleteNavActionTest {
    @MockitoBean
    private lateinit var missionActionRepository: INavMissionActionRepository

    @MockitoBean
    private lateinit var deleteTarget: DeleteTarget

    @MockitoBean
    private lateinit var  deleteInquiry: DeleteInquiry

    @Test
    fun `test execute delete nav action`() {
        val actionId = UUID.randomUUID()
        doNothing().`when`(missionActionRepository).deleteById(actionId)

        val deleteNavAction = DeleteNavAction(
            deleteTarget = deleteTarget,
            missionActionRepository = missionActionRepository
        )
        deleteNavAction.execute(actionId)
        assertThatNoException()
    }
}
