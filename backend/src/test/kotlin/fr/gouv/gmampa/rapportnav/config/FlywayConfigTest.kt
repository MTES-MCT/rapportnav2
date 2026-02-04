package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.FlywayConfig
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.mock

class FlywayConfigTest {

    @Test
    fun `should create FlywayMigrationStrategy bean`() {
        val config = FlywayConfig()
        val strategy = config.flywayMigrationStrategy()
        assertNotNull(strategy)
    }

    @Test
    fun `should call repair before migrate`() {
        val config = FlywayConfig()
        val strategy = config.flywayMigrationStrategy()
        val flyway = mock(Flyway::class.java)

        strategy.migrate(flyway)

        val inOrder = inOrder(flyway)
        inOrder.verify(flyway).repair()
        inOrder.verify(flyway).migrate()
    }
}
