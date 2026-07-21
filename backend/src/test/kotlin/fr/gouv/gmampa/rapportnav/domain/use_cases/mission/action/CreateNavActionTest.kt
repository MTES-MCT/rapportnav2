package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.ResolveActionOwnerId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.CreateNavAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ComputeActionValidityAndRecomputeMission
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.argThat
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.UUID


@SpringBootTest(classes = [CreateNavAction::class])
@ContextConfiguration(classes = [CreateNavAction::class])
class CreateNavActionTest {
    @MockitoBean
    private lateinit var missionActionRepository: INavMissionActionRepository

    @MockitoBean
    private lateinit var entityValidityValidator: EntityValidityValidator

    @MockitoBean
    private lateinit var computeActionValidityAndRecomputeMission: ComputeActionValidityAndRecomputeMission

    @MockitoBean
    private lateinit var resolveActionOwnerId: ResolveActionOwnerId

    @BeforeEach
    fun setup() {
        // Default: validation passes
        doNothing().whenever(entityValidityValidator).validateAndThrow(anyOrNull(), anyOrNull())
    }

    @Test
    fun `test execute create nav action`() {
        val actionId = UUID.randomUUID().toString()
        val input = MissionNavAction(
            id = actionId,
            missionId = 761,
            ownerId = UUID.randomUUID().toString(),
            actionType = ActionType.CONTROL,
            source = MissionSourceEnum.RAPPORT_NAV,
            data = getNavActionDataInput(),
        )
        val model = MissionActionModelMock.create()
        `when`(missionActionRepository.save(anyOrNull())).thenReturn(model)

        val createNavAction = CreateNavAction(
            missionActionRepository = missionActionRepository,
            entityValidityValidator = entityValidityValidator,
            computeActionValidityAndRecomputeMission = computeActionValidityAndRecomputeMission,
            resolveActionOwnerId = resolveActionOwnerId
        )
        val response = createNavAction.execute(input)
        assertThat(response).isNotNull
    }

    @Test
    fun `delegates validation persistence to ComputeActionValidityAndRecomputeMission for the created action`() {
        val actionId = UUID.randomUUID().toString()
        val input = MissionNavAction(
            id = actionId,
            missionId = 761,
            ownerId = UUID.randomUUID().toString(),
            actionType = ActionType.CONTROL,
            source = MissionSourceEnum.RAPPORT_NAV,
            data = getNavActionDataInput(),
        )
        `when`(missionActionRepository.save(anyOrNull())).thenReturn(MissionActionModelMock.create())

        val createNavAction = CreateNavAction(
            missionActionRepository = missionActionRepository,
            entityValidityValidator = entityValidityValidator,
            computeActionValidityAndRecomputeMission = computeActionValidityAndRecomputeMission,
            resolveActionOwnerId = resolveActionOwnerId
        )
        createNavAction.execute(input)

        // Validity is computed on the action, and the mission is recomputed only AFTER the row is saved
        // (otherwise the aggregate would re-read the not-yet-persisted action).
        verify(computeActionValidityAndRecomputeMission).computeActionValidity(argThat { this.getActionId() == actionId }, anyOrNull(), anyOrNull())
        inOrder(missionActionRepository, computeActionValidityAndRecomputeMission).apply {
            verify(missionActionRepository).save(anyOrNull())
            verify(computeActionValidityAndRecomputeMission).recomputeMission(argThat { this.getActionId() == actionId }, anyOrNull())
        }
    }

    @Test
    fun `stamps ownerId resolved from the path owner before saving`() {
        val ownerUuid = UUID.randomUUID()
        val input = MissionNavAction(
            id = UUID.randomUUID().toString(),
            missionId = 761,
            ownerId = UUID.randomUUID().toString(),
            actionType = ActionType.CONTROL,
            source = MissionSourceEnum.RAPPORT_NAV,
            data = getNavActionDataInput(),
        )
        whenever(resolveActionOwnerId.execute("761")).thenReturn(ownerUuid)
        `when`(missionActionRepository.save(anyOrNull())).thenReturn(MissionActionModelMock.create())

        val createNavAction = CreateNavAction(
            missionActionRepository = missionActionRepository,
            entityValidityValidator = entityValidityValidator,
            computeActionValidityAndRecomputeMission = computeActionValidityAndRecomputeMission,
            resolveActionOwnerId = resolveActionOwnerId
        )
        createNavAction.execute(input = input, ownerId = "761")

        // the request body carried no ownerId; it must be stamped from the resolved path owner
        verify(missionActionRepository).save(argThat { this.ownerId == ownerUuid })
    }

    @Test
    fun `test execute throws when startDate is outside mission range`() {
        val entity = MissionNavActionEntityMock.create(startDateTimeUtc = Instant.parse("2020-01-01T00:00:00Z"))
        val input = MissionNavAction.fromMissionActionEntity(entity)

        doThrow(
            BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Erreur de validation",
                data = mapOf("fieldErrors" to listOf(
                    mapOf("field" to "_class", "message" to "Les dates doivent être comprises dans les dates de la mission", "rule" to "WithinMissionDateRange")
                ))
            )
        ).whenever(entityValidityValidator).validateAndThrow(anyOrNull(), anyOrNull())

        val createNavAction = CreateNavAction(
            missionActionRepository = missionActionRepository,
            entityValidityValidator = entityValidityValidator,
            computeActionValidityAndRecomputeMission = computeActionValidityAndRecomputeMission,
            resolveActionOwnerId = resolveActionOwnerId
        )

        val exception = assertThrows<BackendUsageException> { createNavAction.execute(input) }
        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION)
        assertThat(exception.data).isNotNull
    }

    @Test
    fun `test execute throws when endDate is outside mission range`() {
        val entity = MissionNavActionEntityMock.create(
            startDateTimeUtc = Instant.parse("2019-09-15T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2020-01-01T00:00:00Z")
        )
        val input = MissionNavAction.fromMissionActionEntity(entity)

        doThrow(
            BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Erreur de validation",
                data = mapOf("fieldErrors" to listOf(
                    mapOf("field" to "_class", "message" to "Les dates doivent être comprises dans les dates de la mission", "rule" to "WithinMissionDateRange")
                ))
            )
        ).whenever(entityValidityValidator).validateAndThrow(anyOrNull(), anyOrNull())

        val createNavAction = CreateNavAction(
            missionActionRepository = missionActionRepository,
            entityValidityValidator = entityValidityValidator,
            computeActionValidityAndRecomputeMission = computeActionValidityAndRecomputeMission,
            resolveActionOwnerId = resolveActionOwnerId
        )

        val exception = assertThrows<BackendUsageException> { createNavAction.execute(input) }
        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION)
        assertThat(exception.data).isNotNull
    }
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
