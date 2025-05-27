package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ICrossControlRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessActionCrossControl
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
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


@SpringBootTest(classes = [ProcessActionCrossControl::class])
@ContextConfiguration(classes = [ProcessActionCrossControl::class])
class ProcessActionCrossControlTest {

    @MockitoBean
    private lateinit var crossControlRepo: ICrossControlRepository

    @Autowired
    private lateinit var processActionCrossControl: ProcessActionCrossControl

    @Test
    fun `should return null for null cross control id`() {
        val missionId = 761
        val actionId = UUID.randomUUID()
        val crossControlId = UUID.randomUUID()
        val action = getMissionNavActionEntity(
            actionId =actionId,
            missionId = missionId,
            actionType = ActionType.CROSS_CONTROL
        )
        action.crossControl = null
        val controlControl = CrossControlEntity(
            id = crossControlId,
            startDateTimeUtc = Instant.now(),
            status = CrossControlStatusType.NEW
        ).toCrossControlModel()

        //Mock
        `when`(crossControlRepo.findById(crossControlId)).thenReturn(Optional.of(controlControl))

        //When
        processActionCrossControl = ProcessActionCrossControl(crossControlRepo = crossControlRepo)
        val crossControl = processActionCrossControl.execute(action)

        //Then
        assertThat(crossControl).isNull()
    }

    @Test
    fun `should return null for Type not CROSS CONTROL`() {
        val missionId = 761
        val actionId = UUID.randomUUID()
        val crossControlId = UUID.randomUUID()
        var action = getMissionNavActionEntity(
            actionId =actionId,
            missionId = missionId,
            actionType = ActionType.CONTROL,
            crossControlId = crossControlId
        )

        val controlControl = CrossControlEntity(
            id = crossControlId,
            startDateTimeUtc = Instant.now(),
            status = CrossControlStatusType.NEW
        ).toCrossControlModel()

        //Mock
        `when`(crossControlRepo.findById(crossControlId)).thenReturn(Optional.of(controlControl))

        //When
        processActionCrossControl = ProcessActionCrossControl(crossControlRepo = crossControlRepo)
        val crossControl = processActionCrossControl.execute(action)

        //Then
        assertThat(crossControl).isNull()
    }

    @Test
    fun `should no update for blank update ( id null and status follow up)`() {
        val missionId = 761
        val actionId = UUID.randomUUID()
        val crossControlId = UUID.randomUUID()
        val action = getMissionNavActionEntity(
            actionId = actionId,
            missionId = missionId,
            actionType = ActionType.CROSS_CONTROL,
            crossControlId = null,
           crossControlStatusType = CrossControlStatusType.FOLLOW_UP.toString()
        )

        val controlControl = CrossControlEntity(
            id = crossControlId,
            startDateTimeUtc = Instant.now(),
            status = CrossControlStatusType.NEW
        ).toCrossControlModel()

        //Mock
        `when`(crossControlRepo.findById(crossControlId)).thenReturn(Optional.of(controlControl))

        //When
        processActionCrossControl = ProcessActionCrossControl(crossControlRepo = crossControlRepo)
        val crossControl = processActionCrossControl.execute(action)

        //Then
        assertThat(crossControl).isNotNull
        assertThat(crossControl).isEqualTo(action.crossControl)
    }

    @Test
    fun `should return a response with update with status CLOSED `() {
        val missionId = 761
        val actionId = UUID.randomUUID()
        val crossControlId = UUID.randomUUID()
        val controlControl = CrossControlEntity(
            id = crossControlId,
            startDateTimeUtc = Instant.now(),
            status = CrossControlStatusType.FOLLOW_UP
        ).toCrossControlModel()
        val action = getMissionNavActionEntity(
            actionId =actionId,
            missionId = missionId,
            actionType = ActionType.CROSS_CONTROL,
            crossControlId = crossControlId,
            crossControlStatusType = CrossControlStatusType.CLOSED.toString()
        )
        //Mock
        `when`(crossControlRepo.findById(crossControlId)).thenReturn(Optional.of(controlControl))
        `when`(crossControlRepo.save(anyOrNull())).thenReturn(controlControl)

        //When
        processActionCrossControl = ProcessActionCrossControl(crossControlRepo = crossControlRepo)
        val crossControl = processActionCrossControl.execute(action)

        //Then
        assertThat(crossControl).isNotNull()
    }

    private fun getMissionNavActionEntity(
        actionId : UUID,
        missionId : Int,
        actionType: ActionType,
        crossControlId : UUID? = null,
        crossControlStatusType: String? = null
    ): MissionNavActionEntity{
        return MissionNavActionEntity.fromMissionActionModel(MissionActionModel(
            id = actionId,
            missionId = missionId,
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true,
            actionType = actionType,
            crossControlId = crossControlId,
            crossControlStatus = crossControlStatusType
        ))
    }
}
