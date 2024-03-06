package fr.gouv.gmampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMission
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ExportMission::class])
class ExportMissionTest {

//    @Autowired
//    private lateinit var exportMission: ExportMission

//    @MockBean
//    private lateinit var rpnExportRepository: IRpnExportRepository

//    @Test
//    fun `formatNavNoteForTimeline should return formatted string`() {
////        given(this.rpnExportRepository.exportOdt(missionId = 1)).willReturn(emptyList<Any>())
//        val action: ActionFreeNoteEntity = NavActionFreeNoteMock.create()
//        assertThat(formatNavNoteForTimeline(action)).isEqualTo("12:06 - Largué, appareillé")
//    }
//
//    @Test
//    fun `formatNavNoteForTimeline should return null if freeNoteAction is null`() {
//        val action = null
//        assertThat(formatNavNoteForTimeline(action)).isEqualTo("null")
//    }


}
