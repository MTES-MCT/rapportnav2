package fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.m1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.DiscardReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.DiscardedSpeciesControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.SpeciesOnboardControl
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiSpeciesInspectionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfGenerator
import fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.bodyText
import fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.satiDocument
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DiscardsSectionTest {

    private val generator = SatiPdfGenerator()

    @Test
    fun `generate should render the discards`() {
        val speciesInspection = SatiSpeciesInspectionEntity(
            species = listOf(SpeciesOnboardControl().apply { speciesCode = "NEP"; speciesName = "Langoustine" }),
            discards = listOf(
                DiscardedSpeciesControl(speciesCode = "NEP", rejectedWeight = 5.0, discardReason = DiscardReason.DIM),
                DiscardedSpeciesControl(speciesCode = "COD", rejectedWeight = 2.5, discardReason = DiscardReason.RET),
                DiscardedSpeciesControl(speciesCode = "BIB", speciesName = "Tacaud commun", discardReason = DiscardReason.DIS),
                DiscardedSpeciesControl(speciesCode = "WHG"),
            ),
        )

        val text = bodyText(generator.generate(satiDocument.copy(speciesInspection = speciesInspection)))

        assertThat(text).contains(
            "Rejets Espèce Qté rejetée Nature du rejet",
            "NEP - Langoustine 5 (kg) DIM - de minimis",
            "COD 2,5 (kg) RET - espèces interdites",
            "BIB - Tacaud commun - DIS - autres rejets",
            "WHG - -",
        )
    }

    @Test
    fun `generate should render a tag without discards`() {
        val text = bodyText(generator.generate(satiDocument.copy(speciesInspection = SatiSpeciesInspectionEntity())))

        assertThat(text).contains("Rejets Aucun rejet").doesNotContain("Qté rejetée")
    }
}
