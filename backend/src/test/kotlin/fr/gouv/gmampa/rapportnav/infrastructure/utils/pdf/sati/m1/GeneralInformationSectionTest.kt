package fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.m1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AddressEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.ContactEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiPartyEntity
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfGenerator
import fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.bodyText
import fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.pageText
import fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.satiDocument
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GeneralInformationSectionTest {

    private val generator = SatiPdfGenerator()

    @Test
    fun `generate should render the general information page`() {
        val bytes = generator.generate(satiDocument)

        assertThat(bodyText(bytes)).contains(
            "Informations générales",
            "Informations sur le navire contrôlé",
            "Nom du navire : FLEUR DE LYS",
            "CFR : FRA000123456",
            "Marq. ext. : FL854548",
            "N° d'appel radio : FQHZ",
            "IMO : 9123456",
            "Longueur : 24,5 m",
            "Type : Chalutier",
            "N° de marée : TRIP-42",
            // the owner only has a full name, standing for the last name
            "Informations sur le propriétaire Nom : Armement Dupont Prénom : - Nationalité : FRA " +
                "Tél. : 0601020304 Email : armement@example.fr Adresse : 1 quai des Indes, 56100 Lorient",
            "Informations sur le capitaine Nom : Le Goff Prénom : Yann",
            "Informations sur l'affréteur Non applicable",
            "Informations sur l'agent Non applicable",
        )
    }

    @Test
    fun `generate should start the general information on the page of the control information`() {
        val page2 = pageText(generator.generate(satiDocument), page = 2)

        assertThat(page2).contains("Informations sur le contrôle", "Informations générales")
    }

    @Test
    fun `generate should move a subsection that does not fit to the next page rather than cut it`() {
        // a long owner address makes the owner subsection too tall for the rest of page 2
        val address = AddressEntity(fullAddress = List(150) { "Quai des Indes $it" }.joinToString(" "))
        val owner = satiDocument.vessel!!.owner!!.copy(contact = satiDocument.vessel!!.owner!!.contact!!.copy(address = address))
        val bytes = generator.generate(satiDocument.copy(vessel = satiDocument.vessel!!.copy(owner = owner)))

        val page2 = pageText(bytes, page = 2)
        val page3 = pageText(bytes, page = 3)
        assertThat(page2).contains("Informations sur le navire contrôlé").doesNotContain("Informations sur le propriétaire")
        assertThat(page3).contains("Informations sur le propriétaire", "Quai des Indes 0", "Quai des Indes 149")
    }

    @Test
    fun `generate should render the operator and the agent when filled in`() {
        val vessel = satiDocument.vessel!!.copy(
            operator = SatiPartyEntity(contact = ContactEntity(fullName = "Affrètement SA")),
            agent = SatiPartyEntity(contact = ContactEntity(fullName = "Paul Agent", email = "paul@example.fr")),
        )

        val text = bodyText(generator.generate(satiDocument.copy(vessel = vessel)))

        assertThat(text)
            .contains("Informations sur l'affréteur Nom : Affrètement SA", "Informations sur l'agent Nom : Paul Agent")
            .contains("Email : paul@example.fr")
            .doesNotContain("Non applicable")
    }

    @Test
    fun `generate should render dashes for a vessel without SATI data`() {
        val text = bodyText(generator.generate(satiDocument.copy(vessel = null)))

        assertThat(text).contains("Nom du navire : FLEUR DE LYS CFR : -", "Informations sur le propriétaire Nom : -")
    }
}
