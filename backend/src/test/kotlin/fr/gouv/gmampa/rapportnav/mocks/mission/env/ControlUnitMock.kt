package fr.gouv.gmampa.rapportnav.mocks.mission.env

import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnit

object ControlUnitMock {
    fun create(
        id: Int = 1,
        areaNote: String? = null,
        administrationId: Int = 1,
        departmentAreaInseeCode: String? = null,
        isArchived: Boolean = false,
        name: String = "PAM Themis",
        termsNote: String? = null,
    ): ControlUnit {
        return ControlUnit(
            id = id,
            areaNote = areaNote,
            administrationId = administrationId,
            departmentAreaInseeCode = departmentAreaInseeCode,
            isArchived = isArchived,
            name = name,
            termsNote = termsNote,
        )
    }
}
