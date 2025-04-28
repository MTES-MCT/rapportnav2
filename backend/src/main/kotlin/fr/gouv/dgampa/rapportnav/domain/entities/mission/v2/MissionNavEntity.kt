package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import java.time.Instant

data class MissionNavEntity(
    val id: Int? = null,
    val controlUnits: List<Int> = listOf(),
    val openBy: String? = null,
    val completedBy: String? = null,
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant? = null,
    val isDeleted: Boolean = false,
    val missionSource: MissionSourceEnum? = MissionSourceEnum.RAPPORT_NAV,
    val observationsByUnit: String? = null,
    val controlUnitIdOwner: Int,
    val missionIdString: String? = null
) {
    companion object {
        fun fromMissionModel(model: MissionModel): MissionNavEntity {
            return MissionNavEntity(
                id = model.id,
                controlUnits = model.controlUnits,
                openBy = model.openBy,
                completedBy = model.completedBy,
                startDateTimeUtc = model.startDateTimeUtc,
                endDateTimeUtc = model.endDateTimeUtc,
                isDeleted = model.isDeleted,
                missionSource = model.missionSource,
                observationsByUnit = model.observationsByUnit,
                controlUnitIdOwner = model.controlUnitIdOwner,
                missionIdString = model.missionIdString.toString()
            )
        }
    }

    fun toMissionModel(): MissionModel {
        return MissionModel(
            id = id,
            controlUnits = controlUnits,
            openBy = openBy,
            completedBy = completedBy,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isDeleted = isDeleted,
            missionSource = missionSource,
            observationsByUnit = observationsByUnit,
            controlUnitIdOwner = controlUnitIdOwner,
        )
    }

    fun toMissionEntity(): MissionEntity {
        return MissionEntity(
            id = id,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            missionTypes = listOf(),
            isDeleted = isDeleted,
            observationsByUnit = observationsByUnit,
            controlUnits = controlUnits.map { LegacyControlUnitEntity(
                id = it
            ) },
            hasMissionOrder = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            controlUnitIdOwner = controlUnitIdOwner,
            missionIdString = missionIdString.toString()
        )
    }
}
