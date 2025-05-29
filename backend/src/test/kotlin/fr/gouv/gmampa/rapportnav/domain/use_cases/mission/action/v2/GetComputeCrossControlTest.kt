package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ICrossControlRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeCrossControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessCrossControl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [GetComputeCrossControl::class])
@ContextConfiguration(classes = [GetComputeCrossControl::class])
class GetComputeCrossControlTest {

    @MockitoBean
    private lateinit var crossControlRepo: ICrossControlRepository

    @MockitoBean
    private lateinit var processCrossControl: ProcessCrossControl

    @Autowired
    private lateinit var getComputeCrossControl: GetComputeCrossControl


    @MockitoBean
    private lateinit var missionActionRepository: INavMissionActionRepository

    @Test
    fun `should return null for null cross control id`() {
        val crossControlId = UUID.randomUUID()
        val controlControl = CrossControlEntity(
            id = crossControlId,
            startDateTimeUtc = Instant.now(),
            status = CrossControlStatusType.NEW
        ).toCrossControlModel()

        //Mock
        `when`(crossControlRepo.findById(crossControlId)).thenReturn(Optional.of(controlControl))


        //When
        getComputeCrossControl = GetComputeCrossControl(
            crossControlRepo = crossControlRepo,
            processCrossControl = processCrossControl
        )
        val crossControl = getComputeCrossControl.execute(crossControlId = null)

        //Then
        assertThat(crossControl).isNull()
    }

    @Test
    fun `should process cross control when id is not null`() {
        val crossControlId = UUID.randomUUID()
        val controlControl = CrossControlEntity(
            id = crossControlId,
            startDateTimeUtc = Instant.now(),
            status = CrossControlStatusType.NEW
        ).toCrossControlModel()

        //Mock
        `when`(crossControlRepo.findById(crossControlId)).thenReturn(Optional.of(controlControl))


        //When
        getComputeCrossControl = GetComputeCrossControl(
            crossControlRepo = crossControlRepo,
            processCrossControl = processCrossControl
        )
        getComputeCrossControl.execute(crossControlId = crossControlId)
        verify(processCrossControl, times(1)).execute(controlControl)
    }
}
