package fr.gouv.dgampa.rapportnav.infrastructure.exceptions

/**
 * Error code thrown when the request is invalid.
 *
 * ## Examples
 * - The request has missing or wrongly-typed properties.
 * - The request has valid properties but is made of illogical data.
 *
 * ## Logging
 * The related exception is logged as a warning on the Backend side in order to track any unexpected behavior.
 * It should definitely be logged on the Frontend side as an error.
 *
 * ## ⚠️ Important
 * **Don't forget to mirror any update here in the corresponding Frontend enum.**
 */
enum class BackendRequestErrorCode {
    /** Thrown when the request body ID doesn't match the ID in the request path. */
    BODY_MISSING_DATA,

}
