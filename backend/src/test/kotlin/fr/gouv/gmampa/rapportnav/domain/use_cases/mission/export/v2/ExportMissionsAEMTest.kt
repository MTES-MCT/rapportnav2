package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionAEMExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.ExportMissionsAEM
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FillAEMExcelRow
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.NavActionControlMock
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(classes = [ExportMissionsAEM::class])
class ExportMissionsAEMTest {

    @Autowired
    private lateinit var exportMissionListAEM: ExportMissionsAEM

    @MockBean
    private lateinit var fillAEMExcelRow: FillAEMExcelRow

    @MockBean
    private lateinit var getMissionById: GetMission

    @Test
    fun `execute AEM mission list export return a MissionAEMExportEntity when mission list has actions`() {
        val action = NavActionControlMock.create().toNavActionEntity()
        val missionAction = MissionActionEntity.NavAction(action)

        val mission = MissionEntityMock.create(actions = listOf(missionAction))

        Mockito.`when`(getMissionById.execute(1)).thenReturn(mission)

        val result = exportMissionListAEM.execute(listOf(1))

        Assertions.assertThat(result).isNotNull()
        Assertions.assertThat(result).isInstanceOf(MissionAEMExportEntity::class.java)

    }
}
