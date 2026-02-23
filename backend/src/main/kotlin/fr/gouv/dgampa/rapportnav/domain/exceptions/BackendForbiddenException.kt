package fr.gouv.dgampa.rapportnav.domain.exceptions

/**
 * Domain exception to throw when a user is authenticated but not authorized
 * to perform the requested action.
 *
 * Returns HTTP 403 Forbidden.
 *
 * ## Examples
 * - A non-admin user tries to impersonate another service.
 * - A user tries to access a resource they don't have permission for.
 *
 * ## Logging
 * This exception is logged as a warning on the Backend side for security monitoring.
 */
open class BackendForbiddenException(
    val code: BackendForbiddenErrorCode,
    final override val message: String? = null,
    val data: Any? = null,
) : RuntimeException(code.name)
