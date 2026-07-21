package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import java.time.Instant
import java.util.*

data class MissionNavEntity(
    val id: UUID,
    var externalId: String? = null,
    val serviceId: Int? = null,
    val openBy: String? = null,
    var completedBy: String? = null,
    var startDateTimeUtc: Instant,
    var endDateTimeUtc: Instant? = null,
    var isDeleted: Boolean = false,
    val missionSource: MissionSourceEnum? = MissionSourceEnum.RAPPORT_NAV,
    var observationsByUnit: String? = null,
    // Mission-level validation, mirrored 1:1 from MissionModel
    var isCompleteForStats: Boolean? = null,
    var sourcesOfMissingData: String? = null
) {
    companion object {
        fun fromMissionModel(model: MissionModel): MissionNavEntity {
            return MissionNavEntity(
                id = model.id,
                externalId = model.externalId,
                serviceId = model.serviceId,
                openBy = model.openBy,
                completedBy = model.completedBy,
                startDateTimeUtc = model.startDateTimeUtc,
                endDateTimeUtc = model.endDateTimeUtc,
                isDeleted = model.isDeleted,
                missionSource = model.missionSource,
                observationsByUnit = model.observationsByUnit,
                isCompleteForStats = model.isCompleteForStats,
                sourcesOfMissingData = model.sourcesOfMissingData
            )
        }
    }

    fun toMissionModel(): MissionModel {
        return MissionModel(
            id = id,
            externalId = externalId,
            serviceId = serviceId,
            openBy = openBy,
            completedBy = completedBy,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isDeleted = isDeleted,
            missionSource = missionSource,
            observationsByUnit = observationsByUnit,
            isCompleteForStats = isCompleteForStats,
            sourcesOfMissingData = sourcesOfMissingData
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
