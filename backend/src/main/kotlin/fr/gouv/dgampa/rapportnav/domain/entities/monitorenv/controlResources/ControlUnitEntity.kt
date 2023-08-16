package fr.gouv.dgampa.rapportnav.domain.entities.monitorenv.controlResources

data class ControlUnitEntity(
    val id: Int,
    val administration: String,
    val isArchived: Boolean,
    val name: String,
    val resources: List<ControlResourceEntity>,
    val contact: String? = null,
)
