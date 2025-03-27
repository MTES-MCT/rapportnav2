package fr.gouv.dgampa.rapportnav.infrastructure.database.mapper.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel

fun MissionNavEntity.toMissionModel(): MissionModel = MissionModel(
    id = this.id,
    missionTypes = this.missionTypes,
    controlUnits = this.controlUnits,
    openBy = this.openBy,
    completedBy = this.completedBy,
    startDateTimeUtc = this.startDateTimeUtc,
    endDateTimeUtc = this.endDateTimeUtc,
    isDeleted = this.isDeleted,
    missionSource = this.missionSource,
    observationsByUnit = this.observationsByUnit,
    controlUnitIdOwner = this.controlUnitIdOwner
)
