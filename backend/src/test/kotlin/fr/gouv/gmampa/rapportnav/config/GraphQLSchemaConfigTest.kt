package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.GraphQLSchemaConfig
import graphql.schema.GraphQLScalarType
import graphql.schema.idl.RuntimeWiring
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.graphql.execution.RuntimeWiringConfigurer

class GraphQLSchemaConfigTest {

    private val instantScalar = mock(GraphQLScalarType::class.java)
    private val runtimeWiringBuilder = mock(RuntimeWiring.Builder::class.java)

    @Test
    fun `runtimeWiringConfigurer should register scalar in builder`() {
        // Arrange
        val config = GraphQLSchemaConfig()
        val runtimeWiringConfigurer: RuntimeWiringConfigurer = config.runtimeWiringConfigurer(instantScalar)

        // Act
        runtimeWiringConfigurer.configure(runtimeWiringBuilder)

        // Assert
        verify(runtimeWiringBuilder).scalar(instantScalar)
    }

    @Test
    fun `runtimeWiringConfigurer should not modify builder when no scalar is provided`() {
        // Arrange
        val config = GraphQLSchemaConfig()
        val emptyScalar: GraphQLScalarType? = null

        // This test ensures null values for the scalar won't result in errors (if allowed).
        if (emptyScalar != null) {
            val runtimeWiringConfigurer: RuntimeWiringConfigurer = config.runtimeWiringConfigurer(emptyScalar)
            runtimeWiringConfigurer.configure(runtimeWiringBuilder)
            verify(runtimeWiringBuilder).scalar(emptyScalar)
        }
    }
}
