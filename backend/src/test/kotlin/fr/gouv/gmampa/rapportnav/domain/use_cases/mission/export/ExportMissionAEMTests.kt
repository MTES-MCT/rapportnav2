package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionAEM
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionControlMock
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(classes = [ExportMissionAEM::class])
class ExportMissionAEMTests {

    @Autowired
    private lateinit var exportMissionAEM: ExportMissionAEM

    @MockBean
    private lateinit var envRepository: IEnvMissionRepository

    @Test
    fun `execute AEM export to ods`() {
        val envAction = EnvActionControlMock.create()
        val mission = EnvMissionMock.create(
            envActions = listOf(envAction)
        )

        Mockito.`when`(envRepository.findMissionById(1)).thenReturn(mission);
        val response = exportMissionAEM.execute(mission.id!!);

        Assertions.assertThat(response).isNotNull();

    }
}
