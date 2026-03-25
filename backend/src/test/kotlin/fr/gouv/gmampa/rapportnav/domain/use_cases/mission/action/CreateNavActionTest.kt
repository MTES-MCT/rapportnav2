package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.CreateNavAction
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
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant


@SpringBootTest(classes = [CreateNavAction::class])
@ContextConfiguration(classes = [CreateNavAction::class])
class CreateNavActionTest {
    @MockitoBean
    private lateinit var missionActionRepository: INavMissionActionRepository

    @MockitoBean
    private lateinit var entityValidityValidator: EntityValidityValidator

    @BeforeEach
    fun setup() {
        // Default: validation passes
        doNothing().whenever(entityValidityValidator).validateAndThrow(anyOrNull(), anyOrNull())
    }

    @Test
    fun `test execute create nav action`() {
        val entity = MissionNavActionEntityMock.create()
        val input = MissionNavAction.fromMissionActionEntity(entity)
        val model = MissionActionModelMock.create()
        `when`(missionActionRepository.save(anyOrNull())).thenReturn(model)

        val createNavAction = CreateNavAction(
            missionActionRepository = missionActionRepository,
            entityValidityValidator = entityValidityValidator
        )
        val response = createNavAction.execute(input)
        assertThat(response).isNotNull
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
            entityValidityValidator = entityValidityValidator
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
            entityValidityValidator = entityValidityValidator
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
