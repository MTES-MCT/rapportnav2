package fr.gouv.gmampa.rapportnav.infrastructure.bff.model

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvActionOutput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class MissionEnvActionOutputTest {

    @Test
    fun `execute should retrieve output from mission action env Entity`() {
       val envAction = getEnvAction();
        val entity =  MissionEnvActionEntity.fromEnvAction(761, envAction)
        val output = MissionEnvActionOutput.fromMissionActionEntity(entity)
        assertThat(output).isNotNull()
        assertThat(output.id).isEqualTo(entity.id.toString())
        assertThat(output.data.startDateTimeUtc).isEqualTo(entity.actionStartDateTimeUtc)
        assertThat(output.data.endDateTimeUtc).isEqualTo(entity.actionEndDateTimeUtc)
        assertThat(output.data.observations).isEqualTo(entity.observations)
        assertThat(output.data.completedBy).isEqualTo(entity.completedBy)
        assertThat(output.data.facade).isEqualTo(entity.facade)
        assertThat(output.data.department).isEqualTo(entity.department)
        assertThat(output.actionType).isEqualTo(entity.actionType)
        assertThat(output.data.isSeafarersControl).isEqualTo(entity.isSeafarersControl)
        assertThat(output.data.isAdministrativeControl).isEqualTo(entity.isAdministrativeControl)
        assertThat(output.data.openBy).isEqualTo(entity.openBy)
        assertThat(output.data.vehicleType).isEqualTo(entity.vehicleType)
        assertThat(output.data.observationsByUnit).isEqualTo(entity.observationsByUnit)
        assertThat(output.data.actionNumberOfControls).isEqualTo(entity.actionNumberOfControls)
        assertThat(output.data.actionTargetType).isEqualTo(entity.actionTargetType)
        assertThat(output.data.isComplianceWithWaterRegulationsControl).isEqualTo(entity.isComplianceWithWaterRegulationsControl)
        assertThat(output.data.isSafetyEquipmentAndStandardsComplianceControl).isEqualTo(entity.isSafetyEquipmentAndStandardsComplianceControl)
        assertThat(output.data.infractions).isEqualTo(entity.infractions)
        assertThat(output.data.formattedControlPlans).isEqualTo(null)

    }


    private fun getEnvAction(): EnvActionEntity {
        return EnvActionControlEntity(
            UUID.randomUUID(),
            completedBy = "completedBy",
            facade = "facade",
            department = "department",
            isSeafarersControl = false,
            isAdministrativeControl = false,
            openBy = "FEFEFE",
            vehicleType = VehicleTypeEnum.VEHICLE_AIR,
            observations = "observations",
            observationsByUnit = "observationsByUnit",
            completion = ActionCompletionEnum.TO_COMPLETE,
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
