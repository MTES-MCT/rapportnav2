package fr.gouv.dgampa.rapportnav.config

import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryOptions
import io.sentry.Hint
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class SentryConfig {

    private val logger = LoggerFactory.getLogger(SentryConfig::class.java)

    @Value("\${sentry.dsn:}")
    private lateinit var sentryDsn: String

    @Value("\${sentry.environment:production}")
    private lateinit var sentryEnvironment: String

    @Value("\${sentry.debug:false}")
    private var sentryDebug: Boolean = false

    @Value("\${sentry.traces-sample-rate:0.1}")
    private var tracesSampleRate: Double = 0.1

    @PostConstruct
    fun initSentry() {
        logger.info(">>> SENTRY CONFIG: Initializing Sentry manually...")
        logger.info(">>> SENTRY CONFIG: Environment = '$sentryEnvironment'")

        if (sentryDsn.isBlank()) {
            logger.warn(">>> SENTRY CONFIG: DSN is blank, Sentry will NOT be initialized")
            return
        }

        // Only initialize if not already initialized
        if (!Sentry.isEnabled()) {
            Sentry.init { options: SentryOptions ->
                options.dsn = sentryDsn
                options.environment = sentryEnvironment
                options.isDebug = sentryDebug
                options.tracesSampleRate = tracesSampleRate
                options.proxy = SentryOptions.Proxy(
                    System.getenv("PROXY_HOST"),
                    System.getenv("PROXY_PORT"),
                )
            }
            logger.info(">>> SENTRY CONFIG: Sentry initialized manually, isEnabled = ${Sentry.isEnabled()}")
        } else {
            logger.info(">>> SENTRY CONFIG: Sentry was already initialized by auto-configuration")
        }
    }
}
