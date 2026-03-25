package fr.gouv.dgampa.rapportnav.domain.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * Utility functions for validating dates against mission date ranges.
 */
object MissionDateUtils {

    /**
     * Validates that dates (as Instant) fall within the mission date range.
     *
     * @param startDate The start date to validate
     * @param endDate The end date to validate (optional)
     * @param missionStart The mission start date
     * @param missionEnd The mission end date (optional for ongoing missions)
     * @return true if dates are within range, false otherwise
     */
    fun isWithinMissionDates(
        startDate: Instant?,
        endDate: Instant?,
        missionStart: Instant?,
        missionEnd: Instant?
    ): Boolean {
        if (missionStart == null) return true
        if (startDate == null) return true

        if (startDate.isBefore(missionStart)) return false

        if (missionEnd != null) {
            if (startDate.isAfter(missionEnd)) return false
            if (endDate != null && endDate.isAfter(missionEnd)) return false
        }

        return true
    }

    /**
     * Validates that dates (as LocalDate) fall within the mission date range.
     * Mission dates are provided as Instant and converted to LocalDate for comparison.
     *
     * @param startDate The start date to validate
     * @param endDate The end date to validate
     * @param missionStart The mission start date (as Instant)
     * @param missionEnd The mission end date (as Instant, optional for ongoing missions)
     * @return true if dates are within range, false otherwise
     */
    fun isWithinMissionDates(
        startDate: LocalDate?,
        endDate: LocalDate?,
        missionStart: Instant?,
        missionEnd: Instant?
    ): Boolean {
        if (missionStart == null) return true
        if (startDate == null) return true

        val missionStartDate = missionStart.atZone(ZoneOffset.UTC).toLocalDate()

        if (startDate.isBefore(missionStartDate)) return false

        if (missionEnd != null) {
            val missionEndDate = missionEnd.atZone(ZoneOffset.UTC).toLocalDate()
            if (startDate.isAfter(missionEndDate)) return false
            if (endDate != null && endDate.isAfter(missionEndDate)) return false
        }

        return true
    }
}
