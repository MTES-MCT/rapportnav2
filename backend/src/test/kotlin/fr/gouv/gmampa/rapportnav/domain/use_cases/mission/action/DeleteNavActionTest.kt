package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.DeleteNavAction
import org.assertj.core.api.Assertions.assertThatNoException
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import java.util.*


@SpringBootTest(classes = [DeleteNavAction::class])
@ContextConfiguration(classes = [DeleteNavAction::class])
class DeleteNavActionTest {
    @MockBean
    private lateinit var missionActionRepository: INavMissionActionRepository

    @Test
    fun `test execute delete nav action`() {
        val actionId =  UUID.randomUUID()
        doNothing().`when`(missionActionRepository).deleteById(actionId)

        val deleteNavAction = DeleteNavAction(
            missionActionRepository = missionActionRepository
        )
        deleteNavAction.execute(actionId)
        assertThatNoException()
    }
}
