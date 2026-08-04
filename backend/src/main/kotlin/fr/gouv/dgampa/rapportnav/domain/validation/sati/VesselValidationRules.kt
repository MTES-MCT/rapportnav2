package fr.gouv.dgampa.rapportnav.domain.validation.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiVesselEntity
import fr.gouv.dgampa.rapportnav.domain.validation.RequiredFieldsValidator.Rule
import fr.gouv.dgampa.rapportnav.domain.validation.RequiredFieldsValidator.Rule.Companion.conditional
import fr.gouv.dgampa.rapportnav.domain.validation.forNested

object VesselValidationRules {
    val rules: List<Rule<SatiVesselEntity>> = listOf<Rule<SatiVesselEntity>>(
        conditional(
            "jpe.tripNumber",
            "Le numéro de marée est requis en l'absence de PNO",
            "jpe.pnoId = null",
            { it.jpe?.pnoId == null  }
        ) { it.jpe?.tripNumber },
        conditional(
            "jpe.lastStopDate",
            "La date de dernière escale est requise",
            "jpe.lastPortIsNotSame = true",
            { it.jpe?.lastPortIsNotSame == true }
        ) { it.jpe?.lastStopDate },
        conditional(
            "jpe.portId",
            "Le port de dernière escale est requis",
            "jpe.lastPortIsNotSame = true",
            { it.jpe?.lastPortIsNotSame == true }
        ) { it.jpe?.portId }
    ) + ContactValidationRules.rules.forNested("master.contact") { vessel: SatiVesselEntity ->
        vessel.master?.contact
    }
}
