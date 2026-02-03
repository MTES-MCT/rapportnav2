package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.JpaConfig
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

class JpaConfigTest {

    @Test
    fun `should be instantiable`() {
        val config = JpaConfig()
        assertNotNull(config)
    }
}
