package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.ProcessMissionActionControl
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlAdministrativeModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlGensDeMerModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlSecurityModel
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlInputMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import java.util.*

@SpringBootTest(classes = [ProcessMissionActionControl::class])
@ContextConfiguration(classes = [ProcessMissionActionControl::class])
class ProcessMissionActionControlTest {

    @MockBean
    private lateinit var processMissionActionControl: ProcessMissionActionControl

    @MockBean
    private lateinit var controlSecurityRepo: IControlSecurityRepository

    @MockBean
    private lateinit var controlNavigationRepo: IControlNavigationRepository

    @MockBean
    private lateinit var controlGensDeMerRepo: IControlGensDeMerRepository

    @MockBean
    private lateinit var controlAdministrativeRepo: IControlAdministrativeRepository

    @Test
    fun `Should process all controls  when execute, `() {
        val action = getMissionAction()
        val input = ControlInputMock.createAllControl()

        input.controlGensDeMer?.setMissionIdAndActionId(actionId = action.id.toString(), missionId = action.missionId)
        input.controlSecurity?.setMissionIdAndActionId(actionId = action.id.toString(), missionId = action.missionId)
        input.controlNavigation?.setMissionIdAndActionId(actionId = action.id.toString(), missionId = action.missionId)
        input.controlAdministrative?.setMissionIdAndActionId(actionId = action.id.toString(), missionId = action.missionId)

        val controls = input.toActionControlEntity()

        action.controlSecurity = controls.controlSecurity
        action.controlNavigation = controls.controlNavigation
        action.controlAdministrative = controls.controlAdministrative

        val controlSecurityModel = ControlSecurityModel.fromControlSecurityEntity(controls.controlSecurity!!)
        val controlNavigationModel = ControlNavigationModel.fromControlNavigationEntity(controls.controlNavigation!!)

        val controlGensDeMerModel = ControlGensDeMerModel.fromControlGensDeMerEntity(controls.controlGensDeMer!!)
        val controlAdministrationModel =
            ControlAdministrativeModel.fromControlAdministrativeEntity(controls.controlAdministrative!!)

        controlAdministrationModel.hasBeenDone = true
        `when`(controlSecurityRepo.findByActionControlId(action.id.toString())).thenReturn(controlSecurityModel)
        `when`(controlGensDeMerRepo.findByActionControlId(action.id.toString())).thenReturn(controlGensDeMerModel)
        `when`(controlNavigationRepo.findByActionControlId(action.id.toString())).thenReturn(controlNavigationModel)
        `when`(controlAdministrativeRepo.findByActionControlId(action.id.toString())).thenReturn(
            controlAdministrationModel
        )

        `when`(controlSecurityRepo.save(anyOrNull())).thenReturn(controlSecurityModel)
        `when`(controlGensDeMerRepo.save(anyOrNull())).thenReturn(controlGensDeMerModel)
        `when`(controlNavigationRepo.save(anyOrNull())).thenReturn(controlNavigationModel)
        `when`(controlAdministrativeRepo.save(anyOrNull())).thenReturn(controlAdministrationModel)

        processMissionActionControl = ProcessMissionActionControl(
            controlSecurityRepo = controlSecurityRepo,
            controlGensDeMerRepo = controlGensDeMerRepo,
            controlNavigationRepo = controlNavigationRepo,
            controlAdministrativeRepo = controlAdministrativeRepo
        )

        input.controlGensDeMer = null
        val actionControl = processMissionActionControl.execute(actionId = action.id.toString(), controls = input)

        assertThat(actionControl).isNotNull
        assertThat(actionControl.controlGensDeMer).isNull()
        assertThat(actionControl.controlSecurity).isEqualTo(input.controlSecurity)
        assertThat(actionControl.controlNavigation).isEqualTo(input.controlNavigation)
        assertThat(actionControl.controlAdministrative).isEqualTo(input.controlAdministrative)
    }


    private fun getMissionAction(): MissionNavActionEntity {
        return MissionNavActionEntity(
            id = UUID.randomUUID(),
            missionId = 761,
            actionType = ActionType.CONTROL,
        )
    }

}
