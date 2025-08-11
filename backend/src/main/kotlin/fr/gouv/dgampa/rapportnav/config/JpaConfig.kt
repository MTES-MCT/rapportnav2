package fr.gouv.dgampa.rapportnav.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableJpaRepositories(basePackages = ["fr.gouv.dgampa.rapportnav.infrastructure.database.repositories"])
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@ComponentScan("fr.gouv.dgampa.rapportnav")
class JpaConfig
