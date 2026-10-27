package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo

/**
 * The PAM usage metrics stored on the PAM ship's row of mission_resource_usage. The PAM costs (operating / fuel) belong
 * to the mission, not to a resource, so they are not part of it and stay in mission_general_info only.
 */
data class PamResourceMetrics(
    val distanceInNauticalMiles: Double? = null,
    val consumedGOInLiters: Double? = null,
    val consumedFuelInLiters: Double? = null
) {
    fun hasAny(): Boolean = listOf(distanceInNauticalMiles, consumedGOInLiters, consumedFuelInLiters).any { it != null }

    /**
     * What upserting these metrics onto [existing] (the (mission, resource) row, or null when missing) amounts to.
     * Shared by the real write and the backfill's dry run so both report the same thing.
     */
    fun mergeOutcome(existing: ResourceUsageEntity?): PamMetricsMergeResult = when {
        existing == null -> if (hasAny()) PamMetricsMergeResult.CREATED else PamMetricsMergeResult.UNCHANGED
        fromResourceUsageEntity(existing) == this -> PamMetricsMergeResult.UNCHANGED
        else -> PamMetricsMergeResult.UPDATED
    }

    companion object {
        /**
         * From the legacy Float fields of mission_general_info. Converts through the decimal string so that
         * 12.3f becomes 12.3, not 12.300000190734863 (a plain toDouble() widens the binary float).
         */
        fun fromFloats(
            distanceInNauticalMiles: Float?,
            consumedGOInLiters: Float?,
            consumedFuelInLiters: Float?
        ) = PamResourceMetrics(
            distanceInNauticalMiles = distanceInNauticalMiles.toExactDouble(),
            consumedGOInLiters = consumedGOInLiters.toExactDouble(),
            consumedFuelInLiters = consumedFuelInLiters.toExactDouble()
        )

        /** The three PAM usage values as stored in mission_general_info. */
        fun fromMissionGeneralInfoEntity(generalInfo: MissionGeneralInfoEntity) = fromFloats(
            distanceInNauticalMiles = generalInfo.distanceInNauticalMiles,
            consumedGOInLiters = generalInfo.consumedGOInLiters,
            consumedFuelInLiters = generalInfo.consumedFuelInLiters
        )

        fun fromResourceUsageEntity(entity: ResourceUsageEntity) = PamResourceMetrics(
            distanceInNauticalMiles = entity.distanceInNauticalMiles,
            consumedGOInLiters = entity.consumedGOInLiters,
            consumedFuelInLiters = entity.consumedFuelInLiters
        )

        private fun Float?.toExactDouble(): Double? = this?.toString()?.toDouble()
    }
}

/** Outcome of upserting the PAM metrics of one (mission, resource) row. */
enum class PamMetricsMergeResult { CREATED, UPDATED, UNCHANGED }
