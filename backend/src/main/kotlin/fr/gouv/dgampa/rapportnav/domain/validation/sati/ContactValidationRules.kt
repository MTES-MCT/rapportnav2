package fr.gouv.dgampa.rapportnav.domain.validation.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.ContactEntity
import fr.gouv.dgampa.rapportnav.domain.validation.RequiredFieldsValidator.Rule
import fr.gouv.dgampa.rapportnav.domain.validation.forNested

object ContactValidationRules {
    val rules: List<Rule<ContactEntity>> = listOf<Rule<ContactEntity>>(
        Rule.always("id", "L'identifiant est requis") { it.id },
        Rule.always("fullName", "Le nom complet est requis") { it.fullName },
        Rule.always("nationality", "La nationalité est requise") { it.nationality },
        Rule.always("email", "L'e-mail est requis") { it.email },
        Rule.always("phone", "Le téléphone est requis") { it.phone }
    ) + AddressValidationRules.rules.forNested("address") { contact: ContactEntity ->
        contact.address
    }
}
