package fr.gouv.dgampa.rapportnav.domain.validation.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AddressEntity
import fr.gouv.dgampa.rapportnav.domain.validation.RequiredFieldsValidator.Rule

object AddressValidationRules {
    val rules: List<Rule<AddressEntity>> = listOf(
        Rule.always("town", "La ville est requise") { it.town },
        Rule.always("id", "L'identifiant est requis") { it.id },
        Rule.always("street", "La rue est requise") { it.street },
        Rule.always("country", "Le pays est requis") { it.country },
        Rule.always("zipcode", "Le code postal est requis") { it.zipcode }
    )
}
