package fr.gouv.dgampa.rapportnav.domain.entities.satiExport

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.ControlCheck
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.DiscardedSpeciesControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.SpeciesOnboardControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity

/**
 * Species inspection of a sea control, as filled in MonitorFish. A null check was not answered.
 */
data class SatiSpeciesInspectionEntity(
    val speciesWeightControlled: ControlCheck? = null,
    val speciesSizeControlled: ControlCheck? = null,
    val separateStowageOfPreservedSpecies: ControlCheck? = null,
    val underSizedSeparateStowage: ControlCheck? = null,
    val underSizedSeparateRecording: ControlCheck? = null,
    val species: List<SpeciesOnboardControl> = emptyList(),
    val speciesObservations: String? = null,
    val discards: List<DiscardedSpeciesControl> = emptyList(),
) {
    /** Name of a discarded species, from MonitorFish or else from the species on board with the same code. */
    fun speciesName(discard: DiscardedSpeciesControl): String? =
        discard.speciesName ?: species.firstOrNull { it.speciesCode == discard.speciesCode }?.speciesName

    companion object {
        fun fromFishAction(action: MissionFishActionEntity) = SatiSpeciesInspectionEntity(
            speciesWeightControlled = action.speciesWeightControlled,
            speciesSizeControlled = action.speciesSizeControlled,
            separateStowageOfPreservedSpecies = action.separateStowageOfPreservedSpecies,
            underSizedSeparateStowage = action.underSizedSeparateStowage,
            underSizedSeparateRecording = action.underSizedSeparateRecording,
            species = action.speciesOnboard.orEmpty(),
            speciesObservations = action.speciesObservations,
            discards = action.discardedSpecies.orEmpty(),
        )
    }
}
