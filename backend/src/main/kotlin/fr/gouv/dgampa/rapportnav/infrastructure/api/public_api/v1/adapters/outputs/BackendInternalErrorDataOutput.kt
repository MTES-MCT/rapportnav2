package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs

/**
 * Domain exception to throw when an internal error occurred in the backend.
 *
 * ## Examples
 * - An unexpected exception has been caught.
 *
 * ## Logging
 * The related exception is logged as an error on the Backend side.
 */
class BackendInternalErrorDataOutput(
    val message: String = "An internal error occurred."
)
