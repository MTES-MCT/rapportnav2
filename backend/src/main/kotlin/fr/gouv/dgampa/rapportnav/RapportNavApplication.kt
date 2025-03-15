package fr.gouv.dgampa.rapportnav

import com.github.tomakehurst.wiremock.WireMockServer
import fr.gouv.dgampa.rapportnav.config.WireMockConfig
import io.sentry.Sentry
import io.sentry.SentryOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching


@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
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
            println("WireMock server stopped")
        })
    }

    val isSentryEnabled: String? = ctx.environment.getProperty("sentry.enabled")
    val sentryDsn: String? = System.getenv("SENTRY_DSN")

    if (isSentryEnabled == "true") {
        Sentry.init { options ->
            options.environment = System.getenv("ENV_PROFILE")
            options.dsn = sentryDsn
            options.proxy = SentryOptions.Proxy(
                System.getenv("PROXY_HOST"),
                System.getenv("PROXY_PORT"),
            )
            options.tracesSampleRate = 1.0
        }
    }

}
