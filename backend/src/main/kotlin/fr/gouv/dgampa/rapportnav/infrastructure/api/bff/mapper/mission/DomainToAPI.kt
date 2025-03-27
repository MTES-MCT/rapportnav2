package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.mapper.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNav

fun MissionNav.toMissionNavEntity(): MissionNavEntity = MissionNavEntity(
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
