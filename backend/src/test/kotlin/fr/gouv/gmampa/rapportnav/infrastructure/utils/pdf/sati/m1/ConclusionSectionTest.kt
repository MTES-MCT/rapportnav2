package fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.m1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FishInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiConclusionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiInfractionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfGenerator
import fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.bodyText
import fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.satiDocument
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConclusionSectionTest {

    private val generator = SatiPdfGenerator()

    private val infraction = SatiInfractionEntity.fromFishInfraction(
        FishInfraction(
            infractionType = InfractionType.WITH_RECORD,
            natinf = 27689,
            natinfDescription = "Entrave au contrôle",
            threat = "Obligations déclaratives",
            comments = "Le capitaine a refusé l'accès à la cale.",
        ),
        natinfs = listOf(NatinfEntity(infraction = "Entrave au contrôle", natinfCode = 27689, infractionCategory = "Pêche")),
    )

    private fun conclusionText(conclusion: SatiConclusionEntity): String {
        val text = bodyText(generator.generate(satiDocument.copy(conclusion = conclusion)))
        return text.substringAfter("Conclusions de l'inspection").trim()
    }

    /** The tags of the conclusion, before the infraction boxes and the comments. */
    private fun conclusionTagsText(conclusion: SatiConclusionEntity): String =
        conclusionText(conclusion).substringBefore("Infraction :").substringBefore("Commentaire de l'inspecteur").trim()

    @Test
    fun `generate should only conclude without infraction when there is none`() {
        // seizures without infraction are not shown
        val conclusion = SatiConclusionEntity(hasSomeGearsSeized = true)

        assertThat(conclusionTagsText(conclusion)).isEqualTo("Sans infraction")
    }

    @Test
    fun `generate should conclude with infraction and without seizure`() {
        assertThat(conclusionTagsText(SatiConclusionEntity(infractions = listOf(infraction))))
            .isEqualTo("Avec infraction Sans appréhension")
    }

    @Test
    fun `generate should conclude with infraction and the seized gears and species`() {
        val conclusion = SatiConclusionEntity(
            infractions = listOf(infraction),
            hasSomeGearsSeized = true,
            hasSomeSpeciesSeized = true,
            speciesQuantitySeized = 250,
        )

        assertThat(conclusionTagsText(conclusion)).isEqualTo("Avec infraction Appréhension engin Appréhension espèces (250 kg)")
    }

    @Test
    fun `generate should conclude with seized species of unknown quantity`() {
        val conclusion = SatiConclusionEntity(infractions = listOf(infraction), hasSomeSpeciesSeized = true)

        assertThat(conclusionTagsText(conclusion)).isEqualTo("Avec infraction Sans appréhension Appréhension espèces")
    }

    @Test
    fun `generate should render a box per infraction`() {
        // without its NATINF in the referential: no category
        val warning = SatiInfractionEntity.fromFishInfraction(
            FishInfraction(
                infractionType = InfractionType.WITHOUT_RECORD,
                natinf = 7059,
                natinfDescription = "Pêche avec engin non autorisé",
                threat = "Mesures techniques",
            )
        )

        val text = conclusionText(SatiConclusionEntity(infractions = listOf(infraction, warning)))

        assertThat(text).contains(
            "Infraction : Entrave au contrôle Verbalisation (PV) Pêche / NATINF : 27689 " +
                "Le capitaine a refusé l'accès à la cale.",
            "Infraction : Pêche avec engin non autorisé Rappel à la réglementation - / NATINF : 7059",
        )
    }

    @Test
    fun `generate should render an infraction of RapportNav with all its NATINFs and its control type`() {
        val rapportNavInfraction = SatiInfractionEntity(
            infractionType = InfractionType.PENDING,
            natinfs = listOf("2593", "27000"),
            descriptions = listOf("Défaut de permis d'armement", "Défaut de rôle d'équipage"),
            controlType = ControlType.ADMINISTRATIVE,
        )

        val text = conclusionText(SatiConclusionEntity(infractions = listOf(rapportNavInfraction)))

        assertThat(text).contains(
            "Infraction : Défaut de permis d'armement ; Défaut de rôle d'équipage En attente " +
                "Administratif navire / NATINF : 2593, 27000"
        )
    }

    @Test
    fun `generate should render the infraction boxes without the observations when there are none`() {
        val text = conclusionText(SatiConclusionEntity(infractions = listOf(infraction.copy(comments = null))))

        assertThat(text.substringBefore("Commentaire de l'inspecteur").trim()).endsWith("Pêche / NATINF : 27689")
    }

    @Test
    fun `generate should render the comments and signatures of the inspector and of the controlled person`() {
        val conclusion = SatiConclusionEntity(
            inspectorComment = "Contrôle sans difficulté.",
            inspectorSigned = true,
            controlledPersonComment = null,
            controlledPersonSigned = false,
        )

        assertThat(conclusionText(conclusion)).endsWith(
            "Commentaire de l'inspecteur Commentaire : Contrôle sans difficulté. Rapport signé : OUI " +
                "Commentaire de la personne contrôlée Commentaire : Pas de commentaire Rapport signé : NON"
        )
    }

    @Test
    fun `generate should render the comments after the infraction boxes`() {
        val text = conclusionText(SatiConclusionEntity(infractions = listOf(infraction)))

        assertThat(text.indexOf("Commentaire de l'inspecteur")).isGreaterThan(text.indexOf("Infraction : Entrave au contrôle"))
        assertThat(text).contains("Rapport signé : -")
    }

    @Test
    fun `generate should render no infraction box without infraction`() {
        assertThat(conclusionText(SatiConclusionEntity())).doesNotContain("Infraction :")
    }
}
