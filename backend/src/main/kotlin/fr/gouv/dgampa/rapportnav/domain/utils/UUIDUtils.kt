package fr.gouv.dgampa.rapportnav.domain.utils

import java.util.UUID

fun isValidUUID(uuid: String? = null): Boolean {
    return try {
        uuid?.let { UUID.fromString(it) } != null
    } catch (e: IllegalArgumentException) {
        false
    }
}

