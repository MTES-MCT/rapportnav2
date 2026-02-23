package fr.gouv.dgampa.rapportnav.domain.exceptions

/**
 * Error codes for forbidden (HTTP 403) operations.
 *
 * These errors occur when a user is authenticated but not authorized to perform
 * the requested action.
 *
 * ### Important
 * **Don't forget to mirror any update here in the corresponding Frontend enum.**
 */
enum class BackendForbiddenErrorCode {
    UNAUTHORIZED_IMPERSONATION
}
