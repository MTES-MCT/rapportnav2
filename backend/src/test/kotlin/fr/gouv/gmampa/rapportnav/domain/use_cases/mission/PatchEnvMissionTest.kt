package fr.gouv.gmampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.PatchEnvMission
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [PatchEnvMission::class])
class PatchEnvMissionTest {
    @Autowired
    private lateinit var patchEnvMission: PatchEnvMission

    @MockitoBean
    private lateinit var envRepository: IEnvMissionRepository

    @Test
    fun `execute update env mission with ObservationByUnit`() {
        val missionId = "761"
        val observationsByUnit = "MyBeautifulObservation"
        val mission = EnvMissionMock.create(observationsByUnit = observationsByUnit)
        val missionEnvEntity = PatchMissionInput(observationsByUnit = observationsByUnit)

        Mockito.`when`(envRepository.patchMission(missionId, missionEnvEntity)).thenReturn(mission)
        val response =
            patchEnvMission.execute(MissionEnvInput(missionId = missionId, observationsByUnit = observationsByUnit))
        assertThat(response).isNotNull()
        assertThat(response?.observationsByUnit).isEqualTo(observationsByUnit)

    }
}
