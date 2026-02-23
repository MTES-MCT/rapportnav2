package fr.gouv.dgampa.rapportnav.domain.exceptions

/**
 * Error codes for internal server errors (HTTP 500).
 *
 * These errors occur when an unexpected internal failure happens in the backend.
 *
 * ### Important
 * **Don't forget to mirror any update here in the corresponding Frontend enum.**
 */
enum class BackendInternalErrorCode {
    AUDIT_LOGGING_FAILED
}
