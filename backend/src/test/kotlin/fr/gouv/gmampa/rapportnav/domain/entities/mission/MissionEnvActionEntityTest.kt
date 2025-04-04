package fr.gouv.gmampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class MissionEnvActionEntityTest {

    @Test
    fun `execute should retrieve entity from Env action`() {
        val envAction = getEnvAction()
        val entity = MissionEnvActionEntity.fromEnvAction(missionId = 761, action = envAction)
        assertThat(entity).isNotNull()
        assertThat(entity.id).isEqualTo(envAction.id)
        assertThat(entity.startDateTimeUtc).isEqualTo(envAction.actionStartDateTimeUtc)
        assertThat(entity.endDateTimeUtc).isEqualTo(envAction.actionEndDateTimeUtc)
        assertThat(entity.observations).isEqualTo(envAction.observations)
        assertThat(entity.completedBy).isEqualTo(envAction.completedBy)
        assertThat(entity.facade).isEqualTo(envAction.facade)
        assertThat(entity.department).isEqualTo(envAction.department)
        assertThat(entity.actionType.toString()).isEqualTo(envAction.actionType.toString())
        assertThat(entity.isSeafarersControl).isEqualTo(envAction.isSeafarersControl)
        assertThat(entity.isAdministrativeControl).isEqualTo(envAction.isAdministrativeControl)
        assertThat(entity.openBy).isEqualTo(envAction.openBy)
        assertThat(entity.vehicleType).isEqualTo(envAction.vehicleType)
        assertThat(entity.observationsByUnit).isEqualTo(envAction.observationsByUnit)
        assertThat(entity.completion).isEqualTo(envAction.completion)
        assertThat(entity.actionNumberOfControls).isEqualTo(envAction.actionNumberOfControls)
        assertThat(entity.actionTargetType).isEqualTo(envAction.actionTargetType)
        assertThat(entity.isComplianceWithWaterRegulationsControl).isEqualTo(envAction.isComplianceWithWaterRegulationsControl)
        assertThat(entity.isSafetyEquipmentAndStandardsComplianceControl).isEqualTo(envAction.isSafetyEquipmentAndStandardsComplianceControl)
        assertThat(entity.envInfractions).isEqualTo(envAction.infractions)
        assertThat(entity.controlPlans).isEqualTo(envAction.controlPlans)
        assertThat(entity.controlsToComplete).isNotNull
        assertThat(entity.availableControlTypesForInfraction).isNotNull
    }


    @Test
    fun `execute should be complete for stats env `() {
        val envAction = getEnvAction()
        val entity = MissionEnvActionEntity.fromEnvAction(missionId = 761, action = envAction)
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.sourcesOfMissingDataForStats).isEqualTo(listOf(MissionSourceEnum.MONITORENV))
        assertThat(entity.completenessForStats?.sources).isEqualTo(listOf(MissionSourceEnum.MONITORENV))
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)
    }

    @Test
    fun `execute should be complete controls to complete and available controlTypes for infractions `() {
        val envAction = getEnvAction()
        val mockControls = ControlMock.createAllControl()
        mockControls.controlSecurity = null
        mockControls.controlAdministrative = null
        val entity = MissionEnvActionEntity.fromEnvAction(missionId = 761, action = envAction)
        entity.computeControls( mockControls)
        entity.computeCompleteness()
        assertThat(entity.controlsToComplete).isEqualTo(listOf( ControlType.SECURITY))
        assertThat(entity.availableControlTypesForInfraction).isEqualTo(
            listOf(
                ControlType.NAVIGATION,
                ControlType.SECURITY,
                ControlType.GENS_DE_MER
            )
        )
    }


    @Test
    fun `execute should not be complete if end date is null`() {
        val envAction = getEnvAction(
            completion = ActionCompletionEnum.COMPLETED
        )
        val entity = MissionEnvActionEntity.fromEnvAction(missionId = 761, action = envAction)
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(true)

        entity.endDateTimeUtc = null
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(false)
    }

    @Test
    fun `execute should compute control`() {
        val model = getEnvAction()
        val controls = ActionControlEntity(
            controlSecurity = ControlSecurityEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionControlId = "MyActionId1",
                amountOfControls = 0,
                hasBeenDone = true
            ),
            controlGensDeMer = ControlGensDeMerEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionControlId = "MyActionId2",
                amountOfControls = 0
            ),
            controlNavigation = ControlNavigationEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionControlId =  "MyActionId3",
                amountOfControls = 0,
                hasBeenDone = false
            ),
            controlAdministrative = ControlAdministrativeEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionControlId = "MyActionId4",
                amountOfControls = 5,
                hasBeenDone = true
            )
        )
        val entity = MissionEnvActionEntity.fromEnvAction(761, model)
        entity.computeControls(controls = controls)
        entity.computeControlsToComplete()
        assertThat(entity.controlsToComplete?.size).isEqualTo(3)
    }

    @Test
    fun `execute should compute control to complete 2`() {
        val envAction = getEnvAction(
            completion = ActionCompletionEnum.COMPLETED,

        )
        val entity = MissionEnvActionEntity.fromEnvAction(missionId = 761, action = envAction)
        entity.targets = listOf(TargetMissionMock.create())
        entity.computeControlsToComplete2()
        assertThat(entity.controlsToComplete?.size).isEqualTo(2)
        assertThat(entity.controlsToComplete).contains(ControlType.GENS_DE_MER)
        assertThat(entity.controlsToComplete).contains(ControlType.NAVIGATION)
    }

    private fun getEnvAction(completion: ActionCompletionEnum? = null): EnvActionEntity {
        return EnvActionControlEntity(
            UUID.randomUUID(),
            completedBy = "completedBy",
            facade = "facade",
            department = "department",
            isSeafarersControl = true,
            isAdministrativeControl = false,
            openBy = "FEFEFE",
            vehicleType = VehicleTypeEnum.VEHICLE_AIR,
            observations = "observations",
            observationsByUnit = "observationsByUnit",
            completion = completion?: ActionCompletionEnum.TO_COMPLETE,
            actionNumberOfControls = 3,
            actionTargetType = ActionTargetTypeEnum.COMPANY,
            isComplianceWithWaterRegulationsControl = true,
            isSafetyEquipmentAndStandardsComplianceControl = true,
            infractions = listOf(
                InfractionEntity(
                    id = "Infraction_Id",
                    infractionType = InfractionTypeEnum.WITH_REPORT,
                    formalNotice = FormalNoticeEnum.NO,
                    toProcess = false
                )
            ),
            controlPlans = listOf(EnvActionControlPlanEntity(themeId = 104, subThemeIds = listOf(143))),
            actionStartDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
            actionEndDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
        )
    }
}
