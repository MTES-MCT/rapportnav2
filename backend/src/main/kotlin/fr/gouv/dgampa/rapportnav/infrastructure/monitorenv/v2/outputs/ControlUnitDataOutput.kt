package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs

data class ControlUnitDataOutput(
    val id: Int,
    val areaNote: String? = null,
    val administrationId: Int,
    val departmentAreaInseeCode: String? = null,
    val isArchived: Boolean,
    val name: String,
    val termsNote: String? = null,
)
