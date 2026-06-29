package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import java.util.*
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
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionExternalId
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FillAEMExcelRow
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
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
    @MockitoBean
    private lateinit var getMissionExternalId: GetMissionExternalId

    @Test
    fun `createFile should throw BackendInternalException when underlying service throws`() {
        val missionId = UUID.randomUUID()
        val mission = MissionEntityMock.create(id = missionId)
        whenever(getMissionExternalId.execute(missionId)).thenReturn(123)
        whenever(getEnvMissionById2.execute(any<Int>()))
            .thenThrow(RuntimeException("boom"))
        assertThrows(BackendInternalException::class.java) {
            useCase.createFile(mission)
        }
    }

    @Test
    fun `execute should throw BackendUsageException when mission not found`() {
        val missionId = 999
        whenever(getComputeEnvMission.execute(externalId = missionId))
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
    fun `getAemData should return AEMTableExport2 when missionId is valid`() {
        val missionUUID = UUID.randomUUID()
        whenever(getMissionExternalId.execute(missionUUID)).thenReturn(123)
        whenever(getEnvMissionById2.execute(123)).thenReturn(null)
        whenever(getEnvActionByMissionId.execute(missionUUID)).thenReturn(emptyList())
        whenever(getFIshListActionByMissionId.execute(missionUUID)).thenReturn(emptyList())

        val result = useCase.getAemData(missionId = missionUUID)

        assertNotNull(result)
        assertThat(result).isInstanceOf(AEMTableExport::class.java)
    }

    @Test
    fun `createFile should not throw when externalId is null`() {
        val missionId = UUID.randomUUID()
        val mission = MissionEntity(id = missionId, externalId = null)
        whenever(getMissionExternalId.execute(missionId)).thenReturn(null)
        whenever(getEnvActionByMissionId.execute(missionId)).thenReturn(emptyList())
        whenever(getFIshListActionByMissionId.execute(missionId)).thenReturn(emptyList())

        // createFile should succeed (not throw) when externalId is null
        // since getAemData handles missing externalId gracefully
        val result = useCase.createFile(mission)
        assertNotNull(result)
    }

}
