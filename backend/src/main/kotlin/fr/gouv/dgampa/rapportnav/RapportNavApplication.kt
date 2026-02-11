package fr.gouv.dgampa.rapportnav

import com.github.tomakehurst.wiremock.WireMockServer
import fr.gouv.dgampa.rapportnav.config.WireMockConfig
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

private val logger = LoggerFactory.getLogger(RapportNavApplication::class.java)

@SpringBootApplication
@EnableCaching
open class RapportNavApplication

fun main(args: Array<String>) {

    val ctx = runApplication<RapportNavApplication>(*args)

    val isLocalProfile = ctx.environment.activeProfiles.contains("local")

    if (isLocalProfile) {
        var wireMockServer: WireMockServer? = null
        val wireMockConfig = WireMockConfig()
        wireMockServer = wireMockConfig.startWireMock()

        Runtime.getRuntime().addShutdownHook(Thread {
            wireMockServer.stop()
            logger.info("WireMock server stopped")
        })
    }
}
