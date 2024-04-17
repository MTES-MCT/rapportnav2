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

    val isSentryEnabled: String? = ctx.environment.getProperty("sentry.enabled")
    val sentryDsn: String? = ctx.environment.getProperty("sentry.dsn")

    if (isSentryEnabled == "true") {
        Sentry.init { options ->
            options.dsn = sentryDsn
            options.proxy = SentryOptions.Proxy(
                ctx.environment.getProperty("host.proxy.host"),
                ctx.environment.getProperty("host.proxy.port")
            )
            options.tracesSampleRate = 1.0
        }
    }

}
