package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel

data class ServiceInput(
    val id: Int?,
    val name: String,
) {
    fun toServiceModel(): ServiceModel {
        return ServiceModel(
            id = id,
            name = name,
        )
    }
}
