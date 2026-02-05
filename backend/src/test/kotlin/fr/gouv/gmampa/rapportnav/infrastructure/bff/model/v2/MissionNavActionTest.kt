package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EstablishmentEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant

@ExtendWith(SpringExtension::class)
class MissionNavActionTest {

    @Test
    fun `execute should not complete for stats until all is filled action type NOTE`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.NOTE)
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(true)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type  ANTI_POLLUTION`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.ANTI_POLLUTION)
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.latitude = 2.0
        entity.longitude = 2.0
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(true)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }


    @Test
    fun `execute should not complete for stats until all is filled action type  BAAEM_PERMANENCE`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.BAAEM_PERMANENCE)
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(true)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }


    @Test
    fun `execute should not complete for stats until all is filled action type VIGIMER`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.VIGIMER)
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(true)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type REPRESENTATION`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.REPRESENTATION)
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(true)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type PUBLIC_ORDER`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.PUBLIC_ORDER)
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(true)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type NAUTICAL_EVENT`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.NAUTICAL_EVENT)
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(true)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type ILLEGAL_IMMIGRATION`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.ILLEGAL_IMMIGRATION)
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()

        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.nbOfInterceptedVessels = 5
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.nbOfInterceptedMigrants = 10
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.nbOfSuspectedSmugglers = 3
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.latitude = 2.0
        entity.longitude = 2.0
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(true)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)

    }

    @Test
    fun `execute should not complete for stats until all is filled action type RESCUE`() {
        val entity = MissionNavActionEntityMock.create(
            actionType = ActionType.RESCUE, isPersonRescue = true,
            isMigrationRescue = false
        )
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.latitude = 345.0
        entity.computeCompleteness()

        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.longitude = 897.0
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.numberPersonsRescued = 3
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.numberOfDeaths = 4
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.isMigrationRescue = true
        entity.isPersonRescue = false
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.nbOfVesselsTrackedWithoutIntervention = 9
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.nbAssistedVesselsReturningToShore = 2
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.now()
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(true)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type STATUS`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.STATUS)
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.status = ActionStatusType.DOCKED
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.reason = ActionStatusReason.ADMINISTRATION
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(true)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type CONTROL`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.CONTROL)
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.latitude = 345.0
        entity.computeCompleteness()

        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.longitude = 897.0
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.controlMethod = ControlMethod.SEA
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.vesselIdentifier = "My vessel identifier"
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.vesselType = VesselTypeEnum.SAILING
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.vesselSize = VesselSizeEnum.LESS_THAN_12m
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.identityControlledPerson = "My identify person"
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(true)
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute return all infractions on action`() {
        val target = TargetEntityMock.create()
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.CONTROL, targets = listOf(target))
        val infractionIds = entity.getAllInfractions().map { it.id }.toSet()
        val mockInfractionIds = target.controls?.flatMap { it.infractions!! }?.map { it.id }?.toSet()
        assertThat(entity).isNotNull
        assertThat(infractionIds).isEqualTo(mockInfractionIds)
    }

    @Test
    fun `execute return  infractions by control type`() {
        val target = TargetEntityMock.create()
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.CONTROL, targets = listOf(target))
        val infractionIds = entity.getInfractionByControlType(ControlType.GENS_DE_MER).map { it.id }.toSet()
        val mockInfractionIds =
            target.controls?.filter { it.controlType == ControlType.GENS_DE_MER }?.flatMap { it.infractions!! }
                ?.map { it.id }?.toSet()
        assertThat(entity).isNotNull
        assertThat(infractionIds).isEqualTo(mockInfractionIds)
    }


    @Test
    fun `execute have establishment`() {
        val entity = MissionNavActionEntityMock.create()
        entity.establishment = EstablishmentEntity(id= 2, name= "myEstablishment")
        val action = MissionNavAction.fromMissionActionEntity(action = entity)

        assertThat(action).isNotNull
        assertThat(action.data.establishment).isNotNull
        assertThat(action.data.establishment?.name).isEqualTo(entity.establishment?.name)
    }
}
