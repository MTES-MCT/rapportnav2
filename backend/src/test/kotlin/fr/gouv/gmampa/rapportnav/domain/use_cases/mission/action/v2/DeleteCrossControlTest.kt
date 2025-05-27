package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ICrossControlRepository
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


@SpringBootTest(classes = [DeleteCrossControl::class])
@ContextConfiguration(classes = [DeleteCrossControl::class])
class DeleteCrossControlTest {

    @Autowired
    private lateinit var deleteCrossControl: DeleteCrossControl

    @MockitoBean
    private lateinit var crossControlRepo: ICrossControlRepository

    @Test
    fun `should not call repository when type is not CROSS CONTROL or id is null`() {
        val crossControlId = UUID.randomUUID()
        deleteCrossControl = DeleteCrossControl(crossControlRepo = crossControlRepo)
        deleteCrossControl.execute(crossControlId  = null, actionType = ActionType.CROSS_CONTROL)
        verify(crossControlRepo, times(0)).deleteById(crossControlId)

        deleteCrossControl.execute(crossControlId  = crossControlId, actionType = ActionType.CONTROL)
        verify(crossControlRepo, times(0)).deleteById(crossControlId)
    }


    @Test
    fun `should not call repository delete if status is Follow up`() {
        val crossControlId = UUID.randomUUID()
        val actionType = ActionType.CROSS_CONTROL

        val controlControl = CrossControlEntity(
            id = crossControlId,
            startDateTimeUtc = Instant.now(),
            status = CrossControlStatusType.FOLLOW_UP
        ).toCrossControlModel()

        `when`(crossControlRepo.findById(crossControlId)).thenReturn(Optional.of(controlControl))
        deleteCrossControl = DeleteCrossControl(crossControlRepo = crossControlRepo)
        deleteCrossControl.execute(crossControlId  = crossControlId, actionType = actionType)
        verify(crossControlRepo, times(0)).deleteById(crossControlId)
    }



    @Test
    fun `should call delete of repository `() {
        val crossControlId = UUID.randomUUID()
        val actionType = ActionType.CROSS_CONTROL
        val controlControl = CrossControlEntity(
            id = crossControlId,
            startDateTimeUtc = Instant.now(),
            status = CrossControlStatusType.NEW
        ).toCrossControlModel()
        `when`(crossControlRepo.findById(crossControlId)).thenReturn(Optional.of(controlControl))
        deleteCrossControl = DeleteCrossControl(crossControlRepo = crossControlRepo)
        deleteCrossControl.execute(crossControlId  = crossControlId, actionType = actionType)
        verify(crossControlRepo, times(1)).deleteById(crossControlId)
    }

}
