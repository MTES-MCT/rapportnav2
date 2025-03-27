package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import java.time.Instant

data class MissionNav(
    val id: Int? = null,
    val missionTypes: List<MissionTypeEnum>,
    val controlUnits: List<Int> = listOf(),
    val openBy: String? = null,
    val completedBy: String? = null,
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant? = null,
    val isDeleted: Boolean = false,
    val missionSource: MissionSourceEnum? = MissionSourceEnum.RAPPORT_NAV,
    val observationsByUnit: String? = null,
    val controlUnitIdOwner: Int
) {

    companion object {
        fun fromMissionNavEntity(entity: MissionNavEntity): MissionNav {
            return MissionNav(
                id = entity.id,
                missionTypes = entity.missionTypes,
                controlUnits = entity.controlUnits,
                openBy = entity.openBy,
                completedBy = entity.completedBy,
                startDateTimeUtc = entity.startDateTimeUtc,
                endDateTimeUtc = entity.endDateTimeUtc,
                isDeleted = entity.isDeleted,
                missionSource = entity.missionSource,
                observationsByUnit = entity.observationsByUnit,
                controlUnitIdOwner = entity.controlUnitIdOwner
            )
        }
    }

    fun toMissionNavEntity(): MissionNavEntity {
        return MissionNavEntity(
            id = id,
            missionTypes = missionTypes,
            controlUnits = controlUnits,
            openBy = openBy,
            completedBy = completedBy,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isDeleted = isDeleted,
            missionSource = missionSource,
            observationsByUnit = observationsByUnit,
            controlUnitIdOwner = controlUnitIdOwner
        )
    }
}
