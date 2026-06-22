package fr.gouv.dgampa.rapportnav.domain.validation

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import java.io.File
import java.time.LocalDate

/**
 * Generates a markdown documentation file from the validation policies.
 *
 * Run: ./gradlew generateValidationDocs
 * Or run the main function directly from your IDE.
 */
fun main() {
    val markdown = generateValidationRulesMarkdown()
    val outputFile = File("../docs/engineering/concepts/validation-rules.md")
    outputFile.writeText(markdown)
    println("Generated ${outputFile.absolutePath}")
}

private data class FieldEntry(val field: String, val extraCondition: String?)

fun generateValidationRulesMarkdown(): String {
    val sb = StringBuilder()
    appendHeader(sb)

    for (policy in ValidationPolicies.all) {
        sb.appendLine("---")
        sb.appendLine()
        sb.appendLine("# Policy v${policy.version}: ${policy.label}")
        sb.appendLine()
        sb.appendLine("Applies from: `${policy.appliesFrom}`")
        sb.appendLine()

        appendRulesPerEntity(sb, policy)
        appendRulesPerActionType(sb, policy)
    }

    return sb.toString()
}

private fun appendHeader(sb: StringBuilder) {
    sb.appendLine("# Regles de validation des champs requis")
    sb.appendLine()
    sb.appendLine("> **Fichier genere automatiquement** a partir du code source.")
    sb.appendLine("> Ne pas modifier manuellement. Lancer le generateur pour mettre a jour :")
    sb.appendLine("> `./gradlew generateValidationDocs`")
    sb.appendLine(">")
    sb.appendLine("> Derniere generation : ${LocalDate.now()}")
    sb.appendLine()
    sb.appendLine("Ces regles sont evaluees pour la completude statistique.")
    sb.appendLine("La politique applicable depend de la date de debut de la mission.")
    sb.appendLine()
}

private fun appendRulesPerEntity(sb: StringBuilder, policy: ValidationPolicy) {
    for ((entityClass, rules) in policy.rules) {
        sb.appendLine("## ${entityClass.simpleName}")
        sb.appendLine()
        sb.appendLine("| Champ | Condition | Message d'erreur |")
        sb.appendLine("|-------|-----------|------------------|")

        val groupedByField = rules.groupBy { it.field }
        for ((field, fieldRules) in groupedByField) {
            val condition = fieldRules.joinToString(" **OU** ") { it.conditionDescription }
            sb.appendLine("| `$field` | $condition | ${fieldRules.first().message} |")
        }

        sb.appendLine()
    }
}

private fun appendRulesPerActionType(sb: StringBuilder, policy: ValidationPolicy) {
    val navRules = policy.rules[MissionNavActionEntity::class.java] ?: return

    sb.appendLine("## Champs requis par type d'action")
    sb.appendLine()

    val actionTypes = collectActionTypes(navRules)

    for (actionType in actionTypes.sortedBy { it.name }) {
        val fields = collectFieldsForActionType(actionType, navRules)
        appendActionTypeTable(sb, actionType, fields)
    }
}

private fun collectActionTypes(rules: List<RequiredFieldsValidator.Rule<*>>): Set<ActionType> {
    val actionTypes = mutableSetOf<ActionType>()
    for (rule in rules) {
        if (rule is RequiredFieldsValidator.Rule.ForActionTypes) actionTypes.addAll(rule.actionTypes)
        if (rule is RequiredFieldsValidator.Rule.Conditional<*>) rule.relatedActionTypes?.let { actionTypes.addAll(it) }
    }
    return actionTypes
}

private fun collectFieldsForActionType(
    actionType: ActionType,
    rules: List<RequiredFieldsValidator.Rule<*>>
): List<FieldEntry> {
    val fields = mutableListOf<FieldEntry>()

    for (rule in rules) {
        if (!ruleMatchesActionType(rule, actionType)) continue
        val extra = (rule as? RequiredFieldsValidator.Rule.Conditional<*>)?.extraCondition
        fields.add(FieldEntry(rule.field, extra))
    }

    return fields
        .groupBy { it.field }
        .map { (field, entries) ->
            val extras = entries.mapNotNull { it.extraCondition }.distinct()
            FieldEntry(field, extras.joinToString(" / ").ifEmpty { null })
        }
}

private fun ruleMatchesActionType(rule: RequiredFieldsValidator.Rule<*>, actionType: ActionType): Boolean {
    return when (rule) {
        is RequiredFieldsValidator.Rule.Always<*> -> true
        is RequiredFieldsValidator.Rule.ForActionTypes -> actionType in rule.actionTypes
        is RequiredFieldsValidator.Rule.Conditional<*> -> rule.relatedActionTypes?.contains(actionType) == true
    }
}

private fun appendActionTypeTable(sb: StringBuilder, actionType: ActionType, fields: List<FieldEntry>) {
    sb.appendLine("### $actionType")
    sb.appendLine()
    sb.appendLine("| Champ | Condition supplementaire |")
    sb.appendLine("|-------|-------------------------|")
    for (entry in fields) {
        sb.appendLine("| `${entry.field}` | ${entry.extraCondition ?: "-"} |")
    }
    sb.appendLine()
}
