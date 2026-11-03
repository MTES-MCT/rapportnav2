package fr.gouv.dgampa.rapportnav.domain.entities.satiExport

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity

/**
 * Conclusions of a sea control: infractions declared in MonitorFish then in RapportNav,
 * seizures as filled in MonitorFish, comments and signatures as filled in the SATI.
 */
data class SatiConclusionEntity(
    val infractions: List<SatiInfractionEntity> = emptyList(),
    val hasSomeGearsSeized: Boolean = false,
    val hasSomeSpeciesSeized: Boolean = false,
    /** In kg. */
    val speciesQuantitySeized: Int? = null,
    val inspectorComment: String? = null,
    /** Null when unknown (no principal inspector). */
    val inspectorSigned: Boolean? = null,
    val controlledPersonComment: String? = null,
    /** Null when unknown (no master). */
    val controlledPersonSigned: Boolean? = null,
) {
    val hasInfractions: Boolean
        get() = infractions.isNotEmpty()

    companion object {
        /** [natinfs] give the categories of the MonitorFish infractions and the descriptions of the RapportNav ones. */
        fun fromFishAction(action: MissionFishActionEntity, natinfs: List<NatinfEntity> = emptyList()) = SatiConclusionEntity(
            infractions = action.fishInfractions.map { SatiInfractionEntity.fromFishInfraction(it, natinfs) } +
                rapportNavInfractions(action, natinfs),
            hasSomeGearsSeized = action.hasSomeGearsSeized == true,
            hasSomeSpeciesSeized = action.hasSomeSpeciesSeized == true,
            speciesQuantitySeized = action.speciesQuantitySeized,
            // as in the app: the observations of the unit are the ones of the inspector,
            // and the master is the controlled person
            inspectorComment = action.observationsByUnit,
            inspectorSigned = action.sati?.inspectors?.find { it.isPrincipal }?.party?.signature,
            controlledPersonComment = action.sati?.vessel?.master?.comments,
            controlledPersonSigned = action.sati?.vessel?.master?.signature,
        )

        private fun rapportNavInfractions(action: MissionFishActionEntity, natinfs: List<NatinfEntity>) =
            action.targets.orEmpty()
                .flatMap { it.controls.orEmpty() }
                .flatMap { control ->
                    control.infractions.orEmpty().map { SatiInfractionEntity.fromInfraction(it, control.controlType, natinfs) }
                }

        /** Whether [action] has infractions, whose NATINFs must then be looked up. */
        fun hasInfractions(action: MissionFishActionEntity): Boolean =
            action.fishInfractions.isNotEmpty() ||
                action.targets.orEmpty().any { target -> target.controls.orEmpty().any { !it.infractions.isNullOrEmpty() } }
    }
}
