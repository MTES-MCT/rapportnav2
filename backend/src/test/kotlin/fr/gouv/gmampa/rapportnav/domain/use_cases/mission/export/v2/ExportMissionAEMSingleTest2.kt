package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeEnvActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeFishActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.ExportMissionAEMSingle2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FillAEMExcelRow
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock2
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [ExportMissionAEMSingle2::class, FormatDateTime::class])
class ExportMissionAEMSingleTest2 {

    @Autowired
    private lateinit var useCase: ExportMissionAEMSingle2

    @MockitoBean
    private lateinit var fillAEMExcelRow: FillAEMExcelRow
    @MockitoBean
    private lateinit var getEnvMissionById2: GetEnvMissionById2
    @MockitoBean
    private lateinit var getEnvActionByMissionId: GetComputeEnvActionListByMissionId
    @MockitoBean
    private lateinit var getNavActionByMissionId: GetComputeNavActionListByMissionId
    @MockitoBean
    private lateinit var getFIshListActionByMissionId: GetComputeFishActionListByMissionId
    @MockitoBean
    private lateinit var getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId
    @MockitoBean
    private lateinit var getComputeEnvMission: GetComputeEnvMission

    @Test
    fun `execute AEM export return null when mission not exist`() {
        val result = useCase.createFile(mission=null)
        Assertions.assertThat(result).isNull()
    }

    @Test
    fun `createFile should return null when mission throws`() {
        val missionId = 123
        val mission = MissionEntityMock2.create(id = missionId)
        whenever(getEnvMissionById2.execute(any()))
            .thenThrow(RuntimeException("boom"))
        assertThat(useCase.createFile(mission)).isNull()
    }

}
