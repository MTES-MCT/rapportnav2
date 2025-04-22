package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.FormatActionsForTimeline
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.GetInfoAboutNavAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.MapStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.ExportMissionPatrolSingle
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetNbOfDaysAtSeaFromNavigationStatus
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.Mockito.reset
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(
    classes = [
        ExportMissionPatrolSingle::class,
        ComputeDurations::class,
        GetInfoAboutNavAction::class,
        MapStatusDurations::class,
        GetStatusDurations::class,
        GetNbOfDaysAtSeaFromNavigationStatus::class,
        FormatDateTime::class,
    ]
)
class ExportMissionPatrolSingleTest {

    @Autowired
    private lateinit var exportMissionRapportPatrouille: ExportMissionPatrolSingle

    @MockitoBean
    private lateinit var getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId

    @MockitoBean
    private lateinit var agentsCrewByMissionId: GetAgentsCrewByMissionId

    @MockitoBean
    private lateinit var getMission: GetMission

    @MockitoBean
    private lateinit var navActionStatus: INavActionStatusRepository

    @MockitoBean
    private lateinit var formatActionsForTimeline: FormatActionsForTimeline

    @MockitoBean
    private lateinit var getServiceById: GetServiceById

    @BeforeEach
    fun setUp() {
        reset(getMission)
    }

    @Test
    fun `execute should return null if mission is not found`() {
        val missionId = "123"
        `when`(getMission.execute(missionId)).thenReturn(null)

        val result = exportMissionRapportPatrouille.execute(missionId)

        assertThat(result).isNull()
    }

    @Test
    fun `execute should not throw`() {
        val missionId = "123"
        assertDoesNotThrow {
            exportMissionRapportPatrouille.execute(missionId)
        }
    }

    @Test
    fun `execute should return null when mission is null`() {
        val missionId = "123"
        `when`(getMission.execute(missionId)).thenReturn(null)
        assertThat(exportMissionRapportPatrouille.execute(missionId)).isNull()
    }

    @Test
    fun `createFile should return null when mission throws`() {
        val missionId = "123"
        val mission = MissionEntityMock.create(id = missionId)
        `when`(getMission.execute(missionId)).thenThrow()
        assertThat(exportMissionRapportPatrouille.createFile(mission)).isNull()
    }

//    @Test
//    fun `execute should return MissionExportEntity`() {
//        val missionId = "123"
//        val output = MissionExportEntity(
//            fileName = "rapport.odt",
//            fileContent = "some content"
//        )
//        // check why mockito.any() did not work, hence the creation of this mock var
//        val exportParams = ExportParams(
//            service = null,
//            id = "2022-01-02",
//            startDateTime = Instant.parse("2022-01-02T12:00:00Z"),
//            endDateTime = null,
//            presenceMer = mapOf(
//                "navigationEffective" to 0,
//                "mouillage" to 0,
//                "total" to 0,
//            ),
//            presenceQuai = mapOf(
//                "maintenance" to 0,
//                "meteo" to 0,
//                "representation" to 0,
//                "adminFormation" to 0,
//                "autre" to 0,
//                "contrPol" to 0,
//                "total" to 0,
//            ),
//            indisponibilite = mapOf(
//                "technique" to 0,
//                "personnel" to 0,
//                "total" to 0,
//            ),
//            nbJoursMer = 0,
//            dureeMission = 0,
//            patrouilleEnv = 0,
//            patrouilleMigrant = 0,
//            distanceMilles = null,
//            goMarine = null,
//            essence = null,
//            crew = emptyList(),
//            timeline = emptyList(),
//            rescueInfo = null,
//            nauticalEventsInfo = null,
//            antiPollutionInfo = null,
//            baaemAndVigimerInfo = null,
//        )
//        `when`(getMission.execute(missionId)).thenReturn(MissionEntityMock.create())
//        `when`(exportRepository.execute(exportParams)).thenReturn(output)
//
//        val result = exportMission.execute(missionId)
//        assertThat(result).isEqualTo(output)
//    }
}
