package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf

import java.awt.Color

/** DSFR color tokens (light theme), see https://github.com/GouvernementFR/dsfr */
object PdfColors {
    val TEXT_TITLE_GREY = Color(0x16, 0x16, 0x16)
    val TEXT_DEFAULT_GREY = Color(0x3A, 0x3A, 0x3A)
    val TEXT_MENTION_GREY = Color(0x66, 0x66, 0x66)
    val BLUE_FRANCE = Color(0x00, 0x00, 0x91)
    val BACKGROUND_ACTION_LOW_BLUE_FRANCE = Color(0xE3, 0xE3, 0xFD)
    val SUCCESS = Color(0x18, 0x75, 0x3C)
    // DSFR yellow-tournesol
    val BACKGROUND_CONTRAST_YELLOW = Color(0xFE, 0xEC, 0xC2)
    val TEXT_DEFAULT_YELLOW = Color(0x71, 0x60, 0x43)
    val ERROR = Color(0xCE, 0x05, 0x00)
    val BORDER_DEFAULT_GREY = Color(0xDD, 0xDD, 0xDD)
    val BACKGROUND_CONTRAST_GREY = Color(0xEE, 0xEE, 0xEE)
    val BACKGROUND_CONTRAST_INFO = Color(0xE8, 0xED, 0xFF)
    val BORDER_DEFAULT_INFO = Color(0x00, 0x63, 0xCB)
}
