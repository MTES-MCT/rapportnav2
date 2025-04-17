package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import java.time.Instant
import java.util.*

data class MissionNavEntity(
    val id: String? = null,
    val controlUnits: List<Int> = listOf(),
    val openBy: String? = null,
    val completedBy: String? = null,
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant? = null,
    val isDeleted: Boolean = false,
    val missionSource: MissionSourceEnum? = MissionSourceEnum.RAPPORT_NAV,
    val observationsByUnit: String? = null,
    val controlUnitIdOwner: Int,
    val navId: UUID? = null
) {
    companion object {
        fun fromMissionModel(model: MissionModel): MissionNavEntity {
            return MissionNavEntity(
                id = model.id.toString(),
                controlUnits = model.controlUnits,
                openBy = model.openBy,
                completedBy = model.completedBy,
                startDateTimeUtc = model.startDateTimeUtc,
                endDateTimeUtc = model.endDateTimeUtc,
                isDeleted = model.isDeleted,
                missionSource = model.missionSource,
                observationsByUnit = model.observationsByUnit,
                controlUnitIdOwner = model.controlUnitIdOwner,
                navId = model.navId
            )
        }
    }

    fun toMissionModel(): MissionModel {
        return MissionModel(
            id = id?.toInt(),
            controlUnits = controlUnits,
            openBy = openBy,
            completedBy = completedBy,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isDeleted = isDeleted,
            missionSource = missionSource,
            observationsByUnit = observationsByUnit,
            controlUnitIdOwner = controlUnitIdOwner,
            navId = UUID.randomUUID()
        )
    }

    fun toMissionEntity(): MissionEntity {
        return MissionEntity(
            id = navId.toString(),
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
            navId = navId
        )
    }
}
