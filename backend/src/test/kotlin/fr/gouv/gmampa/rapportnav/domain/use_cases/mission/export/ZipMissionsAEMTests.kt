package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionAEMExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ZipExportMissionListAEM
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FillAEMExcelRow
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.NavActionControlMock
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(classes = [ZipExportMissionListAEM::class])
class ZipMissionsAEMTests {

    @MockBean
    private lateinit var getMissionById: GetMission

    @MockBean
    private lateinit var fillAEMExcelRow: FillAEMExcelRow

    @Autowired
    private lateinit var zipExportMissionListAEM: ZipExportMissionListAEM


    @Test
    fun `should return a MissionExportAEMEntity`() {
        val action = NavActionControlMock.create().toNavActionEntity()
        val missionAction = MissionActionEntity.NavAction(action)
        val mission = MissionEntityMock.create(actions = listOf(missionAction))
        val mission2 = MissionEntityMock.create(actions = listOf(missionAction), id = 2)

        Mockito.`when`(getMissionById.execute(1)).thenReturn(mission)
        Mockito.`when`(getMissionById.execute(2)).thenReturn(mission2)

        val missionAEMExport = zipExportMissionListAEM.execute(listOf(1, 2))

        Assertions.assertThat(missionAEMExport).isInstanceOf(MissionAEMExportEntity::class.java)
    }
}
