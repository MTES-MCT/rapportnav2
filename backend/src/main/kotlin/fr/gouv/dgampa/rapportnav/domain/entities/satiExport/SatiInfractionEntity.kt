package fr.gouv.dgampa.rapportnav.domain.entities.satiExport

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FishInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity

/**
 * An infraction of the SATI, declared either in MonitorFish or in RapportNav.
 * Its category is the [natinfCategory] of a MonitorFish infraction or the RapportNav [controlType].
 */
data class SatiInfractionEntity(
    val infractionType: InfractionType? = null,
    val natinfs: List<String> = emptyList(),
    /** One per NATINF, when known. */
    val descriptions: List<String> = emptyList(),
    /** Category of the NATINF in the NATINF referential (e.g. "Pêche"), when known. */
    val natinfCategory: String? = null,
    val controlType: ControlType? = null,
    val comments: String? = null,
) {
    companion object {
        /** MonitorFish gives the NATINF description but not its category: it comes from [natinfs]. */
        fun fromFishInfraction(infraction: FishInfraction, natinfs: List<NatinfEntity> = emptyList()) = SatiInfractionEntity(
            infractionType = infraction.infractionType,
            natinfs = listOfNotNull(infraction.natinf?.toString()),
            descriptions = listOfNotNull(infraction.natinfDescription),
            natinfCategory = natinfs.find { it.natinfCode == infraction.natinf }?.infractionCategory,
            comments = infraction.comments,
        )

        /** RapportNav only stores NATINF codes: their descriptions come from [natinfs]. */
        fun fromInfraction(infraction: InfractionEntity, controlType: ControlType, natinfs: List<NatinfEntity>) =
            SatiInfractionEntity(
                infractionType = when (infraction.infractionType) {
                    InfractionTypeEnum.WITH_REPORT -> InfractionType.WITH_RECORD
                    InfractionTypeEnum.WITHOUT_REPORT -> InfractionType.WITHOUT_RECORD
                    InfractionTypeEnum.WAITING -> InfractionType.PENDING
                    null -> null
                },
                natinfs = infraction.natinfs,
                descriptions = infraction.natinfs.mapNotNull { code ->
                    natinfs.find { it.natinfCode.toString() == code }?.infraction
                },
                controlType = controlType,
                comments = infraction.observations,
            )
    }
}
