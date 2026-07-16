package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2.passengers

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionCrew
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.DeleteMissionPassenger
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.GetMissionPassengers
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.ProcessMissionPassengers
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.UpdateMissionPassenger
import fr.gouv.gmampa.rapportnav.mocks.mission.passenger.MissionPassengerEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*


@SpringBootTest(classes = [ProcessMissionPassengers::class])
class ProcessMissionPassengersTest {

    @MockitoBean
    lateinit var getMissionPassengers: GetMissionPassengers

    @MockitoBean
    lateinit var updateMissionPassenger: UpdateMissionPassenger

    @MockitoBean
    lateinit var deleteMissionPassenger: DeleteMissionPassenger

    @MockitoBean
    lateinit var processMissionPassengers: ProcessMissionPassengers

    @Test
    fun `test execute process passengers`() {
        val missionId = UUID.randomUUID()
        val (missionCrews, dbMissionCrews) = getMissionPassengers()

        //Mock
        `when`(getMissionPassengers.execute(missionId)).thenReturn(dbMissionCrews)
        `when`(updateMissionPassenger.execute(anyOrNull())).thenReturn(dbMissionCrews.get(0))

        //When
        processMissionPassengers = Mockito.spy(
            ProcessMissionPassengers(
                deleteMissionPassenger = deleteMissionPassenger,
                updateMissionPassenger = updateMissionPassenger,
                getMissionPassengers = getMissionPassengers
            )
        )

        val crews = processMissionPassengers.execute(missionId, missionCrews)

        //Then
        assertThat(crews).isNotNull
        assertThat(crews.size).isEqualTo(3)
    }

    private fun getMissionPassengers(): Pair<List<MissionPassengerEntity>, List<MissionPassengerEntity>> {
        val passenger1 = MissionPassengerEntityMock.create(id = 1, )
        val passenger2 = MissionPassengerEntityMock.create(id = 2, )
        val passenger3 = MissionPassengerEntityMock.create(id = 3, )
        val passenger4 = MissionPassengerEntityMock.create(id = 4, )

        val passengers = listOf(passenger1, passenger2)
        val dbPassengers = listOf(passenger1, passenger3, passenger4)
        return Pair(passengers, dbPassengers)
    }
}
