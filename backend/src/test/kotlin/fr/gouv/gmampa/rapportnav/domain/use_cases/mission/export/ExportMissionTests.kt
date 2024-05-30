package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.ExportParams
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IRpnExportRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.FormatActionsForTimeline
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.GetInfoAboutNavAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.MapStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetNbOfDaysAtSeaFromNavigationStatus
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.Mockito.reset
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

@SpringBootTest(
    classes = [
        ExportMission::class,
        ComputeDurations::class,
        GetInfoAboutNavAction::class,
        MapStatusDurations::class,
        GetStatusDurations::class,
        GetNbOfDaysAtSeaFromNavigationStatus::class,
    ]
)
class ExportMissionTests {

    @Autowired
    private lateinit var exportMission: ExportMission

    @MockBean
    private lateinit var exportRepository: IRpnExportRepository

    @MockBean
    private lateinit var getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId

    @MockBean
    private lateinit var agentsCrewByMissionId: GetAgentsCrewByMissionId

    @MockBean
    private lateinit var getMissionById: GetMissionById

    @MockBean
    private lateinit var navActionStatus: INavActionStatusRepository

    @MockBean
    private lateinit var formatActionsForTimeline: FormatActionsForTimeline

    @BeforeEach
    fun setUp() {
        reset(exportRepository)
        reset(getMissionById)
    }

    @Test
    fun `exportOdt should return null if mission is not found`() {
        val missionId = 123
        `when`(getMissionById.execute(missionId)).thenReturn(null)

        val result = exportMission.exportOdt(missionId)

        assertThat(result).isNull()
    }

    @Test
    fun `exportOdt should not throw`() {
        val missionId = 123
        assertDoesNotThrow {
            exportMission.exportOdt(missionId)
        }
    }

    @Test
    fun `exportOdt should return null when throw`() {
        val missionId = 123
        `when`(getMissionById.execute(missionId)).thenThrow()
        assertThat(exportMission.exportOdt(missionId)).isNull()
    }

    @Test
    fun `exportOdt should return MissionExportEntity`() {
        val missionId = 123
        val output = MissionExportEntity(
            fileName = "rapport.odt",
            fileContent = "some content"
        )
        // check why mockito.any() did not work, hence the creation of this mock var
        val exportParams = ExportParams(
            service = null,
            id = "2022-01-02",
            startDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 0), ZoneOffset.UTC),
            endDateTime = null,
            presenceMer = mapOf(
                "navigationEffective" to 0,
                "mouillage" to 0,
                "total" to 0,
            ),
            presenceQuai = mapOf(
                "maintenance" to 0,
                "meteo" to 0,
                "representation" to 0,
                "adminFormation" to 0,
                "autre" to 0,
                "contrPol" to 0,
                "total" to 0,
            ),
            indisponibilite = mapOf(
                "technique" to 0,
                "personnel" to 0,
                "total" to 0,
            ),
            nbJoursMer = 0,
            dureeMission = 0,
            patrouilleEnv = 0,
            patrouilleMigrant = 0,
            distanceMilles = null,
            goMarine = null,
            essence = null,
            crew = emptyList(),
            timeline = emptyList(),
            rescueInfo = null,
            nauticalEventsInfo = null,
            antiPollutionInfo = null,
            baaemAndVigimerInfo = null,
        )
        `when`(getMissionById.execute(missionId)).thenReturn(MissionEntityMock.create())
        `when`(exportRepository.exportOdt(exportParams)).thenReturn(output)

        val result = exportMission.exportOdt(missionId)
        assertThat(result).isEqualTo(output)
    }
}
