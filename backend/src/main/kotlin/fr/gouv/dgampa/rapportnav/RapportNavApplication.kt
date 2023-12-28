package fr.gouv.dgampa.rapportnav

import io.sentry.Sentry
import io.sentry.SentryOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication


@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class RapportNavApplication

fun main(args: Array<String>) {

    val ctx = runApplication<RapportNavApplication>(*args)

    val isSentryEnabled: String? = ctx.environment.getProperty("sentry.enabled")
    val sentryDsn: String? = ctx.environment.getProperty("sentry.dsn")

    if (isSentryEnabled == "true") {
        Sentry.init { options ->
            options.dsn = sentryDsn
            options.proxy = SentryOptions.Proxy("172.27.229.197", "8090")
            options.tracesSampleRate = 1.0
        }
    }

    Sentry.captureMessage("Something went wrong2")

    try {
        throw Exception("This is a test.2")
    } catch (e: Exception) {
        Sentry.captureException(e)
    }
}
