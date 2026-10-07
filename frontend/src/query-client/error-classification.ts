/**
 * HTTP-status classification for API error handling.
 *
 * Kept as pure, dependency-free functions so they can be unit-tested in isolation, without
 * importing the query-client's heavy dependency graph (offline hooks, mutation defaults, …).
 */

/**
 * Expected, user-correctable API errors that should NOT be reported to Sentry:
 * - 400: usage errors (stale client data, invalid input)
 * - 403: authorization denials (object-level cloisonnement / insufficient privilege)
 *
 * 401 (unauthenticated / expired session) is handled upstream by the axios interceptor
 * (global logout) and never reaches the query-client error handler.
 */
export const isUsageError = (status: number | undefined): boolean => status === 400 || status === 403

/** Server errors (5xx) get a generic user message instead of the backend-provided detail. */
export const isServerError = (status: number | undefined): boolean => status !== undefined && status >= 500

/** Report to Sentry for anything that is not an expected usage error (5xx, network, unexpected). */
export const shouldReportToSentry = (status: number | undefined): boolean => !isUsageError(status)
