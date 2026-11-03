package fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.m1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AuthorityType
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfGenerator
import fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.bodyText
import fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.satiDocument
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ControlInformationSectionTest {

    private val generator = SatiPdfGenerator()

    @Test
    fun `generate should render the control information page`() {
        val bytes = generator.generate(satiDocument)

        assertThat(bodyText(bytes)).contains(
            "Informations sur le contrôle",
            "Autorité d'inspection",
            "Administration : Affaires Maritimes",
            "Unité : ULAM 56",
            "État membre : FRA-France",
            "Inspecteur responsable",
            "Nom : Dupont",
            "Prénom : Jean",
            "Autorité investie du pouvoir de nomination : Etat membre (FR)",
            "Début et fin d'inspection",
            "Heure de début : 31/12/2026 08:15 (UTC)",
            "Heure de fin : 31/12/2026 10:45 (UTC)",
            "Lieu d'inspection",
            "Coordonnées GPS (DMD) : 47° 48.585' N 003° 54.583' W",
        )
    }

    @Test
    fun `generate should render the authority that appointed the inspector, or none when not answered`() {
        assertThat(bodyText(generator.generate(satiDocument.copy(inspectorAuthorityType = AuthorityType.AECP))))
            .contains("Autorité investie du pouvoir de nomination : AECP")
        assertThat(bodyText(generator.generate(satiDocument.copy(inspectorAuthorityType = null))))
            .contains("Autorité investie du pouvoir de nomination : -")
    }

    @Test
    fun `generate should warn in the control information when the unit could not board`() {
        val text = bodyText(generator.generate(satiDocument.copy(isUnitBoarded = false)))

        assertThat(text).contains(
            "Informations sur le contrôle L'unité de contrôle n'a pas pu monter à bord du navire contrôlé Autorité d'inspection"
        )
    }

    @Test
    fun `generate should not warn when the unit boarded or when not answered`() {
        listOf(true, null).forEach { isUnitBoarded ->
            val text = bodyText(generator.generate(satiDocument.copy(isUnitBoarded = isUnitBoarded)))

            assertThat(text).doesNotContain("n'a pas pu monter à bord")
        }
    }

    @Test
    fun `generate should render a dash for a missing position`() {
        val bytes = generator.generate(satiDocument.copy(latitude = null))

        assertThat(bodyText(bytes)).contains("Coordonnées GPS (DMD) : -")
    }

    @Test
    fun `generate should render the port of a control without position`() {
        val bytes = generator.generate(satiDocument.copy(latitude = null, longitude = null, portName = "Lorient", portLocode = "FRLRT"))

        assertThat(bodyText(bytes)).contains("Lieu d'inspection Port : Lorient (FRLRT)").doesNotContain("Coordonnées GPS")
    }

    @Test
    fun `generate should prefer the position over the port`() {
        val bytes = generator.generate(satiDocument.copy(portName = "Lorient", portLocode = "FRLRT"))

        assertThat(bodyText(bytes)).contains("Coordonnées GPS (DMD) : 47° 48.585' N 003° 54.583' W").doesNotContain("Lorient (FRLRT)")
    }
}
