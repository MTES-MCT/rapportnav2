package fr.gouv.dgampa.rapportnav.domain.validation.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEstablishmentEntity
import fr.gouv.dgampa.rapportnav.domain.validation.RequiredFieldsValidator.Rule
import fr.gouv.dgampa.rapportnav.domain.validation.RequiredFieldsValidator.Rule.Companion.conditional
import fr.gouv.dgampa.rapportnav.domain.validation.forNested

object EstablishmentValidationRules {

    val rules: List<Rule<SatiEstablishmentEntity>> = listOf<Rule<SatiEstablishmentEntity>>(
        Rule.always("id", "L'identifiant est requis") { it.id },
        Rule.always("type", "Le type d'établissement est requis") { it.type },
        conditional(
            "subType",
            "Le sous-type d'établissement est requis",
            "type = GMS",
            { it.type == SectorEstablishmentType.GMS }
        ) { it.subType }
    ) + ContactValidationRules.rules.forNested("owner.contact") { establishment: SatiEstablishmentEntity ->
        establishment.owner?.contact
    }
}
