package fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.m1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.ControlCheck
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.GearControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.SpeciesOnboardControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.WireType
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiGearInspectionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiSpeciesInspectionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiVesselComplianceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfGenerator
import fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.bodyText
import fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.pageText
import fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati.satiDocument
import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.TextPosition
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChecksSectionsTest {

    private val generator = SatiPdfGenerator()

    @Test
    fun `generate should render the vessel compliance with an answer per check`() {
        val compliance = SatiVesselComplianceEntity(
            gangwayPresentAndCompliant = ControlCheck.YES,
            emitsVms = ControlCheck.NO,
            emitsAis = ControlCheck.NOT_APPLICABLE,
        )

        val text = bodyText(generator.generate(satiDocument.copy(compliance = compliance)))

        assertThat(text).contains(
            "Conformité du navire",
            "Échelle de coupée présente et conforme OUI",
            "Bonne émission VMS NON",
            "Bonne émission AIS N/A",
            "Journal de pêche ouvert avant le contrôle -",
            "Autorisations de pêche (AEP, ANP, licences locales) conformes à l'activité du navire -",
            "Certificat de pesée présent et systèmes de pesée à bord valides -",
        )
    }

    @Test
    fun `generate should render the answers of the checks in bold, regular and italic`() {
        val compliance = SatiVesselComplianceEntity(
            gangwayPresentAndCompliant = ControlCheck.YES,
            emitsVms = ControlCheck.NO,
            emitsAis = ControlCheck.NOT_APPLICABLE,
        )
        val bytes = generator.generate(satiDocument.copy(compliance = compliance))

        Loader.loadPDF(bytes).use { pdf ->
            val fonts = (2..pdf.numberOfPages).flatMap { index ->
                val resources = pdf.getPage(index - 1).resources
                resources.fontNames.map { resources.getFont(it).name.substringAfter('+') }
            }
            assertThat(fonts).contains("Marianne-RegularItalic")
        }
    }

    @Test
    fun `generate should render the declarative obligations observations in a callout`() {
        val compliance = SatiVesselComplianceEntity(licencesAndLogbookObservations = "Journal de pêche rempli tardivement.")

        val text = bodyText(generator.generate(satiDocument.copy(compliance = compliance)))

        assertThat(text).contains(
            "Observations sur les obligations déclaratives et autorisations de pêche Journal de pêche rempli tardivement."
        )
    }

    @Test
    fun `generate should not render the observations callout without observations`() {
        val compliance = SatiVesselComplianceEntity(licencesAndLogbookObservations = " ")

        val text = bodyText(generator.generate(satiDocument.copy(compliance = compliance)))

        assertThat(text).doesNotContain("Observations sur les obligations déclaratives")
    }

    private val trawl = GearControl().apply {
        gearCode = "OTB"
        gearName = "Chaluts de fond à panneaux"
        gearWasControlled = true
        gearMarkingIsCompliant = ControlCheck.NO
        declaredMesh = 70.0
        controlledMesh = 68.5
        averageWireThickness = 4.0
        wireType = WireType.SINGLE
        comments = "Cul de chalut doublé."
    }

    private val net = GearControl().apply {
        gearCode = "GNS"
        gearName = "Filets maillants calés"
        gearWasControlled = true
        gearMarkingIsCompliant = ControlCheck.YES
        declaredMesh = 100.0
        hasUncontrolledMesh = true
    }

    private val longline = GearControl().apply {
        gearCode = "LLS"
        gearName = "Palangres calées"
        gearWasControlled = false
        declaredMesh = 1.0
    }

    @Test
    fun `generate should render the gear inspection with a row per controlled gear`() {
        val gearInspection = SatiGearInspectionEntity(listOf(trawl, net, longline))

        val text = bodyText(generator.generate(satiDocument.copy(gearInspection = gearInspection)))

        assertThat(text)
            .contains(
                "Inspection des engins",
                "Engin contrôlé OUI",
                "Marquage de l'engin conforme NON",
                "Engin Maillage déclaré Maillage mesuré Épaisseur moy. fil Type de fil",
                "OTB - Chaluts de fond à panneaux 70 mm 68,5 mm 4 mm Simple",
                "GNS - Filets maillants calés 100 mm Non mesuré - -",
                "Observations sur les engins OTB : Cul de chalut doublé.",
            )
            .doesNotContain("LLS - Palangres calées")
    }

    @Test
    fun `generate should render no gear table nor observations without controlled gears`() {
        val text = bodyText(generator.generate(satiDocument.copy(gearInspection = SatiGearInspectionEntity(listOf(longline)))))

        assertThat(text)
            .contains("Engin contrôlé NON", "Marquage de l'engin conforme N/A")
            .doesNotContain("Maillage déclaré", "Observations sur les engins")
    }

    @Test
    fun `generate should render the species inspection with a row per species on board`() {
        val hake = SpeciesOnboardControl().apply {
            speciesCode = "HKE"
            speciesName = "Merlu européen"
            declaredWeight = 988.0
            controlledWeight = 1012.5
            underSizedWeight = 12.0
            presentationCodes = listOf("WHL", "GUT")
            faoZones = listOf("27.8.a", "27.8.b")
        }
        val sole = SpeciesOnboardControl().apply { speciesCode = "SOL"; speciesName = "Sole commune" }
        val speciesInspection = SatiSpeciesInspectionEntity(
            speciesWeightControlled = ControlCheck.YES,
            speciesSizeControlled = ControlCheck.NO,
            underSizedSeparateRecording = ControlCheck.NOT_APPLICABLE,
            species = listOf(hake, sole),
            speciesObservations = "Merlus sous-taille mélangés au reste de la capture.",
        )

        val text = bodyText(generator.generate(satiDocument.copy(speciesInspection = speciesInspection)))

        assertThat(text).contains(
            "Inspection des espèces",
            "Poids des espèces vérifié OUI",
            "Taille des espèces vérifiée NON",
            "Arrimage séparé des espèces soumises à plan -",
            "Arrimage séparé des poissons n'ayant pas la taille requise -",
            "Enregistrement séparé des poissons n'ayant pas la taille requise N/A",
            "HKE - Merlu européen 988 (kg) 1012,5 (kg) 12 (kg) WHL, GUT 27.8.a, 27.8.b",
            "SOL - Sole commune - - - - -",
            "Observations sur les espèces Merlus sous-taille mélangés au reste de la capture.",
        )
    }

    @Test
    fun `generate should split a table and observations taller than a page over several pages`() {
        val speciesInspection = SatiSpeciesInspectionEntity(
            species = (1..80).map { index -> SpeciesOnboardControl().apply { speciesCode = "S%02d".format(index) } },
            speciesObservations = (1..400).joinToString(" ") { "observation$it" },
        )

        val bytes = generator.generate(satiDocument.copy(speciesInspection = speciesInspection))

        val text = bodyText(bytes)
        assertThat(text).contains("S01 -", "S80 -", "observation1 ", "observation400")
        Loader.loadPDF(bytes).use { pdf ->
            val outOfPage = mutableListOf<String>()
            object : PDFTextStripper() {
                override fun writeString(string: String, textPositions: List<TextPosition>) {
                    val pageHeight = currentPage.mediaBox.height
                    if (textPositions.any { it.yDirAdj < 0 || it.yDirAdj > pageHeight }) outOfPage += string
                }
            }.getText(pdf)
            assertThat(outOfPage).isEmpty()
        }
    }

    @Test
    fun `generate should render no species table nor observations without species`() {
        val text = bodyText(generator.generate(satiDocument.copy(speciesInspection = SatiSpeciesInspectionEntity())))

        assertThat(text).contains("Inspection des espèces").doesNotContain("Qté déclarée", "Observations sur les espèces")
    }

    @Test
    fun `generate should keep each checks section whole on a page`() {
        val compliance = SatiVesselComplianceEntity(
            emitsVms = ControlCheck.YES,
            licencesAndLogbookObservations = "Le journal de pêche a été ouvert tardivement.",
        )
        val hake = SpeciesOnboardControl().apply { speciesCode = "HKE"; speciesName = "Merlu européen"; declaredWeight = 988.0 }
        val speciesInspection = SatiSpeciesInspectionEntity(
            species = List(4) { hake },
            speciesObservations = "Merlus sous-taille mélangés au reste de la capture.",
        )
        val bytes = generator.generate(
            satiDocument.copy(
                compliance = compliance,
                gearInspection = SatiGearInspectionEntity(listOf(trawl, net)),
                speciesInspection = speciesInspection,
            )
        )

        val pages = Loader.loadPDF(bytes).use { pdf -> (1..pdf.numberOfPages).map { pageText(bytes, it) } }
        fun pageOf(text: String) = pages.indexOfFirst { it.contains(text) }.also { assertThat(it).isNotNegative() }
        assertThat(pageOf("Observations sur les obligations déclaratives")).isEqualTo(pageOf("Conformité du navire"))
        assertThat(pageOf("Observations sur les engins")).isEqualTo(pageOf("Inspection des engins"))
        assertThat(pageOf("Observations sur les espèces")).isEqualTo(pageOf("Inspection des espèces"))
    }
}
