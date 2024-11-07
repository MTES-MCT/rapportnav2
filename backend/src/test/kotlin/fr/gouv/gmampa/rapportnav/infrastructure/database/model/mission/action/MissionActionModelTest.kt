package fr.gouv.gmampa.rapportnav.infrastructure.database.model.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [MissionActionModel::class])
class MissionActionModelTest {

    @Test
    fun `execute should retrieve action model from entity`() {
        val entity = MissionNavActionEntity(
            missionId = 761,
            id = UUID.fromString("0000-00-00-00-000000"),
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true,
            actionType = ActionType.ILLEGAL_IMMIGRATION,
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
            reason = ActionStatusReason.ADMINISTRATION
        )
        val model = MissionActionModel.fromMissionActionEntity(entity)

        assertThat(model).isNotNull()
        assertThat(model.id).isEqualTo(entity.id)
        assertThat(model.missionId).isEqualTo(entity.missionId)
        assertThat(model.startDateTimeUtc).isEqualTo(entity.startDateTimeUtc)
        assertThat(model.endDateTimeUtc).isEqualTo(entity.endDateTimeUtc)
        assertThat(model.observations).isEqualTo(entity.observations)
        assertThat(model.isAntiPolDeviceDeployed).isEqualTo(entity.isAntiPolDeviceDeployed)
        assertThat(model.isSimpleBrewingOperationDone).isEqualTo(entity.isSimpleBrewingOperationDone)
        assertThat(model.diversionCarriedOut).isEqualTo(entity.diversionCarriedOut)
        assertThat(model.actionType).isEqualTo(entity.actionType.toString())
        assertThat(model.latitude).isEqualTo(entity.latitude)
        assertThat(model.longitude).isEqualTo(entity.longitude)
        assertThat(model.detectedPollution).isEqualTo(entity.detectedPollution)
        assertThat(model.pollutionObservedByAuthorizedAgent).isEqualTo(entity.pollutionObservedByAuthorizedAgent)
        assertThat(model.controlMethod).isEqualTo(entity.controlMethod.toString())
        assertThat(model.vesselIdentifier).isEqualTo(entity.vesselIdentifier)
        assertThat(model.vesselType).isEqualTo(entity.vesselType.toString())
        assertThat(model.vesselSize).isEqualTo(entity.vesselSize.toString())
        assertThat(model.identityControlledPerson).isEqualTo(entity.identityControlledPerson)
        assertThat(model.nbOfInterceptedVessels).isEqualTo(entity.nbOfInterceptedVessels)
        assertThat(model.nbOfInterceptedMigrants).isEqualTo(entity.nbOfInterceptedMigrants)
        assertThat(model.nbOfSuspectedSmugglers).isEqualTo(entity.nbOfSuspectedSmugglers)
        assertThat(model.isVesselRescue).isEqualTo(entity.isVesselRescue)
        assertThat(model.isPersonRescue).isEqualTo(entity.isPersonRescue)
        assertThat(model.isVesselNoticed).isEqualTo(entity.isVesselNoticed)
        assertThat(model.isVesselTowed).isEqualTo(entity.isVesselTowed)
        assertThat(model.isInSRRorFollowedByCROSSMRCC).isEqualTo(entity.isInSRRorFollowedByCROSSMRCC)
        assertThat(model.numberPersonsRescued).isEqualTo(entity.numberPersonsRescued)
        assertThat(model.numberOfDeaths).isEqualTo(entity.numberOfDeaths)
        assertThat(model.operationFollowsDEFREP).isEqualTo(entity.operationFollowsDEFREP)
        assertThat(model.locationDescription).isEqualTo(entity.locationDescription)
        assertThat(model.isMigrationRescue).isEqualTo(entity.isMigrationRescue)
        assertThat(model.nbOfVesselsTrackedWithoutIntervention).isEqualTo(entity.nbOfVesselsTrackedWithoutIntervention)
        assertThat(model.nbAssistedVesselsReturningToShore).isEqualTo(entity.nbAssistedVesselsReturningToShore)
        assertThat(model.reason).isEqualTo(entity.reason.toString())
    }
}
