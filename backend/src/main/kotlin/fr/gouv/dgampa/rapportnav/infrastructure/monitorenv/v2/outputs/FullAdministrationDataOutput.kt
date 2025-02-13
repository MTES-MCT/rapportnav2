package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs

data class FullAdministrationDataOutput(
    val id: Int,
    val controlUnitIds: List<Int>,
    val controlUnits: List<ControlUnitDataOutput>,
    val isArchived: Boolean,
    val name: String,
)
