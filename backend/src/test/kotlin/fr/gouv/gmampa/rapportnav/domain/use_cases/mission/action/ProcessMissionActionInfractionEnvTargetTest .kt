package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionEnvTargetRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2.ProcessMissionActionInfractionEnvTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.Infraction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.InfractionTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.InfractionsByVessel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
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


@SpringBootTest(classes = [ProcessMissionActionInfractionEnvTarget::class])
@ContextConfiguration(classes = [ProcessMissionActionInfractionEnvTarget::class])
class ProcessMissionActionInfractionEnvTargetTest {

    @MockBean
    private lateinit var infractionRepo: IInfractionRepository

    @MockBean
    private lateinit var infractionEnvTargetRepo: IInfractionEnvTargetRepository

    @Captor
    lateinit var deleteCaptor: ArgumentCaptor<List<InfractionEntity>>

    @Captor
    lateinit var saveCaptor: ArgumentCaptor<List<Infraction>>

    @MockBean
    private lateinit var processMissionActionInfractionEnvTarget: ProcessMissionActionInfractionEnvTarget

    @Test
    fun `test execute process infractions env target`() {
        val actionId = UUID.randomUUID()
        val infractions = getInfractions(actionId.toString())
        val byVessels = InfractionsByVessel(infractions = infractions).groupInfractionsByVesselIdentifier(infractions)

        //Infraction entities
        val infractionToDelete = InfractionEntityMock.create(
            actionId = actionId.toString(),
            controlType = ControlType.GENS_DE_MER
        )
        val mockSaveInfraction = InfractionEntityMock.create(controlType = ControlType.SECURITY)

        //Mock
        val response = listOf(InfractionModel.fromInfractionEntity(infractionToDelete))

        `when`(infractionRepo.findAllByActionId(actionId.toString())).thenReturn(response)
        `when`(infractionRepo.save(anyOrNull())).thenReturn(InfractionModel.fromInfractionEntity(mockSaveInfraction))

        //When
        processMissionActionInfractionEnvTarget = Mockito.spy(
            ProcessMissionActionInfractionEnvTarget(
                infractionRepo = infractionRepo,
                infractionEnvTargetRepo = infractionEnvTargetRepo
            )
        )
        val entities = processMissionActionInfractionEnvTarget.execute(actionId.toString(), byVessels)
        verify(processMissionActionInfractionEnvTarget).save(saveCaptor.capture())
        verify(processMissionActionInfractionEnvTarget).delete(deleteCaptor.capture())


        //Then
        assertThat(entities).isNotNull
        assertThat(6).isEqualTo(saveCaptor.value.size)
        assertThat(1).isEqualTo(deleteCaptor.value.size)
        assertThat(infractionToDelete.id).isEqualTo(deleteCaptor.value.get(0).id)
    }


    private fun getInfractions(actionId: String): List<Infraction> {
        return listOf(
            Infraction(
                id = UUID.randomUUID().toString(),
                actionId = actionId,
                missionId = 145,
                controlId = UUID.randomUUID(),
                infractionType = InfractionTypeEnum.WITH_REPORT,
                target = InfractionTarget(
                    id = UUID.randomUUID().toString(),
                    identityControlledPerson = "identityPerson1"
                )
            ),
            Infraction(
                id = UUID.randomUUID().toString(),
                actionId = actionId,
                controlId = UUID.randomUUID(),
                missionId = 145,
                controlType = ControlType.SECURITY,
                infractionType = InfractionTypeEnum.WITH_REPORT,
                target = InfractionTarget(
                    id = UUID.randomUUID().toString(),
                    vesselIdentifier = "firstVesselIdentifier",
                    identityControlledPerson = "identityPerson2"
                )
            ),
            Infraction(
                id = UUID.randomUUID().toString(),
                actionId = actionId,
                missionId = 145,
                controlId = UUID.randomUUID(),
                controlType = ControlType.ADMINISTRATIVE,
                infractionType = InfractionTypeEnum.WITH_REPORT,
                target = InfractionTarget(
                    id = UUID.randomUUID().toString(),
                    identityControlledPerson = "identityPerson3"
                )
            ),
            Infraction(
                id = UUID.randomUUID().toString(),
                actionId = actionId,
                missionId = 145,
                controlId = UUID.randomUUID(),
                controlType = ControlType.GENS_DE_MER,
                infractionType = InfractionTypeEnum.WITH_REPORT,
                target = InfractionTarget(
                    id = UUID.randomUUID().toString(),
                    vesselIdentifier = "firstVesselIdentifier",
                    identityControlledPerson = "identityPerson4",
                )
            ),
            Infraction(
                id = UUID.randomUUID().toString(),
                actionId = actionId,
                missionId = 145,
                controlId = UUID.randomUUID(),
                controlType = ControlType.NAVIGATION,
                infractionType = InfractionTypeEnum.WITH_REPORT,
                target = InfractionTarget(
                    id = UUID.randomUUID().toString(),
                    vesselIdentifier = "secondVesselIdentifier",
                    identityControlledPerson = "identityPerson5",
                )
            ),
            Infraction(
                id = UUID.randomUUID().toString(),
                actionId = actionId,
                missionId = 145,
                controlId = UUID.randomUUID(),
                controlType = ControlType.SECURITY,
                infractionType = InfractionTypeEnum.WITH_REPORT,
                target = InfractionTarget(
                    id = UUID.randomUUID().toString(),
                    identityControlledPerson = "identityPerson1",
                )
            )
        )
    }

}
