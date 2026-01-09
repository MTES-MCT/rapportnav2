package fr.gouv.dgampa.rapportnav.config

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.module.kotlin.KotlinModule

//import tools.jackson.databind.cfg.DateTimeFeature
//import tools.jackson.datatype.jsr310.JavaTimeModule
//import tools.jackson.module.kotlin.KotlinModule

@Configuration
class JacksonConfig {


    @Bean
    fun jsonMapperBuilderCustomizer(): JsonMapperBuilderCustomizer {
        return JsonMapperBuilderCustomizer { builder ->
            builder.addModule(KotlinModule.Builder().build())
//            builder.addModule(JavaTimeModule())

            // If you wrote your own Geometry module/serializers, register it here:
//             builder.addModule(JtsM)

            builder.disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
            builder.disable(DateTimeFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            builder.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

//            builder.serializationInclusion(JsonInclude.Include.ALWAYS)
            builder.propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
        }
    }
}
