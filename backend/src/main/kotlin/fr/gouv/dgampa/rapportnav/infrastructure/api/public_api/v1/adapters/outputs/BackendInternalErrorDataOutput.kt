package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs

/**
 * API response body for internal errors (HTTP 500).
 *
 * Provides useful debugging info without exposing sensitive data like stack traces.
 * Full exception details are logged server-side and sent to Sentry.
 */
class BackendInternalErrorDataOutput(
    val message: String = "An internal error occurred.",
    val exception: String? = null,  // Exception class name, e.g., "IOException"
    val cause: String? = null       // Root cause message, e.g., "Connection refused"
)
