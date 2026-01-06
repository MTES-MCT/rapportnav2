package fr.gouv.dgampa.rapportnav.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.n52.jackson.datatype.jts.JtsModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class JacksonConfig {

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            // Register Kotlin module for better Kotlin support
            registerModule(KotlinModule.Builder().build())

            // Register JavaTimeModule for Java 8 date/time types
            // This handles Instant, ZonedDateTime, LocalDate automatically
            registerModule(JavaTimeModule())

            // Register JTS module for geometry types
            registerModule(JtsModule())

            // Configure to serialize dates as ISO-8601 strings (not timestamps)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

            // Include null values in serialization (equivalent to gson.serializeNulls())
            setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS)

            propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
        }
    }
}
