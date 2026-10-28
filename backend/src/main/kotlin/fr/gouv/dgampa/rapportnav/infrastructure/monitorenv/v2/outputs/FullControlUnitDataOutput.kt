package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitResourceType

/**
 * The subset of MonitorEnv's `GET /api/v2/control_units/{id}` (FullControlUnitDataOutput) that we read.
 * Every other field of the response is ignored.
 */
data class FullControlUnitDataOutput(
    val id: Int,
    val name: String? = null,
    val isArchived: Boolean? = null,
    val administration: FullControlUnitAdministrationDataOutput? = null,
    val controlUnitResources: List<FullControlUnitResourceDataOutput> = listOf()
) {
    fun toControlUnitEntity(): ControlUnitEntity {
        return ControlUnitEntity(
            id = id,
            administration = administration?.name.orEmpty(),
            isArchived = isArchived ?: false,
            name = name.orEmpty(),
            resources = controlUnitResources.map { it.toControlResourceEntity() }
        )
    }
}

data class FullControlUnitAdministrationDataOutput(
    val id: Int? = null,
    val name: String? = null
)

data class FullControlUnitResourceDataOutput(
    val id: Int,
    val isArchived: Boolean? = null,
    val name: String? = null,
    val type: String? = null
) {
    // A type RapportNav doesn't know (yet) maps to null rather than failing the whole control unit.
    fun toControlResourceEntity(): ControlResourceEntity {
        return ControlResourceEntity(
            id = id,
            name = name,
            type = ControlUnitResourceType.entries.firstOrNull { it.name == type },
            isArchived = isArchived ?: false
        )
    }
}
