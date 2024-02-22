package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import java.time.Duration
import java.time.ZonedDateTime

class GetStatusDurationsThroughoutMission() {
    fun execute(missionStartDateTime: ZonedDateTime, missionEndDateTime: ZonedDateTime, statuses: List<ActionStatusEntity>?): List<Any>? {
        return statuses?.let {



//            [
//                {status: ActionStatusType.NAVIGATING, value: 123, reason: ActionStatusReason.ADMINISTRATION },
//                {status: ActionStatusType.NAVIGATING, value: 123, reason: ActionStatusReason.METEO },
//            ]




            statuses.mapIndexed { index, actionStatusEntity ->
                {
                    var hours: Long? = null;
                    // 1. calcul duree
                    if (index === 0) {
                        // duree avec missionStartDateTime
                        val duration = Duration.between(missionStartDateTime, actionStatusEntity.startDateTimeUtc)
                        hours = duration.toHours()
                        val minutes = duration.toMinutes() % 60

                        if (minutes > 30) {
                            hours += 1
                        }
                    } else if (index === statuses.size - 1) {
                        val duration = Duration.between(actionStatusEntity.startDateTimeUtc, missionEndDateTime)
                        hours = duration.toHours()
                        val minutes = duration.toMinutes() % 60
                    } else {
                        // duree avec element precedent
                        val duration = Duration.between(
                            statuses.get(index - 1).startDateTimeUtc,
                            actionStatusEntity.startDateTimeUtc
                        )
                        hours = duration.toHours()
                    }
                }
            }
        }
    }
}
