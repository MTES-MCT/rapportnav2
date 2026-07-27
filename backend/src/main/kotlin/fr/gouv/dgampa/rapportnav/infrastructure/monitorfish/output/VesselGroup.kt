package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output

data class VesselGroup(
    val id: Int? = null,
    val name: String,
    val color: String,
    val type: GroupType,
    val isPriorityGroup: Boolean = false,
)
