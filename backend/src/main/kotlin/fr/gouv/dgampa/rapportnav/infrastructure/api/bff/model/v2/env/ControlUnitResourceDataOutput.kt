package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env

data class ControlUnitResourceDataOutput (
    val id: Int,
    val controlUnitId: Int,
    val isArchived: Boolean,
    val name: String,
    val note: String? = null,
    val photo: ByteArray? = null,
    val radioFrequency: String?,
    val registrationId: String?,
    val stationId: Int?,
    val type: String
)
