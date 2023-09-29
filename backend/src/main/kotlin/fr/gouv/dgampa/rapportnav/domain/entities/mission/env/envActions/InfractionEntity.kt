package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

data class InfractionEntity(
    val id: String,
    val natinf: List<String>? = listOf(),
    val observations: String? = null,
    val registrationNumber: String? = null,
    val companyName: String? = null,
    val relevantCourt: String? = null,
    val infractionType: InfractionTypeEnum,
    val formalNotice: FormalNoticeEnum,
    val toProcess: Boolean,
    val controlledPersonIdentity: String? = null,
    val vesselType: VesselTypeEnum? = null,
    val vesselSize: VesselSizeEnum? = null,
)
