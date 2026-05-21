package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources

import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv

data class ControlResourceEntity(
    val id: Int,
    val name: String,
    val type: ControlUnitResourceType
) {
    companion object {
        fun fromControlUnitResourceEnv(
            resourceEnv: ControlUnitResourceEnv
        ): ControlResourceEntity {
            return ControlResourceEntity(
                id = resourceEnv.id,
                name = resourceEnv.name,
                type = resourceEnv.type
            )
        }
    }
}
