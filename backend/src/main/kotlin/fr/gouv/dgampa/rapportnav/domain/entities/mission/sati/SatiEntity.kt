package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import java.time.Instant
import java.util.*


data class SatiEntity(
    var id: UUID? = null,
    var module: SatiModuleType,
    var actionId: String,
    var missionId: Int? = null,
    var resource: ControlResourceEntity? = null,
    var vessel: SatiVesselEntity? = null,
    var startDatetimeUtc: Instant? = null,
    var endDatetimeUtc: Instant? = null,
    var inspectors: List<SatiInspectorEntity>? = emptyList(),
    var status: SatiStatusType = SatiStatusType.IN_PROGRESS,
    var version: Int = 0,
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null,
    var updatedBy: Int? = null,
)
