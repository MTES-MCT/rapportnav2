package fr.gouv.dgampa.rapportnav.domain.ports

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity

/**
 * Renders the SATI document of a fish control action as PDF bytes.
 * Implemented in infrastructure so the domain stays free of any PDF library.
 */
interface ISatiPdfGenerator {
    fun generate(action: MissionFishActionEntity): ByteArray
}
