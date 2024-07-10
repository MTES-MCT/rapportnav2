package fr.gouv.dgampa.rapportnav.domain.use_cases.utils

import fr.gouv.dgampa.rapportnav.config.UseCase
import org.apache.commons.text.StringEscapeUtils

@UseCase
class EncodeSpecialChars {

    /**
     * Some characters need to be escaped in a XML compliant manner
     * most notably the following ones:  &, <, >
     */
    fun escapeForXML(input: String?): String {
        return StringEscapeUtils.escapeXml10(input)
    }
}
