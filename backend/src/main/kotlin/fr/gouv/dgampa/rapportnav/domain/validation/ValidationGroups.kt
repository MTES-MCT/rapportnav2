package fr.gouv.dgampa.rapportnav.domain.validation

/**
 * Validation groups for Jakarta Bean Validation.
 *
 * These interfaces define when different validation rules apply:
 * - ValidateThrowsBeforeSave: Blocking validation (dates, numeric constraints) - always runs
 * - ValidateWhenMissionFinished: Required field checks - only when mission is finished
 */

/**
 * Blocking validation - violations throw exceptions and prevent saving.
 * Used for: date validity (end > start, within mission range), numeric constraints (@Min)
 */
interface ValidateThrowsBeforeSave

/**
 * Required field checks that ONLY apply when mission is FINISHED.
 * Use @NotNull(groups = [ValidateWhenMissionFinished::class]) to replace @MandatoryForStats
 */
interface ValidateWhenMissionFinished
