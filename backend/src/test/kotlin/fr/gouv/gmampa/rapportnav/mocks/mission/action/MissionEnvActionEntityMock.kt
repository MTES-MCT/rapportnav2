package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTargetTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.tags.TagEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes.ThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import org.locationtech.jts.geom.Geometry
import java.time.Instant
import java.util.UUID

object MissionEnvActionEntityMock {
    fun create(
        id: UUID = UUID.randomUUID(),
        missionId: Int = 1,
        envActionType: ActionTypeEnum = ActionTypeEnum.CONTROL,
        completedBy: String? = null,
        completion: ActionCompletionEnum? = null,
        geom: Geometry? = null,
        facade: String? = null,
        department: String? = null,
        startDateTimeUtc: Instant? = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        endDateTimeUtc: Instant? = Instant.parse("2019-09-08T23:00:00.000+01:00"),
        openBy: String? = null,
        observations: String? = null,
        observationsByUnit: String? = null,
        actionNumberOfControls: Int? = null,
        actionTargetType: ActionTargetTypeEnum? = null,
        vehicleType: VehicleTypeEnum? = null,
        envInfractions: List<InfractionEnvEntity>? = listOf(),
        coverMissionZone: Boolean? = null,
        controlsToComplete: List<ControlType>? = listOf(),
        availableControlTypesForInfraction: List<ControlType>? = listOf(),
        isAdministrativeControl: Boolean? = null,
        isComplianceWithWaterRegulationsControl: Boolean? = null,
        isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
        isSeafarersControl: Boolean? = null,
        targets: List<TargetEntity>? = null,
        tags: List<TagEntity>? = listOf(),
        themes: List<ThemeEntity>? = listOf(),
    ): MissionEnvActionEntity {
        return MissionEnvActionEntity(
            id = id,
            missionId = missionId,
            envActionType = envActionType,
            completedBy = completedBy,
            completion = completion,
            geom = geom,
            facade = facade,
            department = department,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            openBy = openBy,
            observations = observations,
            observationsByUnit = observationsByUnit,
            actionNumberOfControls = actionNumberOfControls,
            actionTargetType = actionTargetType,
            vehicleType = vehicleType,
            envInfractions = envInfractions,
            coverMissionZone = coverMissionZone,
            controlsToComplete = controlsToComplete,
            availableControlTypesForInfraction = availableControlTypesForInfraction,
            isAdministrativeControl = isAdministrativeControl,
            isComplianceWithWaterRegulationsControl = isComplianceWithWaterRegulationsControl,
            isSafetyEquipmentAndStandardsComplianceControl = isSafetyEquipmentAndStandardsComplianceControl,
            isSeafarersControl = isSeafarersControl,
            targets = targets,
            tags = tags,
            themes = themes,
        )

    }
}
