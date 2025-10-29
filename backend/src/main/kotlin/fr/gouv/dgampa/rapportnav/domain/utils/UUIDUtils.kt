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

