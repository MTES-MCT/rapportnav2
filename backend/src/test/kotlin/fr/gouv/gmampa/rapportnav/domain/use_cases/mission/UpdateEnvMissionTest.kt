package fr.gouv.gmampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.UpdateEnvMission
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(classes = [UpdateEnvMission::class])
class UpdateEnvMissionTest {
    @Autowired
    private lateinit var updateEnvMission: UpdateEnvMission

    @MockBean
    private lateinit var envRepository: IEnvMissionRepository

    @Test
    fun `execute update env mission with ObservationByUnit`() {
        val missionId = 761;
        val observationsByUnit = "MyBeautifulObservation";
        val mission = EnvMissionMock.create(observationsByUnit = observationsByUnit);
        val missionEnvEntity = MissionEnvEntity(observationsByUnit = observationsByUnit);

        Mockito.`when`(envRepository.updateMission(missionId, missionEnvEntity)).thenReturn(mission);
        val response =
            updateEnvMission.execute(MissionEnvInput(missionId = missionId, observationsByUnit = observationsByUnit));
        assertThat(response).isNotNull();
        assertThat(response.observationsByUnit).isEqualTo(observationsByUnit);

    }
}
