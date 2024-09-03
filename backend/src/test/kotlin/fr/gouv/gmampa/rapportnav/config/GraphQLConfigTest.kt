package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.GraphQLConfig
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.Instant

class GraphQLConfigTest {
    private val config = GraphQLConfig()
    private val scalar = config.instantScalar()

    @Test
    fun `serialize should return ISO-8601 string from Instant`() {
        val instant = Instant.parse("2023-09-01T10:15:30Z")
        val result = scalar.coercing.serialize(instant)
        assertEquals("2023-09-01T10:15:30Z", result)
    }

    @Test
    fun `serialize should throw exception if input is not Instant`() {
        val exception = assertThrows(CoercingSerializeException::class.java) {
            scalar.coercing.serialize("not an Instant")
        }
        assertEquals("Expected an Instant object.", exception.message)
    }

    @Test
    fun `parseValue should return Instant from valid ISO-8601 string`() {
        val result = scalar.coercing.parseValue("2023-09-01T10:15:30Z")
        assertEquals(Instant.parse("2023-09-01T10:15:30Z"), result)
    }

    @Test
    fun `parseValue should return Instant from valid epoch millis`() {
        val result = scalar.coercing.parseValue(1693565730000L)
        assertEquals(Instant.ofEpochMilli(1693565730000L), result)
    }

    @Test
    fun `parseValue should throw exception for invalid string`() {
        val exception = assertThrows(CoercingParseValueException::class.java) {
            scalar.coercing.parseValue("invalid date")
        }
        assertEquals("Invalid ISO-8601 format", exception.message)
    }

    @Test
    fun `parseLiteral should return Instant from valid ISO-8601 string literal`() {
        val input = graphql.language.StringValue("2023-09-01T10:15:30Z")
        val result = scalar.coercing.parseLiteral(input)
        assertEquals(Instant.parse("2023-09-01T10:15:30Z"), result)
    }

    @Test
    fun `parseLiteral should throw exception for invalid literal`() {
        val input = graphql.language.StringValue("invalid date")
        val exception = assertThrows(CoercingParseLiteralException::class.java) {
            scalar.coercing.parseLiteral(input)
        }
        assertEquals("Invalid ISO-8601 format", exception.message)
    }
}
