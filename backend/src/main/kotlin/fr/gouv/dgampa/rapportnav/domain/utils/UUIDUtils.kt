package fr.gouv.dgampa.rapportnav.domain.utils

import java.util.*

fun isValidUUID(uuid: Any?): Boolean {
    return when (uuid) {
        is UUID -> true
        is String -> try {
            UUID.fromString(uuid)
            true
        } catch (_: IllegalArgumentException) {
            false
        }

        else -> false
    }
}

/** The string parsed as a [UUID], or null when it is blank/absent or not a UUID (e.g. an env external id). */
fun String?.toUuidOrNull(): UUID? =
    this?.takeIf { isValidUUID(it) }?.let { UUID.fromString(it) }

