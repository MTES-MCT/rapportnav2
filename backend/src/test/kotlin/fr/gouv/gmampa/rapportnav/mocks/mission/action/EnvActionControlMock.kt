package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import org.locationtech.jts.geom.Geometry
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

object EnvActionControlMock {
    fun create(
        id: UUID = UUID.randomUUID(),
        actionStartDateTimeUtc: ZonedDateTime? = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 0), ZoneOffset.UTC),
        actionEndDateTimeUtc: ZonedDateTime? = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 14, 0), ZoneOffset.UTC),
        geom: Geometry? = null,
        facade: String? = null,
        department: String? = null,
        isAdministrativeControl: Boolean? = null,
        isComplianceWithWaterRegulationsControl: Boolean? = null,
        isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
        isSeafarersControl: Boolean? = null,
        themes: List<ThemeEntity>? = null,
        observations: String? = null,
        actionNumberOfControls: Int? = null,
        actionTargetType: ActionTargetTypeEnum? = null,
        vehicleType: VehicleTypeEnum? = null,
        infractions: List<InfractionEntity>? = emptyList(),
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
            themes = themes,
            observations = observations,
            actionNumberOfControls = actionNumberOfControls,
            actionTargetType = actionTargetType,
            vehicleType = vehicleType,
            infractions = infractions
            // Set other properties to their default values or mocks as needed
        )
    }
}
