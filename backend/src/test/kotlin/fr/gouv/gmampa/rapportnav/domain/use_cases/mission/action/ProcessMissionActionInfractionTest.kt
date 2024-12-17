package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2.ProcessMissionActionInfraction
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlSecurityModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlMock
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
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import java.util.*


@SpringBootTest(classes = [ProcessMissionActionInfraction::class])
@ContextConfiguration(classes = [ProcessMissionActionInfraction::class])
class ProcessMissionActionInfractionTest {

    @MockBean
    private lateinit var infractionRepo: IInfractionRepository

    @Captor
    lateinit var deleteCaptor: ArgumentCaptor<List<InfractionEntity>>

    @Captor
    lateinit var saveCaptor: ArgumentCaptor<List<InfractionEntity>>

    @MockBean
    private lateinit var processMissionActionInfraction: ProcessMissionActionInfraction

    @Test
    fun `test execute process infractions`() {
        val actionId = UUID.randomUUID()

        //Infraction entities
        val securityInfraction = getInfractionSecurityEntity(actionId)
        val infractionToDelete = getInfractionNavigationEntity(actionId)
        val mockSaveInfraction = InfractionEntityMock.create(controlType = ControlType.SECURITY)

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
        val infractions = processMissionActionInfraction.execute(actionId.toString(), actionControl)
        verify(processMissionActionInfraction).save(saveCaptor.capture())
        verify(processMissionActionInfraction).delete(deleteCaptor.capture())


        //Then
        assertThat(infractions).isNotNull
        assertThat(5).isEqualTo(saveCaptor.value.size)
        assertThat(1).isEqualTo(deleteCaptor.value.size)
        assertThat(infractionToDelete.id).isEqualTo(deleteCaptor.value.get(0).id)
    }

    private fun getInfractionNavigationEntity(actionId: UUID): InfractionEntity {
        val infractionToDelete =
            InfractionEntityMock.create(actionId = actionId.toString(), controlType = ControlType.NAVIGATION)
        return infractionToDelete
    }

    private fun getInfractionSecurityEntity(actionId: UUID): InfractionEntity {
        val securityInfraction =
            InfractionEntityMock.create(actionId = actionId.toString(), controlType = ControlType.SECURITY)
        return securityInfraction
    }

    private fun getInfractionSecurityModel(
        securityInfraction: InfractionEntity,
        actionControl: ActionControlEntity
    ): InfractionModel {
        val securityInfractionModel = InfractionModel.fromInfractionEntity(securityInfraction)
        securityInfractionModel.control =
            ControlSecurityModel.fromControlSecurityEntity(actionControl.controlSecurity!!)
        return securityInfractionModel
    }

    private fun getInfractionNavigationModel(
        infractionToDelete: InfractionEntity,
        actionControl: ActionControlEntity
    ): InfractionModel {
        val infractionToDeleteModel = InfractionModel.fromInfractionEntity(infractionToDelete)
        infractionToDeleteModel.control =
            ControlNavigationModel.fromControlNavigationEntity(actionControl.controlNavigation!!)
        return infractionToDeleteModel
    }

    private fun getActionControlEntity(
        actionId: UUID,
        securityInfraction: InfractionEntity
    ): ActionControlEntity {
        val actionControl = ControlMock.createAllControl(actionId = actionId.toString())
        actionControl.controlGensDeMer?.infractions = listOf(
            InfractionEntityMock.create(
                actionId = actionId.toString(),
                controlType = ControlType.GENS_DE_MER,
                controlId = actionControl.controlGensDeMer?.id

            ),
            InfractionEntityMock.create(
                actionId = actionId.toString(),
                controlType = ControlType.GENS_DE_MER,
                controlId = actionControl.controlGensDeMer?.id
            )
        )
        actionControl.controlSecurity?.infractions = listOf(securityInfraction)
        return actionControl
    }

}
