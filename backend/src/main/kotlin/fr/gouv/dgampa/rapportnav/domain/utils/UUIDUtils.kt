package fr.gouv.dgampa.rapportnav.domain.utils

import java.util.UUID

fun isValidUUID(uuid: String? = null): Boolean =
    uuid?.let { runCatching { UUID.fromString(it) }.isSuccess } == true

