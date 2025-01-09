package fr.gouv.dgampa.rapportnav.domain.utils

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.BaseAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity

class AEMUtils {
    companion object {
        fun getDurationInHours(actions: List<BaseAction?>): Double {
            return actions.fold(0.0) { acc, action ->
                acc.plus(
                    ComputeDurationUtils.durationInHours(
                        startDateTimeUtc = action?.startDateTimeUtc,
                        endDateTimeUtc = action?.endDateTimeUtc
                    )
                )
            }
        }

        fun getEnvDurationInHours(actions: List<ExtendedEnvActionEntity?>): Double {
            return actions.fold(0.0) { acc, envAction ->
                acc.plus(
                    ComputeDurationUtils.durationInHours(
                        startDateTimeUtc = envAction?.controlAction?.action?.actionStartDateTimeUtc,
                        endDateTimeUtc = envAction?.controlAction?.action?.actionEndDateTimeUtc
                    )
                ).plus(
                    ComputeDurationUtils.durationInHours(
                        startDateTimeUtc = envAction?.surveillanceAction?.action?.actionStartDateTimeUtc,
                        endDateTimeUtc = envAction?.surveillanceAction?.action?.actionEndDateTimeUtc
                    )
                )
            }
        }

        fun getDurationInHours2(actions: List<MissionActionEntity?>): Double {
            return actions.fold(0.0) { acc, action ->
                acc.plus(
                    ComputeDurationUtils.durationInHours(
                        startDateTimeUtc = action?.startDateTimeUtc,
                        endDateTimeUtc = action?.endDateTimeUtc
                    )
                )
            }
        }
    }
}
