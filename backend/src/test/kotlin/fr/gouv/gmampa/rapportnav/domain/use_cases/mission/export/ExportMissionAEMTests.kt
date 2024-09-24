package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionAEMExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionAEM
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FillAEMExcelRow
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.NavActionControlMock
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
    private lateinit var fillAEMExcelRow: FillAEMExcelRow

    @MockBean
    private lateinit var getMissionById: GetMission

    @Test
    fun `execute AEM export return null when mission not exist`() {
        val missionId = 123
        Mockito.`when`(getMissionById.execute(missionId)).thenReturn(null)

        val result = exportMissionAEM.execute(missionId)

        Assertions.assertThat(result).isNull()

    }
/*
    @Test
    fun `execute AEM export return a MissionAEMExportEntity when mission and action exist`() {
        val action = NavActionControlMock.createActionControlEntity().toNavActionEntity()

        val missionAction = MissionActionEntity.NavAction(action)


        val missionId = 1
        val mission = MissionEntityMock.create(actions = listOf(missionAction))
        Mockito.`when`(getMissionById.execute(missionId)).thenReturn(mission)

        val result = exportMissionAEM.execute(missionId)

        Assertions.assertThat(result).isNotNull()
        Assertions.assertThat(result).isInstanceOf(MissionAEMExportEntity::class.java)

    }*/
}
