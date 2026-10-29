package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.ports.ISatiPdfGenerator
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class SatiPdfGenerator : ISatiPdfGenerator {

    // Skeleton: a single blank A4 page. Content will be rendered from the action/SATI data later.
    override fun generate(action: MissionFishActionEntity): ByteArray {
        PDDocument().use { document ->
            document.addPage(PDPage(PDRectangle.A4))
            document.documentInformation.title = "SATI - action ${action.id}"
            return ByteArrayOutputStream().use { output ->
                document.save(output)
                output.toByteArray()
            }
        }
    }
}
