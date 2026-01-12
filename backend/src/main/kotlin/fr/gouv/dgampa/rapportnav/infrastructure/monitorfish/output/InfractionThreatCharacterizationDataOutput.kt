package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.Infraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionThreatCharacterization

data class InfractionThreatCharacterizationDataOutput(
    val threats: List<ThreatHierarchyDataOutput>,
) {
    companion object {
        fun fromInfractionThreatCharacterization(
            infractions: List<InfractionThreatCharacterization>,
        ): List<ThreatHierarchyDataOutput> {
            val threats =
                InfractionHierarchyBuilder.buildThreatHierarchy(
                    items = infractions,
                    threatExtractor = { it.threat },
                    characterizationExtractor = { it.threatCharacterization },
                    natinfCodeExtractor = { it.natinfCode },
                    infractionNameExtractor = { it.infraction },
                )

            return InfractionThreatCharacterizationDataOutput(threats = threats).threats
        }

        fun fromInfraction(infraction: Infraction): ThreatHierarchyDataOutput {
            val threats =
                InfractionHierarchyBuilder.buildThreatHierarchy(
                    items = listOf(infraction),
                    threatExtractor = { it.threat ?: "Famille inconnue" },
                    characterizationExtractor = { it.threatCharacterization ?: "Type inconnu" },
                    natinfCodeExtractor = { it.natinf!! },
                    infractionNameExtractor = { "" },
                )

            return InfractionThreatCharacterizationDataOutput(threats = threats).threats.single()
        }
    }
}
