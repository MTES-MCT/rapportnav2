package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
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
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.UpdateNavAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.MissionDatesOutput
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.argThat
import org.mockito.kotlin.eq
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
    private lateinit var getMissionDates: GetMissionDates

    @MockitoBean
    private lateinit var resolveActionOwnerId: ResolveActionOwnerId

    private val validResult = CompletenessForStatsEntity(
        status = CompletenessForStatsStatusEnum.VALID,
        errors = emptyList()
    )

    @Test
    fun `test execute update nav action`() {
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
        `when`(entityValidityValidator.validate(any(), eq(ValidateThrowsBeforeSave::class.java))).thenReturn(validResult)
        `when`(missionActionRepository.save(anyOrNull())).thenReturn(model)
        `when`(
            processMissionActionTarget.execute(
                anyOrNull(),
                anyOrNull()
            )
        ).thenReturn(listOf(TargetEntityMock.create()))
        `when`(getMissionDates.execute(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(
            MissionDatesOutput(
                startDateTimeUtc = Instant.parse("2019-09-01T00:00:00Z"),
                endDateTimeUtc = Instant.parse("2019-09-10T00:00:00Z")
            )
        )

        val updateNavAction = UpdateNavAction(
            missionActionRepository = missionActionRepository,
            processMissionActionTarget = processMissionActionTarget,
            entityValidityValidator = entityValidityValidator,
            getMissionDates = getMissionDates,
            resolveActionOwnerId = resolveActionOwnerId
        )

        val response = updateNavAction.execute(actionId, input)
        assertThat(response).isNotNull
    }

    @Test
    fun `stamps ownerId resolved from the path owner before saving`() {
        val actionId = UUID.randomUUID().toString()
        val ownerUuid = UUID.randomUUID()
        val input = MissionNavAction(
            id = actionId,
            missionId = 761,
            ownerId = UUID.randomUUID().toString(),
            actionType = ActionType.CONTROL,
            source = MissionSourceEnum.RAPPORT_NAV,
            data = getNavActionDataInput(),
        )
        whenever(resolveActionOwnerId.execute("761")).thenReturn(ownerUuid)
        `when`(entityValidityValidator.validate(any(), eq(ValidateThrowsBeforeSave::class.java))).thenReturn(validResult)
        `when`(missionActionRepository.save(anyOrNull())).thenReturn(MissionActionModelMock.create())
        `when`(processMissionActionTarget.execute(anyOrNull(), anyOrNull())).thenReturn(listOf(TargetEntityMock.create()))
        `when`(getMissionDates.execute(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(
            MissionDatesOutput(
                startDateTimeUtc = Instant.parse("2019-09-01T00:00:00Z"),
                endDateTimeUtc = Instant.parse("2019-09-10T00:00:00Z")
            )
        )

        val updateNavAction = UpdateNavAction(
            missionActionRepository = missionActionRepository,
            processMissionActionTarget = processMissionActionTarget,
            entityValidityValidator = entityValidityValidator,
            getMissionDates = getMissionDates,
            resolveActionOwnerId = resolveActionOwnerId
        )

        updateNavAction.execute(actionId, input, ownerId = "761")

        verify(missionActionRepository).save(argThat { this.ownerId == ownerUuid })
    }


    @Test
    fun `test throws exception when id different of action id`() {
        val actionId = UUID.randomUUID().toString()
        val input = MissionNavAction(
            id = UUID.randomUUID().toString(),
            missionId = 761,
            ownerId = UUID.randomUUID().toString(),
            actionType = ActionType.CONTROL,
            source = MissionSourceEnum.RAPPORT_NAV,
            data = getNavActionDataInput(),
        )

        val updateNavAction = UpdateNavAction(
            missionActionRepository = missionActionRepository,
            processMissionActionTarget = processMissionActionTarget,
            entityValidityValidator = entityValidityValidator,
            getMissionDates = getMissionDates,
            resolveActionOwnerId = resolveActionOwnerId
        )

        val exception = assertThrows<BackendUsageException> {
            updateNavAction.execute(actionId, input)
        }
        assertThat(exception.message).isEqualTo("UpdateNavAction: action id mismatch")
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
