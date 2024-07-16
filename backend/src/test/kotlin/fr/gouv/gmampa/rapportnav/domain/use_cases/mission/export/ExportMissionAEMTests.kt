package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionAEM
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
    private lateinit var getMissionById: GetMissionById

    @Test
    fun `execute AEM export return null`() {
        val missionId = 123
        Mockito.`when`(getMissionById.execute(missionId)).thenReturn(null)

        val result = exportMissionAEM.execute(missionId)

        Assertions.assertThat(result).isNull()

    }
}
