package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionSurveillanceEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes.ThemeEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.env.ThemeEntityMock
import org.locationtech.jts.geom.Geometry
import java.time.Instant
import java.util.*

object EnvActionSurveillanceMock {
    fun create(
        id: UUID = UUID.randomUUID(),
        actionStartDateTimeUtc: Instant? = Instant.parse("2022-01-02T12:00:01Z"),
        actionEndDateTimeUtc: Instant? = Instant.parse("2022-01-02T14:00:01Z"),
        geom: Geometry? = null,
        facade: String? = null,
        department: String? = null,
        controlPlans: List<EnvActionControlPlanEntity>? = null,
        observations: String? = null,
        completion: ActionCompletionEnum = ActionCompletionEnum.COMPLETED,
        completedBy: String? = null,
        openBy: String? = null,
        coverMissionZone: Boolean? = null,
        themes: List<ThemeEntity> = listOf(ThemeEntityMock.create()),
    ): ActionSurveillanceEnvEntity {
        return ActionSurveillanceEnvEntity(
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
            coverMissionZone = coverMissionZone,
            themes = themes,
        )
    }
}
