package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeEnvActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeFishActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionAEMSingle
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FillAEMExcelRow
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [ExportMissionAEMSingle::class, FormatDateTime::class])
class ExportMissionAEMSingleTest {

    @Autowired
    private lateinit var useCase: ExportMissionAEMSingle

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
    fun `createFile should throw BackendInternalException when underlying service throws`() {
        val missionId = 123
        val mission = MissionEntityMock.create(id = missionId)
        whenever(getEnvMissionById2.execute(any()))
            .thenThrow(RuntimeException("boom"))
        assertThrows(BackendInternalException::class.java) {
            useCase.createFile(mission)
        }
    }

    @Test
    fun `execute should throw BackendUsageException when mission not found`() {
        val missionId = 123
        whenever(getComputeEnvMission.execute(missionId = missionId))
            .thenAnswer {
                throw BackendUsageException(
                    code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                    message = "Env mission not found: $missionId"
                )
            }

        val exception = assertThrows(BackendUsageException::class.java) {
            useCase.execute(missionId)
        }
        assertThat(exception.message).isEqualTo("Env mission not found: $missionId")
    }

    @Test
    fun `getAemData should return null when missionId is null`() {
        val result = useCase.getAemData(null)
        assertThat(result).isNull()
    }

    @Test
    fun `getAemData should return AEMTableExport2 when missionId is valid`() {
        val missionId = 123
        whenever(getEnvMissionById2.execute(missionId)).thenReturn(null)
        whenever(getEnvActionByMissionId.execute(missionId)).thenReturn(emptyList())
        whenever(getNavActionByMissionId.execute(missionId)).thenReturn(emptyList())
        whenever(getFIshListActionByMissionId.execute(missionId)).thenReturn(emptyList())
        whenever(getMissionGeneralInfoByMissionId.execute(missionId)).thenReturn(null)

        val result = useCase.getAemData(missionId)

        assertNotNull(result)
        assertThat(result).isInstanceOf(AEMTableExport::class.java)
    }

    @Test
    fun `createFile should throw BackendInternalException when mission id is null`() {
        val mission = MissionEntityMock.create(id = null)

        assertThrows(BackendInternalException::class.java) {
            useCase.createFile(mission)
        }
    }

}
