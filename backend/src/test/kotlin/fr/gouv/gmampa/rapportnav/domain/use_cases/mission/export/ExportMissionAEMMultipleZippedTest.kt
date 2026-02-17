package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionAEMMultipleZipped
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionAEMSingle
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.MissionCrewEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [ExportMissionAEMMultipleZipped::class])
class ExportMissionAEMMultipleZippedTest {

    @Autowired
    private lateinit var useCase: ExportMissionAEMMultipleZipped

    @MockitoBean
    private lateinit var exportMissionAEMSingle: ExportMissionAEMSingle

    @MockitoBean
    private lateinit var getComputeEnvMission: GetComputeEnvMission

    @Test
    fun `should throw BackendUsageException for empty mission list`() {
        assertThrows(BackendUsageException::class.java) {
            useCase.execute(emptyList())
        }
    }

    @Test
    fun `should export a file`() {
        val missionIds = listOf(1)
        `when`(exportMissionAEMSingle.createFile(any())).thenReturn(
            MissionExportEntity(
                fileName = "exportMissionAEMSingle.odt",
                fileContent = "MockContent"
            )
        )
        val mission2 = MissionEntityMock.create(
            generalInfos = MissionGeneralInfoEntity2(
                data = MissionGeneralInfoEntityMock.create(
                    id = 1,
                    missionId = missionIds.first(),
                    service = ServiceEntityMock.create(),
                    nbrOfRecognizedVessel = 3,
                    consumedFuelInLiters = 3f,
                    consumedGOInLiters = 3f,
                    distanceInNauticalMiles = 3f
                ),
                crew = listOf(MissionCrewEntityMock.create()),
            )
        )

        `when`(getComputeEnvMission.execute(missionId = mission2.id)).thenReturn(
            mission2
        )

        val result = useCase.execute(missionIds)

        assertNotNull(result)
        assertEquals("tableaux-AEM.zip", result?.fileName)
    }

    @Test
    fun `should propagate exception when underlying service throws`() {
        val missionIds = listOf(1)
        `when`(getComputeEnvMission.execute(missionId = 1))
            .thenThrow(RuntimeException("Mock exception"))

        assertThrows(RuntimeException::class.java) {
            useCase.execute(missionIds)
        }
    }

}
