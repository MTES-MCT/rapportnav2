package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

class CrossControlDataEntity (
    val type: String? = null,
    val agentId: Int? = null,
    val vesselId: Int? = null,
    val serviceId: Int? = null,
    var sumNbrOfHours: Int? = null,
    var vesselName: String? = null,
    val isReferentialClosed: Boolean? = null,
    val origin: CrossControlOriginType? = null,
)
