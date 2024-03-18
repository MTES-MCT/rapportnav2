package fr.gouv.dgampa.rapportnav.infrastructure.bff.model

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.Action
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

data class MissionReportStatus(
    val status: MissionReportStatusEnum,
    val source: List<MissionSourceEnum>? = null
)

data class Mission(
    val id: Int,
    val isClosed: Boolean,
    val missionSource: MissionSourceEnum,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime?,
    val actions: List<Action>?,
    val openBy: String? = null,
    val status: MissionStatusEnum? = null,
    val reportStatus: List<MissionReportStatus>? = null
) {

    private val logger = LoggerFactory.getLogger(Mission::class.java)

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

    fun calculateMissionStatus(): MissionStatusEnum {
        val compareDate = ZonedDateTime.now()
        val endDateTimeUtc = this.endDateTimeUtc
        val isClosed = this.isClosed
        val startDateTimeUtc = this.startDateTimeUtc

        if (isClosed) {
            return MissionStatusEnum.CLOSED
        }

        try {
            val startDateTime = ZonedDateTime.parse(startDateTimeUtc.toString())
            if (startDateTime.isAfter(compareDate) || startDateTime.isEqual(compareDate)) {
                return MissionStatusEnum.UPCOMING
            }
        } catch (e: DateTimeParseException) {
            logger.error("Mission > calculateMissionStatus - error with startDateTime", e)
            return MissionStatusEnum.UNAVAILABLE
        }

        if (endDateTimeUtc != null) {
            try {
                val endDateTime = ZonedDateTime.parse(endDateTimeUtc.toString())
                if (endDateTime.isAfter(compareDate)) {
                    return MissionStatusEnum.IN_PROGRESS
                } else if (endDateTime.isBefore(compareDate) || endDateTime.isEqual(compareDate)) {
                    return MissionStatusEnum.ENDED
                }
            } catch (e: DateTimeParseException) {
                logger.error("Mission > calculateMissionStatus - error with endDateTime", e)
                return MissionStatusEnum.UNAVAILABLE
            }
        }

        return MissionStatusEnum.PENDING
    }

    fun calculateMissionReportStatus(): List<MissionReportStatus>? {

        // loop over actions and check status
        return listOf(
            MissionReportStatus(
                status = MissionReportStatusEnum.INCOMPLETE,
                source = listOf(MissionSourceEnum.MONITORENV)
            )
        )


    }
}
