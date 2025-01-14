package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.CreateNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [CreateNavAction::class])
@ContextConfiguration(classes = [CreateNavAction::class])
class CreateNavActionTest {
    @MockitoBean
    private lateinit var missionActionRepository: INavMissionActionRepository

    @Test
    fun `test execute create nav action`() {
        val actionId = UUID.randomUUID().toString()
        val input = MissionNavAction(
            id = actionId,
            missionId = 761,
            actionType = ActionType.CONTROL,
            source = MissionSourceEnum.RAPPORTNAV,
            data = getNavActionDataInput(),
        )
        val model = MissionActionModelMock.create()
        `when`(missionActionRepository.save(anyOrNull())).thenReturn(model)

        val createNavAction = CreateNavAction(
            missionActionRepository = missionActionRepository
        )
        val response = createNavAction.execute(input)
        assertThat(response).isNotNull
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
