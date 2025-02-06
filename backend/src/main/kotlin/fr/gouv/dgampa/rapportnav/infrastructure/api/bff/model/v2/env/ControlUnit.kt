package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env

data class ControlUnit(
    val id: Int,
    val areaNote: String? = null,
    val administrationId: Int,
    val departmentAreaInseeCode: String? = null,
    val isArchived: Boolean,
    val name: String,
    val termsNote: String? = null,
)
