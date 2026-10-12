package fr.gouv.dgampa.rapportnav.domain.validation.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AddressEntity
import fr.gouv.dgampa.rapportnav.domain.validation.RequiredFieldsValidator.Rule
import fr.gouv.dgampa.rapportnav.domain.validation.RequiredFieldsValidator.Rule.Companion.conditional

object AddressValidationRules {
    private val HAS_STRUCTURED_ADDRESS: (AddressEntity) -> Boolean =
        { it.street != null && it.country != null && it.zipcode != null && it.town != null }

    val rules: List<Rule<AddressEntity>> = listOf(
        Rule.always("id", "L'identifiant est requis") { it.id },
        conditional(
            "fullAddress",
            "L'adresse complète est requise en l'absence de rue, pays, code postal et ville",
            "street, country, zipcode et town ne sont pas tous renseignés",
            { !HAS_STRUCTURED_ADDRESS(it) }
        ) { it.fullAddress },
        conditional(
            "street",
            "La rue est requise en l'absence d'adresse complète",
            "fullAddress n'est pas renseignée",
            { it.fullAddress == null }
        ) { it.street },
        conditional(
            "town",
            "La ville est requise en l'absence d'adresse complète",
            "fullAddress n'est pas renseignée",
            { it.fullAddress == null }
        ) { it.town },
        conditional(
            "country",
            "Le pays est requis en l'absence d'adresse complète",
            "fullAddress n'est pas renseignée",
            { it.fullAddress == null }
        ) { it.country },
        conditional(
            "zipcode",
            "Le code postal est requis en l'absence d'adresse complète",
            "fullAddress n'est pas renseignée",
            { it.fullAddress == null }
        ) { it.zipcode }
    )
}
