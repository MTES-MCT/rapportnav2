package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.AddOrUpdateMissionCrew
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.DeleteMissionCrew
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.MissionCrew
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.MissionCrewEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean


@SpringBootTest(classes = [ProcessMissionCrew::class])
@ContextConfiguration(classes = [ProcessMissionCrew::class])
class ProcessMissionCrewTest {

    @MockitoBean
    lateinit var getAgentsCrewByMissionId: GetAgentsCrewByMissionId

    @MockitoBean
    lateinit var addOrUpdateMissionCrew: AddOrUpdateMissionCrew

    @MockitoBean
    lateinit var deleteMissionCrew: DeleteMissionCrew

    @MockitoBean
    lateinit var processMissionCrew: ProcessMissionCrew

    @Test
    fun `test execute process crew`() {
        val crew1 = MissionCrewEntityMock.create(id = 1, missionId = 761)
        val crew2 = MissionCrewEntityMock.create(id = 2, missionId = 761)
        val crew3 = MissionCrewEntityMock.create(id = 3, missionId = 761)
        val crew4 = MissionCrewEntityMock.create(id = 4, missionId = 761)

        val missionCrews = listOf<MissionCrew>(
            MissionCrew.fromMissionCrewEntity(crew1),
            MissionCrew.fromMissionCrewEntity(crew2),
        )
        val dbMissionCrews = listOf<MissionCrewEntity>(crew1, crew3, crew4)


        //Mock
        `when`(getAgentsCrewByMissionId.execute(761)).thenReturn(dbMissionCrews)
        `when`(addOrUpdateMissionCrew.addOrUpdateMissionCrew(anyOrNull())).thenReturn(crew1)

        //When
        processMissionCrew = Mockito.spy(
            ProcessMissionCrew(
                deleteMissionCrew = deleteMissionCrew,
                addOrUpdateMissionCrew = addOrUpdateMissionCrew,
                getAgentsCrewByMissionId = getAgentsCrewByMissionId
            )
        )

        val crews = processMissionCrew.execute(761, missionCrews.map { it.toMissionCrewEntity() })

        //Then
        assertThat(crews).isNotNull
        assertThat(crews.size).isEqualTo(3)
    }
}
