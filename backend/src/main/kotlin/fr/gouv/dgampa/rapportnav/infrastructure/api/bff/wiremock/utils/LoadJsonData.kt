package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.utils

import java.nio.file.Files
import java.nio.file.Paths

object LoadJsonData {
    fun load(path: String): String {
        val path = Paths.get("src/main/resources/wiremock/$path")
        return String(Files.readAllBytes(path))
    }
}
