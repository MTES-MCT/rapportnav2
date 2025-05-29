package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ICrossControlRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetCrossControlByServiceId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessCrossControl
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.CrossControlModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [GetCrossControlByServiceId::class])
@ContextConfiguration(classes = [GetCrossControlByServiceId::class])
class GetCrossControlByServiceIdTest {

    @MockitoBean
    private lateinit var crossControlRepo: ICrossControlRepository

    @MockitoBean
    private lateinit var processCrossControl: ProcessCrossControl

    @Autowired
    private lateinit var getCrossControlByServiceId: GetCrossControlByServiceId


    @Test
    fun `should return list of cross control of a specific service id`() {
        val crossControlId = UUID.randomUUID()
        val controlControls = listOf(
            CrossControlModel(
                id = crossControlId,
                serviceId = 6,
                startDateTimeUtc = Instant.now(),
                status = CrossControlStatusType.NEW.toString()
            ),
            CrossControlModel(
                id = crossControlId,
                serviceId = 6,
                startDateTimeUtc = Instant.now(),
                status = CrossControlStatusType.FOLLOW_UP.toString()
            ),
            CrossControlModel(
                id = crossControlId,
                serviceId = 6,
                startDateTimeUtc = Instant.now(),
                status = CrossControlStatusType.CLOSED.toString()
            )
        )

        //Mock
        `when`(crossControlRepo.findByServiceId(6)).thenReturn(controlControls)
        `when`(processCrossControl.execute(anyOrNull())).thenReturn(CrossControlEntity())


        //When
        getCrossControlByServiceId = GetCrossControlByServiceId(
            crossControlRepo = crossControlRepo,
            processCrossControl = processCrossControl
        )
        val response = getCrossControlByServiceId.execute(6)

        //Then
        assertThat(response).isNotNull()
        assertThat(response.size).isEqualTo(3)
    }
}
