package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import java.time.Instant
import java.util.*

data class MissionNavEntity(
    val id: UUID,
    val serviceId: Int,
    val openBy: String? = null,
    var completedBy: String? = null,
    var startDateTimeUtc: Instant,
    var endDateTimeUtc: Instant? = null,
    var isDeleted: Boolean = false,
    val missionSource: MissionSourceEnum? = MissionSourceEnum.RAPPORT_NAV,
    var observationsByUnit: String? = null
) {
    companion object {
        fun fromMissionModel(model: MissionModel): MissionNavEntity {
            return MissionNavEntity(
                id = model.id,
                serviceId = model.serviceId,
                openBy = model.openBy,
                completedBy = model.completedBy,
                startDateTimeUtc = model.startDateTimeUtc,
                endDateTimeUtc = model.endDateTimeUtc,
                isDeleted = model.isDeleted,
                missionSource = model.missionSource,
                observationsByUnit = model.observationsByUnit
            )
        }
    }

    fun toMissionModel(): MissionModel {
        return MissionModel(
            id = id,
            serviceId = serviceId,
            openBy = openBy,
            completedBy = completedBy,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isDeleted = isDeleted,
            missionSource = missionSource,
            observationsByUnit = observationsByUnit
        )
    }

    fun hasNotChanged(input: MissionNavInputEntity): Boolean {
        return this.isDeleted == input.isDeleted
            && this.completedBy == input.completedBy
            && this.endDateTimeUtc == input.endDateTimeUtc
            && this.startDateTimeUtc == input.startDateTimeUtc
            && this.observationsByUnit == input.observationsByUnit
    }

    fun fromMissionNavInput(input: MissionNavInputEntity): MissionModel {
        this.isDeleted = input.isDeleted
        this.completedBy  = input.completedBy
        this.endDateTimeUtc = input.endDateTimeUtc
        this.startDateTimeUtc = input.startDateTimeUtc
        this.observationsByUnit = input.observationsByUnit
        return this.toMissionModel()
    }
}
