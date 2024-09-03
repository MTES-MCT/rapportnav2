package fr.gouv.dgampa.rapportnav.config

import graphql.schema.GraphQLScalarType
import graphql.schema.idl.RuntimeWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer

/**
 * Configuration class that integrates custom scalars into the GraphQL schema.
 */
@Configuration
class GraphQLSchemaConfig {

    /**
     * Registers a custom `GraphQLScalarType`, such as `Instant`, into the GraphQL schema's runtime wiring.
     *
     * @param instantScalar The custom scalar type, typically an `Instant` scalar, to be registered.
     * @return A `RuntimeWiringConfigurer` that adds the scalar to the GraphQL schema wiring.
     */
    @Bean
    fun runtimeWiringConfigurer(instantScalar: GraphQLScalarType): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { builder: RuntimeWiring.Builder ->
            builder.scalar(instantScalar)
        }
    }
}
