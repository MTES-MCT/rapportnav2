package fr.gouv.dgampa.rapportnav.infrastructure.bff.model

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.Action
import java.time.ZonedDateTime

data class Mission(
    val id: Int,
    val isClosed: Boolean,
    val missionSource: MissionSourceEnum,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime?,
    val actions: List<Action>?,
    val openBy: String? = null,
) {
    companion object {
        fun fromMissionEntity(mission: MissionEntity): Mission {
            val actions: List<Action>? = mission.actions?.mapNotNull { missionAction ->
                when (missionAction) {
                    is MissionActionEntity.EnvAction -> Action.fromEnvAction(
                        missionAction.envAction,
                        missionId = mission.id
                    )

                    is MissionActionEntity.FishAction -> Action.fromFishAction(
                        missionAction.fishAction,
                        missionId = mission.id
                    )

                    is MissionActionEntity.NavAction -> Action.fromNavAction(missionAction.navAction)
                }
            }

            return Mission(
                id = mission.id,
                isClosed = mission.isClosed,
                missionSource = mission.missionSource,
                startDateTimeUtc = mission.startDateTimeUtc,
                endDateTimeUtc = mission.endDateTimeUtc,
                actions = actions,
                openBy = mission.openBy,
            )
        }

    }
}
