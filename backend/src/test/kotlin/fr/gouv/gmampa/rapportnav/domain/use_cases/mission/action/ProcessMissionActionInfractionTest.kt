package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2.ProcessMissionActionInfraction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.ActionControl
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Infraction
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlSecurityModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlInputMock
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.InfractionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*


@SpringBootTest(classes = [ProcessMissionActionInfraction::class])
@ContextConfiguration(classes = [ProcessMissionActionInfraction::class])
class ProcessMissionActionInfractionTest {

    @MockitoBean
    private lateinit var infractionRepo: IInfractionRepository

    @Captor
    lateinit var deleteCaptor: ArgumentCaptor<List<InfractionEntity>>

    @Captor
    lateinit var saveCaptor: ArgumentCaptor<List<InfractionEntity>>

    @MockitoBean
    private lateinit var processMissionActionInfraction: ProcessMissionActionInfraction

    @Test
    fun `test execute process infractions`() {
        val actionId = UUID.randomUUID()

        //Infraction entities
        val securityInfraction = getInfractionSecurityEntity(actionId)
        val infractionToDelete = getInfractionNavigationEntity(actionId)
        val mockSaveInfraction =
            InfractionEntityMock.create(
                controlType = ControlType.SECURITY,
                missionId = 761,
                actionId = actionId.toString()
            )

        //Control and infraction model
        val actionControl = getActionControlEntity(actionId, securityInfraction)
        val securityInfractionModel = getInfractionSecurityModel(securityInfraction, actionControl)
        val infractionToDeleteModel = getInfractionNavigationModel(infractionToDelete, actionControl)

        //Mock
        val response = listOf(infractionToDeleteModel, securityInfractionModel)
        `when`(infractionRepo.findAllByActionId(actionId.toString())).thenReturn(response)
        `when`(infractionRepo.save(anyOrNull())).thenReturn(InfractionModel.fromInfractionEntity(mockSaveInfraction))

        //When
        processMissionActionInfraction = Mockito.spy(ProcessMissionActionInfraction(infractionRepo))
        val infractions = processMissionActionInfraction.execute(actionId.toString(), actionControl.getAllInfractions())
        verify(processMissionActionInfraction).save(saveCaptor.capture())
        verify(processMissionActionInfraction).delete(deleteCaptor.capture())


        //Then
        assertThat(infractions).isNotNull
        assertThat(5).isEqualTo(saveCaptor.value.size)
        assertThat(2).isEqualTo(deleteCaptor.value.size)
        assertThat(infractionToDelete.id).isEqualTo(deleteCaptor.value.get(0).id)
    }

    private fun getInfractionNavigationEntity(actionId: UUID): InfractionEntity {
        val infractionToDelete =
            InfractionEntityMock.create(
                actionId = actionId.toString(),
                controlType = ControlType.NAVIGATION,
                missionId = 761
            )
        return infractionToDelete
    }

    private fun getInfractionSecurityEntity(actionId: UUID): Infraction {
        val securityInfraction =
            Infraction(
                actionId = actionId.toString(),
                controlType = ControlType.SECURITY,
                missionId = 761
            )
        return securityInfraction
    }

    private fun getInfractionSecurityModel(
        securityInfraction: Infraction,
        actionControl: ActionControl
    ): InfractionModel {
        val securityInfractionModel = InfractionModel.fromInfractionEntity(securityInfraction.toInfractionEntity())
        securityInfractionModel.control =
            ControlSecurityModel.fromControlSecurityEntity(actionControl.controlSecurity!!.toEntity())
        return securityInfractionModel
    }

    private fun getInfractionNavigationModel(
        infractionToDelete: InfractionEntity,
        actionControl: ActionControl
    ): InfractionModel {
        val infractionToDeleteModel = InfractionModel.fromInfractionEntity(infractionToDelete)
        infractionToDeleteModel.control =
            ControlNavigationModel.fromControlNavigationEntity(actionControl.controlNavigation!!.toEntity())
        return infractionToDeleteModel
    }

    private fun getActionControlEntity(
        actionId: UUID,
        securityInfraction: Infraction
    ): ActionControl {
        val controlId = UUID.randomUUID()
        val actionControl = ControlInputMock.createAllControl()
        actionControl.controlGensDeMer?.setMissionIdAndActionId(actionId = actionId.toString(), missionId = 761)
        actionControl.controlSecurity?.setMissionIdAndActionId(actionId = actionId.toString(), missionId = 761)
        actionControl.controlNavigation?.setMissionIdAndActionId(actionId = actionId.toString(), missionId = 761)
        actionControl.controlAdministrative?.setMissionIdAndActionId(actionId = actionId.toString(), missionId = 761)
        actionControl.controlGensDeMer?.infractions = listOf(
            Infraction(
                actionId = actionId.toString(),
                controlType = ControlType.GENS_DE_MER,
                controlId = controlId.toString(),
                missionId = 761
            ),
            Infraction(
                actionId = actionId.toString(),
                controlType = ControlType.GENS_DE_MER,
                controlId = controlId.toString(),
                missionId = 761
            )
        )
        actionControl.controlSecurity?.infractions = listOf(securityInfraction)
        return actionControl
    }

}
