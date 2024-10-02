package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionRapportPatrouille
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.FormatActionsForTimeline
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.GetInfoAboutNavAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.MapStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetNbOfDaysAtSeaFromNavigationStatus
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.Mockito.reset
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(
    classes = [
        ExportMissionRapportPatrouille::class,
        ComputeDurations::class,
        GetInfoAboutNavAction::class,
        MapStatusDurations::class,
        GetStatusDurations::class,
        GetNbOfDaysAtSeaFromNavigationStatus::class,
        FormatDateTime::class,
    ]
)
class ExportMissionRapportPatrouilleTests {

    @Autowired
    private lateinit var exportMissionRapportPatrouille: ExportMissionRapportPatrouille

    @MockBean
    private lateinit var getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId

    @MockBean
    private lateinit var agentsCrewByMissionId: GetAgentsCrewByMissionId

    @MockBean
    private lateinit var getMission: GetMission

    @MockBean
    private lateinit var navActionStatus: INavActionStatusRepository

    @MockBean
    private lateinit var formatActionsForTimeline: FormatActionsForTimeline

    @MockBean
    private lateinit var getServiceById: GetServiceById

    @BeforeEach
    fun setUp() {
        reset(getMission)
    }

    @Test
    fun `exportOdt should return null if mission is not found`() {
        val missionId = 123
        `when`(getMission.execute(missionId)).thenReturn(null)

        val result = exportMissionRapportPatrouille.exportOdt(missionId)

        assertThat(result).isNull()
    }

    @Test
    fun `exportOdt should not throw`() {
        val missionId = 123
        assertDoesNotThrow {
            exportMissionRapportPatrouille.exportOdt(missionId)
        }
    }

    @Test
    fun `exportOdt should return null when throw`() {
        val missionId = 123
        `when`(getMission.execute(missionId)).thenThrow()
        assertThat(exportMissionRapportPatrouille.exportOdt(missionId)).isNull()
    }

//    @Test
//    fun `exportOdt should return MissionExportEntity`() {
//        val missionId = 123
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
//        `when`(exportRepository.exportOdt(exportParams)).thenReturn(output)
//
//        val result = exportMission.exportOdt(missionId)
//        assertThat(result).isEqualTo(output)
//    }
}
