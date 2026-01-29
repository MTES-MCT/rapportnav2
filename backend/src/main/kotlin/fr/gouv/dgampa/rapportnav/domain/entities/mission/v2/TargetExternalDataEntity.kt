package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum

class TargetExternalDataEntity(
    val id: String,
    val nbTarget: Int = 1,
    val natinfs: List<String>? = listOf(),
    val observations: String? = null,
    val registrationNumber: String? = null,
    val companyName: String? = null,
    val relevantCourt: String? = null,
    val infractionType: InfractionTypeEnum? = null,
    val formalNotice: FormalNoticeEnum? = null,
    val toProcess: Boolean? = null,
    val controlledPersonIdentity: String? = null,
    val vesselType: VesselTypeEnum? = null,
    val vesselSize: VesselSizeEnum? = null,
)
