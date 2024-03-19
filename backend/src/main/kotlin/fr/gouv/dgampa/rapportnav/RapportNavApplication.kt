package fr.gouv.dgampa.rapportnav

import io.sentry.Sentry
import io.sentry.SentryOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication


@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
open class RapportNavApplication

fun main(args: Array<String>) {

    val ctx = runApplication<RapportNavApplication>(*args)

    val isSentryEnabled: String? = ctx.environment.getProperty("rapportnav.sentry.enabled")
    val sentryDsn: String? = ctx.environment.getProperty("rapportnav.sentry.dsn")

    if (isSentryEnabled == "true") {
        Sentry.init { options ->
            options.dsn = sentryDsn
            options.proxy = SentryOptions.Proxy(
                ctx.environment.getProperty("rapportnav.sentry.proxy.host"),
                ctx.environment.getProperty("rapportnav.sentry.proxy.port")
            )
            options.tracesSampleRate = 1.0
        }
    }

}
