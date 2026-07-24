package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.ResolveActionOwnerId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ComputeActionValidityAndRecomputeMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.UpdateNavAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.argThat
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [UpdateNavAction::class])
@ContextConfiguration(classes = [UpdateNavAction::class])
class UpdateNavActionTest {

    @MockitoBean
    private lateinit var missionActionRepository: INavMissionActionRepository

    @MockitoBean
    private lateinit var processMissionActionTarget: ProcessMissionActionTarget

    @MockitoBean
    private lateinit var entityValidityValidator: EntityValidityValidator

    @MockitoBean
    private lateinit var computeActionValidityAndRecomputeMission: ComputeActionValidityAndRecomputeMission

    @MockitoBean
    private lateinit var resolveActionOwnerId: ResolveActionOwnerId

    private fun useCase() = UpdateNavAction(
        missionActionRepository = missionActionRepository,
        processMissionActionTarget = processMissionActionTarget,
        entityValidityValidator = entityValidityValidator,
        computeActionValidityAndRecomputeMission = computeActionValidityAndRecomputeMission,
        resolveActionOwnerId = resolveActionOwnerId
    )

    @Test
    fun `test execute update nav action`() {
        val actionId = UUID.randomUUID().toString()
        val input = navInput(actionId)
        `when`(missionActionRepository.save(anyOrNull())).thenReturn(MissionActionModelMock.create())
        `when`(processMissionActionTarget.execute(anyOrNull(), anyOrNull())).thenReturn(listOf(TargetEntityMock.create()))

        val response = useCase().execute(actionId, input)

        assertThat(response).isNotNull
    }

    @Test
    fun `delegates validation persistence to ComputeActionValidityAndRecomputeMission`() {
        val actionId = UUID.randomUUID().toString()
        val input = navInput(actionId)
        `when`(missionActionRepository.save(anyOrNull())).thenReturn(MissionActionModelMock.create())
        `when`(processMissionActionTarget.execute(anyOrNull(), anyOrNull())).thenReturn(listOf(TargetEntityMock.create()))

        useCase().execute(actionId, input)

        // Validity is computed on the action, and the mission is recomputed only AFTER the row is saved
        // (otherwise the aggregate would re-read the stale, pre-update action).
        verify(computeActionValidityAndRecomputeMission).computeActionValidity(argThat { this.getActionId() == actionId }, anyOrNull(), anyOrNull())
        inOrder(missionActionRepository, computeActionValidityAndRecomputeMission).apply {
            verify(missionActionRepository).save(anyOrNull())
            verify(computeActionValidityAndRecomputeMission).recomputeMission(argThat { this.getActionId() == actionId }, anyOrNull())
        }
    }

    @Test
    fun `stamps ownerId resolved from the path owner before saving`() {
        val actionId = UUID.randomUUID().toString()
        val ownerUuid = UUID.randomUUID()
        val input = navInput(actionId)
        whenever(resolveActionOwnerId.execute("761")).thenReturn(ownerUuid)
        `when`(missionActionRepository.save(anyOrNull())).thenReturn(MissionActionModelMock.create())
        `when`(processMissionActionTarget.execute(anyOrNull(), anyOrNull())).thenReturn(listOf(TargetEntityMock.create()))

        useCase().execute(actionId, input, ownerId = "761")

        verify(missionActionRepository).save(argThat { this.ownerId == ownerUuid })
    }

    @Test
    fun `test throws exception when id different of action id`() {
        val actionId = UUID.randomUUID().toString()
        val input = navInput(UUID.randomUUID().toString())

        val exception = assertThrows<BackendUsageException> {
            useCase().execute(actionId, input)
        }
        assertThat(exception.message).isEqualTo("UpdateNavAction: action id mismatch")
    }

    private fun navInput(id: String) = MissionNavAction(
        id = id,
        missionId = 761,
        ownerId = UUID.randomUUID().toString(),
        actionType = ActionType.CONTROL,
        source = MissionSourceEnum.RAPPORT_NAV,
        data = getNavActionDataInput(),
    )

    private fun getNavActionDataInput() = MissionNavActionData(
        startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
        isAntiPolDeviceDeployed = true,
        isSimpleBrewingOperationDone = true,
        diversionCarriedOut = true,
        latitude = 3434.0,
        longitude = 4353.0,
        detectedPollution = false,
        pollutionObservedByAuthorizedAgent = false,
        controlMethod = ControlMethod.SEA,
        locationType = null,
        vesselIdentifier = "vesselIdentifier",
        vesselType = VesselTypeEnum.FISHING,
        vesselSize = VesselSizeEnum.LESS_THAN_12m,
        identityControlledPerson = "identityControlledPerson",
        nbOfInterceptedVessels = 4,
        nbOfInterceptedMigrants = 64,
        nbOfSuspectedSmugglers = 67,
        isVesselRescue = false,
        isPersonRescue = true,
        isVesselNoticed = true,
        isVesselTowed = true,
        isInSRRorFollowedByCROSSMRCC = false,
        numberPersonsRescued = 4,
        numberOfDeaths = 90,
        operationFollowsDEFREP = false,
        locationDescription = "locationDescription",
        isMigrationRescue = false,
        nbOfVesselsTrackedWithoutIntervention = 4,
        nbAssistedVesselsReturningToShore = 50,
        reason = ActionStatusReason.ADMINISTRATION,
        status = ActionStatusType.ANCHORED
    )
}
