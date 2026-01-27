package fr.gouv.dgampa.rapportnav.domain.exceptions

/**
 * Error code thrown when the request is valid but the backend cannot process it.
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
 *
 * ### ⚠️ Important
 * **Don't forget to mirror any update here in the corresponding Frontend enum.**
 */
enum class BackendUsageErrorCode {
    PASSWORD_TOO_WEAK_EXCEPTION,
    INCORRECT_USER_IDENTIFIER_EXCEPTION,

    //------------------------
    INVALID_PARAMETERS_EXCEPTION,
    COULD_NOT_SAVE_EXCEPTION,
    COULD_NOT_FIND_EXCEPTION,
    COULD_NOT_DELETE_EXCEPTION,
    ALREADY_EXISTS_EXCEPTION,
    TOO_MANY_ROWS_EXCEPTION,

    //------------------------
    COULD_NOT_FIND_CONTROL_FOR_INFRACTION_EXCEPTION
}
