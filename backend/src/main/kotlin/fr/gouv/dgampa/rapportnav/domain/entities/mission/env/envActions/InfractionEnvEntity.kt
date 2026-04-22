package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

data class InfractionEnvEntity(
    val id: String,
    val natinf: List<String>? = listOf(),
    val observations: String? = null,
    val registrationNumber: String? = null,
    val companyName: String? = null,
    val relevantCourt: String? = null,
    val infractionType: InfractionTypeEnum,
    val formalNotice: FormalNoticeEnum,
    val controlledPersonIdentity: String? = null,
    val vesselId: Int? = null,
    val vesselName: String? = null,
    val vesselSize: Number? = null,
    val vesselType: String? = null,
    val imo: String? = null,
    val mmsi: String? = null,
    val nbTarget: Int = 1,
    val administrativeResponse: AdministrativeResponseEnum? = null,
    val seizure: SeizureTypeEnum = SeizureTypeEnum.NO,
)
