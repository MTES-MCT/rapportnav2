package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes.ThemeEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.env.ThemeEntityMock
import org.locationtech.jts.geom.Geometry
import java.time.Instant
import java.util.*

object EnvActionControlMock {
    fun create(
        id: UUID = UUID.randomUUID(),
        actionStartDateTimeUtc: Instant? = Instant.parse("2022-01-02T12:00:01Z"),
        actionEndDateTimeUtc: Instant? = Instant.parse("2022-01-02T14:00:01Z"),
        geom: Geometry? = null,
        facade: String? = null,
        department: String? = null,
        isAdministrativeControl: Boolean? = null,
        isComplianceWithWaterRegulationsControl: Boolean? = null,
        isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
        isSeafarersControl: Boolean? = null,
        controlPlans: List<EnvActionControlPlanEntity>? = null,
        observations: String? = null,
        actionNumberOfControls: Int? = null,
        actionTargetType: ActionTargetTypeEnum? = null,
        vehicleType: VehicleTypeEnum? = null,
        infractions: List<InfractionEnvEntity>? = emptyList(),
        completion: ActionCompletionEnum = ActionCompletionEnum.COMPLETED,
        themes: List<ThemeEntity> = listOf(ThemeEntityMock.create()),
    ): EnvActionControlEntity {
        return EnvActionControlEntity(
            id = id,
            actionStartDateTimeUtc = actionStartDateTimeUtc,
            actionEndDateTimeUtc = actionEndDateTimeUtc,
            geom = geom,
            facade = facade,
            department = department,
            isAdministrativeControl = isAdministrativeControl,
            isComplianceWithWaterRegulationsControl = isComplianceWithWaterRegulationsControl,
            isSafetyEquipmentAndStandardsComplianceControl = isSafetyEquipmentAndStandardsComplianceControl,
            isSeafarersControl = isSeafarersControl,
            controlPlans = controlPlans,
            observations = observations,
            actionNumberOfControls = actionNumberOfControls,
            actionTargetType = actionTargetType,
            vehicleType = vehicleType,
            infractions = infractions,
            completion = completion,
            themes = themes,
        )
    }
}
