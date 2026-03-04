package fr.gouv.gmampa.rapportnav.domain.entities.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant

@ExtendWith(SpringExtension::class)
class MissionNavActionEntityTest {

    @Test
    fun `execute should not complete for stats until all is filled action type NOTE`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.NOTE)
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(true)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type  ANTI_POLLUTION`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.ANTI_POLLUTION)
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.latitude = 2.0
        entity.longitude = 2.0
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(true)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }


    @Test
    fun `execute should not complete for stats until all is filled action type  BAAEM_PERMANENCE`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.BAAEM_PERMANENCE)
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(true)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }


    @Test
    fun `execute should not complete for stats until all is filled action type VIGIMER`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.VIGIMER)
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(true)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type REPRESENTATION`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.REPRESENTATION)
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(true)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type PUBLIC_ORDER`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.PUBLIC_ORDER)
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(true)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type NAUTICAL_EVENT`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.NAUTICAL_EVENT)
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(true)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type ILLEGAL_IMMIGRATION`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.ILLEGAL_IMMIGRATION)
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()

        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.nbOfInterceptedVessels = 5
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.nbOfInterceptedMigrants = 10
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.nbOfSuspectedSmugglers = 3
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.latitude = 2.0
        entity.longitude = 2.0
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(true)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)

    }

    @Test
    fun `execute should not complete for stats until all is filled action type RESCUE`() {
        val entity = MissionNavActionEntityMock.create(
            actionType = ActionType.RESCUE, isPersonRescue = true,
            isMigrationRescue = false
        )
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.latitude = 345.0
        entity.computeCompleteness()

        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.longitude = 897.0
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.numberPersonsRescued = 3
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.numberOfDeaths = 4
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.isMigrationRescue = true
        entity.isPersonRescue = false
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.nbOfVesselsTrackedWithoutIntervention = 9
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.nbAssistedVesselsReturningToShore = 2
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.now()
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(true)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type STATUS`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.STATUS)
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.status = ActionStatusType.DOCKED
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.reason = ActionStatusReason.ADMINISTRATION
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(true)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should not complete for stats until all is filled action type CONTROL`() {
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.CONTROL)
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.endDateTimeUtc = Instant.parse("2019-09-08T24:00:00.000+01:00")
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.latitude = 345.0
        entity.computeCompleteness()

        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.longitude = 897.0
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.controlMethod = ControlMethod.SEA
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.vesselIdentifier = "My vessel identifier"
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.vesselType = VesselTypeEnum.SAILING
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.vesselSize = VesselSizeEnum.LESS_THAN_12m
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(false)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)

        entity.identityControlledPerson = "My identify person"
        entity.computeCompleteness()
        Assertions.assertThat(entity.isCompleteForStats).isEqualTo(true)
        Assertions.assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute return all infractions on action`() {
        val target = TargetEntityMock.create()
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.CONTROL, targets = listOf(target))
        val infractionIds = entity.getAllInfractions().map { it.id }.toSet()
        val mockInfractionIds = target.controls?.flatMap { it.infractions!! }?.map { it.id }?.toSet()
        Assertions.assertThat(entity).isNotNull
        Assertions.assertThat(infractionIds).isEqualTo(mockInfractionIds)
    }

    @Test
    fun `execute return infractions by control type`() {
        val target = TargetEntityMock.create()
        val entity = MissionNavActionEntityMock.create(actionType = ActionType.CONTROL, targets = listOf(target))
        val infractionIds = entity.getInfractionByControlType(ControlType.GENS_DE_MER).map { it.id }.toSet()
        val mockInfractionIds =
            target.controls?.filter { it.controlType == ControlType.GENS_DE_MER }?.flatMap { it.infractions!! }
                ?.map { it.id }?.toSet()
        Assertions.assertThat(entity).isNotNull
        Assertions.assertThat(infractionIds).isEqualTo(mockInfractionIds)
    }

    @Test
    fun `isWithinMissionDates returns true when mission has no start date`() {
        val entity = MissionNavActionEntityMock.create(
            startDateTimeUtc = Instant.parse("2024-01-15T10:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-01-15T12:00:00Z")
        )
        Assertions.assertThat(entity.isWithinMissionDates(null, null)).isTrue()
    }

    @Test
    fun `isWithinMissionDates returns true when action has no start date`() {
        val entity = MissionNavActionEntityMock.create(startDateTimeUtc = null)
        val missionStart = Instant.parse("2024-01-01T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-31T23:59:59Z")
        Assertions.assertThat(entity.isWithinMissionDates(missionStart, missionEnd)).isTrue()
    }

    @Test
    fun `isWithinMissionDates returns true when action dates are within mission dates`() {
        val entity = MissionNavActionEntityMock.create(
            startDateTimeUtc = Instant.parse("2024-01-15T10:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-01-15T12:00:00Z")
        )
        val missionStart = Instant.parse("2024-01-01T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-31T23:59:59Z")
        Assertions.assertThat(entity.isWithinMissionDates(missionStart, missionEnd)).isTrue()
    }

    @Test
    fun `isWithinMissionDates returns false when action start is before mission start`() {
        val entity = MissionNavActionEntityMock.create(
            startDateTimeUtc = Instant.parse("2023-12-31T10:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-01-15T12:00:00Z")
        )
        val missionStart = Instant.parse("2024-01-01T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-31T23:59:59Z")
        Assertions.assertThat(entity.isWithinMissionDates(missionStart, missionEnd)).isFalse()
    }

    @Test
    fun `isWithinMissionDates returns false when action start is after mission end`() {
        val entity = MissionNavActionEntityMock.create(
            startDateTimeUtc = Instant.parse("2024-02-15T10:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-02-15T12:00:00Z")
        )
        val missionStart = Instant.parse("2024-01-01T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-31T23:59:59Z")
        Assertions.assertThat(entity.isWithinMissionDates(missionStart, missionEnd)).isFalse()
    }

    @Test
    fun `isWithinMissionDates returns false when action end is after mission end`() {
        val entity = MissionNavActionEntityMock.create(
            startDateTimeUtc = Instant.parse("2024-01-30T10:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-02-15T12:00:00Z")
        )
        val missionStart = Instant.parse("2024-01-01T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-31T23:59:59Z")
        Assertions.assertThat(entity.isWithinMissionDates(missionStart, missionEnd)).isFalse()
    }

    @Test
    fun `isWithinMissionDates returns true when mission has no end date and action is after mission start`() {
        val entity = MissionNavActionEntityMock.create(
            startDateTimeUtc = Instant.parse("2024-01-15T10:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-02-15T12:00:00Z")
        )
        val missionStart = Instant.parse("2024-01-01T00:00:00Z")
        Assertions.assertThat(entity.isWithinMissionDates(missionStart, null)).isTrue()
    }

    @Test
    fun `isWithinMissionDates returns false when mission has no end date and action is before mission start`() {
        val entity = MissionNavActionEntityMock.create(
            startDateTimeUtc = Instant.parse("2023-12-31T10:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-01-15T12:00:00Z")
        )
        val missionStart = Instant.parse("2024-01-01T00:00:00Z")
        Assertions.assertThat(entity.isWithinMissionDates(missionStart, null)).isFalse()
    }

    @Test
    fun `isWithinMissionDates returns true when action has no end date and start is within mission dates`() {
        val entity = MissionNavActionEntityMock.create(
            startDateTimeUtc = Instant.parse("2024-01-15T10:00:00Z"),
            endDateTimeUtc = null
        )
        val missionStart = Instant.parse("2024-01-01T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-31T23:59:59Z")
        Assertions.assertThat(entity.isWithinMissionDates(missionStart, missionEnd)).isTrue()
    }

}
