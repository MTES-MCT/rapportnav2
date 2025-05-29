package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ICrossControlRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessActionCrossControl
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.CrossControlModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [ProcessActionCrossControl::class])
@ContextConfiguration(classes = [ProcessActionCrossControl::class])
class ProcessActionCrossControlTest {

    @MockitoBean
    private lateinit var crossControlRepo: ICrossControlRepository

    @Autowired
    private lateinit var processActionCrossControl: ProcessActionCrossControl

    @Test
    fun `should return null for if cross control is null`() {
        //When
        processActionCrossControl = ProcessActionCrossControl(crossControlRepo = crossControlRepo)
        val crossControl = processActionCrossControl.execute(null)
        //Then
        assertThat(crossControl).isNull()
    }

    @Test
    fun `should save in db if no crossControl exists already`() {
        val controlControl = CrossControlEntity(
            id = null,
            endDateTimeUtc = Instant.parse("2015-07-30T00:00:00.00Z"),
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z")
        )

        val model = controlControl.toCrossControlModel()

        //Mock
        `when`(crossControlRepo.save(anyOrNull())).thenReturn(model)

        //When
        processActionCrossControl = ProcessActionCrossControl(crossControlRepo = crossControlRepo)
        val crossControl = processActionCrossControl.execute(controlControl)

        //Then
        assertThat(crossControl).isNotNull()
        verify(crossControlRepo, times(1)).save(anyOrNull())
    }

    @Test
    fun `should save in db only data no conclusion`() {
        val crossControlId = UUID.randomUUID()
        val controlControl = CrossControlEntity(
            id = crossControlId,
            type = "",
            agentId = 67,
            vesselId = 4556,
            sumNbrOfHours = 45,
            endDateTimeUtc = Instant.parse("2015-07-30T00:00:00.00Z"),
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z"),
            origin = CrossControlOriginType.FOLLOW_UP_CONTROL,
            status = CrossControlStatusType.NEW,
            conclusion = CrossControlConclusionType.NO_FOLLOW_UP,
        )
        val response = CrossControlModel(
            id = crossControlId,
            type = "My type",
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z")
        )

        //Mock
        `when`(crossControlRepo.save(anyOrNull())).thenReturn(response)
        `when`(crossControlRepo.findById(crossControlId)).thenReturn(Optional.of(response))

        //When
        processActionCrossControl = ProcessActionCrossControl(crossControlRepo = crossControlRepo)
        val crossControl = processActionCrossControl.execute(controlControl)

        //Then
        assertThat(crossControl).isNotNull
        verify(crossControlRepo, times(1)).save(
            CrossControlEntity.fromCrossControlModel(response).toModelSetData(controlControl))
    }

    @Test
    fun `should save in db only conclusion no data`() {
        val crossControlId = UUID.randomUUID()
        val controlControl = CrossControlEntity(
            id = crossControlId,
            type = "",
            agentId = 67,
            vesselId = 4556,
            sumNbrOfHours = 45,
            endDateTimeUtc = Instant.parse("2015-07-30T00:00:00.00Z"),
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z"),
            origin = CrossControlOriginType.FOLLOW_UP_CONTROL,
            status = CrossControlStatusType.FOLLOW_UP,
            conclusion = CrossControlConclusionType.NO_FOLLOW_UP,
        )
        val response = CrossControlModel(
            id = crossControlId,
            type = "My type",
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z")
        )

        //Mock
        `when`(crossControlRepo.save(anyOrNull())).thenReturn(response)
        `when`(crossControlRepo.findById(crossControlId)).thenReturn(Optional.of(response))

        //When
        processActionCrossControl = ProcessActionCrossControl(crossControlRepo = crossControlRepo)
        val crossControl = processActionCrossControl.execute(controlControl)

        //Then
        assertThat(crossControl).isNotNull
        verify(crossControlRepo, times(1)).save(
            CrossControlEntity.fromCrossControlModel(response).toModelSetConclusion(controlControl))
    }

}
