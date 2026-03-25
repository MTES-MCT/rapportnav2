package fr.gouv.dgampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum

/**
 * Represents validation errors for stats completeness.
 */
data class CompletenessForStatsErrorEntity(
    val field: String,
    val rule: String,
    val message: String
)

/**
 * Represents the completeness/validity status of an entity for statistics.
 *
 * @property status COMPLETE if all validation rules pass, INCOMPLETE otherwise
 * @property errors List of validation errors with field, rule, and message
 * @property sources Optional list of sources with missing/invalid data (for multi-source actions)
 */
data class CompletenessForStatsEntity(
    val status: CompletenessForStatsStatusEnum? = null,
    val sources: List<MissionSourceEnum>? = null,
    val errors: List<CompletenessForStatsErrorEntity> = emptyList()
) {
    /**
     * Whether the entity is ready for statistics reporting.
     */
    val isComplete: Boolean
        get() = status == CompletenessForStatsStatusEnum.COMPLETE

    companion object {
        fun complete() = CompletenessForStatsEntity(
            status = CompletenessForStatsStatusEnum.COMPLETE,
            errors = emptyList()
        )

        fun incomplete(errors: List<CompletenessForStatsErrorEntity>, sources: List<MissionSourceEnum>? = null) =
            CompletenessForStatsEntity(
                status = CompletenessForStatsStatusEnum.INCOMPLETE,
                errors = errors,
                sources = sources
            )
    }
}
