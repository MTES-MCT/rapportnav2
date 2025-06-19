package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import java.time.Instant

data class PatchMissionInput(
    val isUnderJdp: Boolean? = null,
    val observationsByUnit: String? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val missionTypes: List<MissionTypeEnum>? =  null,
    val controlUnits: List<LegacyControlUnitEntity>? = null
)
