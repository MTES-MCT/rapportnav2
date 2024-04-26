package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionSurveillanceEntity
import org.locationtech.jts.geom.Geometry
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

object EnvActionSurveillanceMock {
    fun create(
        id: UUID = UUID.randomUUID(),
        actionStartDateTimeUtc: ZonedDateTime? = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 0), ZoneOffset.UTC),
        actionEndDateTimeUtc: ZonedDateTime? = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 14, 0), ZoneOffset.UTC),
        geom: Geometry? = null,
        facade: String? = null,
        department: String? = null,
        controlPlans: List<EnvActionControlPlanEntity>? = null,
        observations: String? = null,
        completion: ActionCompletionEnum = ActionCompletionEnum.COMPLETED,
        completedBy: String? = null,
        openBy: String? = null,
        coverMissionZone: Boolean? = null,
    ): EnvActionSurveillanceEntity {
        return EnvActionSurveillanceEntity(
            id = id,
            actionStartDateTimeUtc = actionStartDateTimeUtc,
            actionEndDateTimeUtc = actionEndDateTimeUtc,
            geom = geom,
            facade = facade,
            department = department,
            controlPlans = controlPlans,
            observations = observations,
            completion = completion,
            openBy = openBy,
            completedBy = completedBy,
            coverMissionZone = coverMissionZone
            // Set other properties to their default values or mocks as needed
        )
    }
}
