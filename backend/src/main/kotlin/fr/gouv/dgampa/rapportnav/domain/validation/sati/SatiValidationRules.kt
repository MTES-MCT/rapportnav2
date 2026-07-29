package fr.gouv.dgampa.rapportnav.domain.validation.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.validation.RequiredFieldsValidator.Rule
import fr.gouv.dgampa.rapportnav.domain.validation.forNested


object SatiValidationRules {

    private const val M1_OR_M3_DESC = "module ∈ {M1, M3}"
    private val IS_M1_OR_M3: (SatiEntity) -> Boolean = {
        it.module == SatiModuleType.M1 || it.module == SatiModuleType.M3
    }

    val rules: List<Rule<SatiEntity>> = listOf<Rule<SatiEntity>>(
        Rule.always("resource.id", "La resource est requise") { it.resource?.id },
        Rule.always("inspectors[0].agentId", "L'inspecteur principal est requis") {
            it.inspectors?.firstOrNull()?.agentId
        }
    ) + VesselValidationRules.rules.forNested(
        fieldPathPrefix = "vessel",
        condition = IS_M1_OR_M3,
        conditionDescription = M1_OR_M3_DESC
    ) { sati: SatiEntity -> sati.vessel }
}
