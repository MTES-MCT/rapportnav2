package fr.gouv.dgampa.rapportnav.domain.entities.mission.env

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.locationtech.jts.geom.MultiPolygon
import java.time.Instant

data class MissionEntity(
    val id: Int? = null,
    val missionTypes: List<MissionTypeEnum>? = listOf(),
    val controlUnits: List<LegacyControlUnitEntity> = listOf(),
    val openBy: String? = null,
    val completedBy: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    val geom: MultiPolygon? = null,
    var startDateTimeUtc: Instant,
    var endDateTimeUtc: Instant? = null,
    var envActions: List<EnvActionEntity>? = listOf(),
    var isDeleted: Boolean,
    val isGeometryComputedFromControls: Boolean,
    val missionSource: MissionSourceEnum,
    val hasMissionOrder: Boolean,
    val isUnderJdp: Boolean,
    var observationsByUnit: String? = null,
    val controlUnitIdOwner: Int? = null,
    val missionIdString: String? = null
) {
    companion object {
        fun fromMissionNavModel(model: MissionModel): MissionEntity {
            return MissionEntity(
                id = model.id,
                missionTypes = listOf(),
                controlUnits = model.controlUnits.map { LegacyControlUnitEntity(
                    id = it
                ) },
                openBy = model.openBy,
                completedBy = model.completedBy,
                observationsCacem = null,
                observationsByUnit = model.observationsByUnit,
                observationsCnsp = null,
                facade = null,
                geom = null,
                startDateTimeUtc = model.startDateTimeUtc,
                endDateTimeUtc = model.endDateTimeUtc,
                envActions = listOf(),
                isDeleted = model.isDeleted,
                isGeometryComputedFromControls = false,
                missionSource = MissionSourceEnum.RAPPORT_NAV,
                hasMissionOrder = false,
                isUnderJdp = false,
                controlUnitIdOwner = model.controlUnitIdOwner,
                missionIdString = model.missionIdString.toString()
            )
        }
    }

    fun toMissionNavEntity(startDateTimeUtc: Instant?, endDateTimeUtc: Instant?, isDeleted: Boolean?, observationsByUnit: String?): MissionNavEntity{
        return MissionNavEntity(
            id = id,
            startDateTimeUtc = startDateTimeUtc!!,
            endDateTimeUtc = endDateTimeUtc,
            isDeleted = isDeleted ?: false,
            observationsByUnit = observationsByUnit,
            completedBy = completedBy,
            openBy = openBy,
            controlUnitIdOwner = controlUnitIdOwner ?: controlUnits.first().id,
            controlUnits = controlUnits.map { it.id },
            missionIdString = missionIdString
        )
    }

    fun equalsInput(startDateTimeUtc: Instant?, endDateTimeUtc: Instant?, isDeleted: Boolean?, observationsByUnit: String?): Boolean {
        return this.startDateTimeUtc.equals(startDateTimeUtc) &&
            this.endDateTimeUtc?.equals(endDateTimeUtc) == true &&
            this.isDeleted.equals(isDeleted) &&
            this.observationsByUnit.equals(observationsByUnit)
    }
}

typealias EnvMission = MissionEntity
