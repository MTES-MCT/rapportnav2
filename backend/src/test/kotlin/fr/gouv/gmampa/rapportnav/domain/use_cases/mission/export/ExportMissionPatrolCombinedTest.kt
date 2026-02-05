package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionPatrolCombined
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionPatrolSingle
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.MissionCrewEntityMock
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [ExportMissionPatrolCombined::class, FormatDateTime::class])
class ExportMissionPatrolCombinedTest {

    @Autowired
    private lateinit var exportMissionPatrolCombined: ExportMissionPatrolCombined

    @MockitoBean
    private lateinit var exportMissionPatrolSingle: ExportMissionPatrolSingle

    @MockitoBean
    private lateinit var getComputeEnvMission: GetComputeEnvMission

    @Test
    fun `should throw BackendUsageException for empty mission list`() {
        assertThrows(BackendUsageException::class.java) {
            exportMissionPatrolCombined.execute(emptyList())
        }
    }

    @Test
    fun `should export a file`() {
        val missionIds = listOf(1)
        val mission2 = MissionEntityMock.create(
            generalInfos = MissionGeneralInfoEntity2(
                data = MissionGeneralInfoEntity(
                    id = 1,
                    missionId = missionIds.first(),
                    nbrOfRecognizedVessel = 3,
                    consumedFuelInLiters = 3f,
                    consumedGOInLiters = 3f,
                    distanceInNauticalMiles = 3f
                ),
                crew = listOf(MissionCrewEntityMock.create()),
            )
        )

        Mockito.`when`(getComputeEnvMission.execute(missionId = mission2.id)).thenReturn(
            mission2
        )

        val result = exportMissionPatrolCombined.execute(missionIds)

        assertNotNull(result)
        assertEquals("rapports-patrouille-combinés_2022-01-02.odt", result?.fileName)
    }

    @Test
    fun `should throw BackendInternalException when underlying service throws`() {
        val missionIds = listOf(1)
        Mockito.`when`(getComputeEnvMission.execute(missionId = 1))
            .thenThrow(RuntimeException("Mock exception"))

        assertThrows(BackendInternalException::class.java) {
            exportMissionPatrolCombined.execute(missionIds)
        }
    }

}
