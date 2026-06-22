package fr.gouv.dgampa.rapportnav.domain.validation

/**
 * Blocking validation - violations throw exceptions and prevent saving.
 * Used for: date validity (end > start, within mission range), numeric constraints (@Min)
 */
interface ValidateThrowsBeforeSave
