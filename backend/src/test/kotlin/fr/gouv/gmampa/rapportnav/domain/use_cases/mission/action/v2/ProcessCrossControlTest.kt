package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlConclusionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlOriginType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.VesselEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeTarget
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessCrossControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessFishAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetVessels
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Vessel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.CrossControlModel
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [ProcessCrossControl::class])
@ContextConfiguration(classes = [ProcessCrossControl::class])
class ProcessCrossControlTest {
    @Autowired
    private lateinit var processCrossControl: ProcessCrossControl

    @MockitoBean
    private lateinit var getVessels: GetVessels

    @MockitoBean
    private lateinit var  missionActionRepository: INavMissionActionRepository

    @Test
    fun `test execute get fish action by id`() {
        val vessels = listOf(
            VesselEntity(vesselId = 4556),
            VesselEntity(vesselId = 34556)
        )
        val model = CrossControlModel(
            id = UUID.randomUUID(),
            type = "",
            agentId = "5",
            vesselId = 4556,
            serviceId = 6,
            endDateTimeUtc = Instant.parse("2015-07-30T00:00:00.00Z"),
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z"),
            origin = CrossControlOriginType.FOLLOW_UP_CONTROL.toString(),
            status = CrossControlStatusType.NEW.toString(),
            conclusion = CrossControlConclusionType.NO_FOLLOW_UP.toString()
        )

        `when`(getVessels.execute()).thenReturn(vessels)
        processCrossControl = ProcessCrossControl(
            getVessels = getVessels,
            missionActionRepository = missionActionRepository
        )
        val entity = processCrossControl.execute(model)

        assertThat(entity).isNotNull
        assertThat(entity.id).isEqualTo(model.id)
        assertThat(entity.vesselId).isEqualTo(4556)
    }
}
