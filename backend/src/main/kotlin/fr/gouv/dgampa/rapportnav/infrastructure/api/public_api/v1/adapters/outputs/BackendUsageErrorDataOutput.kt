package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode


/**
 * Error output to use when the request is valid but the backend cannot process it.
 *
 * It's called "usage" because this request likely comes from an end-user action that's no longer valid
 * which happens when their client data is not up-to-date with the backend.
 *
 * But it can also be a Frontend side bug.
 *
 * ## Examples
 * - A user tries to create a resource that has already been created.
 * - A user tries to delete a resource that doesn't exist anymore.
 *
 * ## Logging
 * The related exception is NOT logged on the Backend side.
 * It should be logged on the Frontend side IF it's unexpected (= Frontend bug),
 * it should rather display a comprehensible error message to the end-user.
 */
data class BackendUsageErrorDataOutput(
    val code: BackendUsageErrorCode,
    val data: Any? = null,
    val message: String? = null,
)
