package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati

import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiDocumentEntity
import fr.gouv.dgampa.rapportnav.domain.ports.ISatiPdfGenerator
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfFonts
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfLayout
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfPageFlow
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.footers
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.render
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.m1.conclusionSection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.m1.controlInformationSection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.m1.discardsSection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.m1.gearInspectionSection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.m1.generalInformationSection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.m1.speciesInspectionSection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.m1.vesselComplianceSection
import org.apache.pdfbox.pdmodel.PDDocument
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

/**
 * Renders the SATI of a document: a cover page, then sections following each other over as many pages as needed,
 * then footers on every page but the cover.
 */
@Component
class SatiPdfGenerator : ISatiPdfGenerator {

    private val logo: ByteArray by lazy {
        requireNotNull(javaClass.getResourceAsStream(LOGO_PATH)) { "Missing resource $LOGO_PATH" }
            .use { it.readBytes() }
    }

    override fun generate(document: SatiDocumentEntity): ByteArray {
        PDDocument().use { pdf ->
            pdf.documentInformation.title = document.reference
            render(pdf, PdfContext(PdfFonts.load(pdf)), document)
            return ByteArrayOutputStream().use { output ->
                pdf.save(output)
                output.toByteArray()
            }
        }
    }

    private fun render(pdf: PDDocument, context: PdfContext, document: SatiDocumentEntity) {
        context.coverPage(pdf, document, logo)
        PdfPageFlow(pdf, PdfLayout.MARGIN).use { flow ->
            with(context) {
                listOf(
                    controlInformationSection(document),
                    generalInformationSection(document),
                    vesselComplianceSection(document.compliance),
                    gearInspectionSection(document.gearInspection),
                    speciesInspectionSection(document.speciesInspection),
                    discardsSection(document.speciesInspection),
                    conclusionSection(document.conclusion),
                )
            }.forEach { flow.render(it, context) }
        }
        context.footers(pdf, document.reference, skippedPages = 1)
    }

    companion object {
        private const val LOGO_PATH = "/pdf/logo.png"
    }
}
