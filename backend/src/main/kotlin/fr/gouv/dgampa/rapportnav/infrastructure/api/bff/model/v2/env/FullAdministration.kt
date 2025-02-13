package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env

import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs.FullAdministrationDataOutput

data class FullAdministration(
    val id: Int,
    val controlUnitIds: List<Int>,
    val controlUnits: List<ControlUnit>,
    val isArchived: Boolean,
    val name: String,
) {
    companion object {
        fun fromFullAdministrationDataOutput(dataOutput: FullAdministrationDataOutput): FullAdministration {
            return FullAdministration(
                id = dataOutput.id,
                controlUnits = dataOutput.controlUnits.map { ControlUnit.fromControlUnitDataOutput(it) },
                controlUnitIds = dataOutput.controlUnitIds,
                isArchived = dataOutput.isArchived,
                name = dataOutput.name
            )
        }
    }
}
