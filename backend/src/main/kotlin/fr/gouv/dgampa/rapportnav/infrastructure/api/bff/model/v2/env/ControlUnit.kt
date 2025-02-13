package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env

import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs.ControlUnitDataOutput

data class ControlUnit(
    val id: Int,
    val areaNote: String? = null,
    val administrationId: Int,
    val departmentAreaInseeCode: String? = null,
    val isArchived: Boolean,
    val name: String,
    val termsNote: String? = null,
) {
    companion object {
        fun fromControlUnitDataOutput(dataOutput: ControlUnitDataOutput): ControlUnit {
            return ControlUnit(
                id = dataOutput.id,
                areaNote = dataOutput.areaNote,
                administrationId = dataOutput.administrationId,
                departmentAreaInseeCode = dataOutput.departmentAreaInseeCode,
                isArchived = dataOutput.isArchived,
                name = dataOutput.name,
                termsNote = dataOutput.termsNote
            )
        }
    }
}
