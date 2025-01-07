package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.ExportMissionAEMSingle
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FillAEMExcelRow
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.NavActionControlMock
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(classes = [ExportMissionAEMSingle::class, FormatDateTime::class])
class ExportMissionAEMSingleTest {

    @Autowired
    private lateinit var exportMissionListAEM: ExportMissionAEMSingle

    @MockBean
    private lateinit var fillAEMExcelRow: FillAEMExcelRow

    @MockBean
    private lateinit var getMissionById: GetMission

    @Test
    fun `execute AEM export return null when mission not exist`() {
        val missionId = 123
        val mission = MissionEntityMock.create(id = missionId)
        Mockito.`when`(getMissionById.execute(missionId)).thenReturn(null)

        val result = exportMissionListAEM.createFile(mission)

        Assertions.assertThat(result).isNull()

    }

    @Test
    fun `execute AEM mission list export return a MissionExportEntity when mission list has actions`() {
        val missionId = 1
        val action = NavActionControlMock.create().toNavActionEntity()
        val missionAction = MissionActionEntity.NavAction(action)

        val mission = MissionEntityMock.create(id = missionId, actions = listOf(missionAction))

        Mockito.`when`(getMissionById.execute(missionId)).thenReturn(mission)

        val result = exportMissionListAEM.createFile(mission)

        Assertions.assertThat(result).isNotNull()
        Assertions.assertThat(result).isInstanceOf(MissionExportEntity::class.java)

    }
}
