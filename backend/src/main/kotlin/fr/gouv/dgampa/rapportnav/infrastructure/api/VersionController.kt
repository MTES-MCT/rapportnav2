package fr.gouv.dgampa.rapportnav.infrastructure.api

import org.springframework.boot.info.BuildProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class VersionController(val buildProperties: BuildProperties) {

    @GetMapping("/version")
    fun version(): Map<String, String> {
        return mapOf(
            "version" to buildProperties.version,
            "commit" to buildProperties["commit.hash"],
        )
    }
}
