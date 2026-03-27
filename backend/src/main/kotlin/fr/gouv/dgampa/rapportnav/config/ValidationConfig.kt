package fr.gouv.dgampa.rapportnav.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

/**
 * Configures Jakarta Bean Validation to use Spring's dependency injection.
 * This allows @Autowired in ConstraintValidator implementations
 * (e.g., WithinMissionDateRangeValidator can inject GetMissionDates).
 */
@Configuration
class ValidationConfig {

    @Bean
    fun validator(): LocalValidatorFactoryBean {
        return LocalValidatorFactoryBean()
    }
}
