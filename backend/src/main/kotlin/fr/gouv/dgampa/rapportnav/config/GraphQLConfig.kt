package fr.gouv.dgampa.rapportnav.config

import graphql.language.StringValue
import graphql.schema.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.format.DateTimeParseException

/**
 * Configuration class to define custom GraphQL scalar types.
 */
@Configuration
class GraphQLConfig {

    /**
     * Defines a custom GraphQL scalar type for Java's `Instant` class.
     *
     * @return A `GraphQLScalarType` representing the `Instant` type.
     * The scalar handles the serialization and deserialization of
     * `Instant` values from ISO-8601 formatted strings or long epoch timestamps.
     */
    @Bean
    fun instantScalar(): GraphQLScalarType {
        return GraphQLScalarType.newScalar()
            .name("Instant")
            .description("Java Instant scalar")
            .coercing(object : Coercing<Instant, String> {

                /**
                 * Serializes an `Instant` object into a string for GraphQL responses.
                 *
                 * @param dataFetcherResult The `Instant` object to serialize.
                 * @return The serialized `Instant` as an ISO-8601 formatted string.
                 * @throws CoercingSerializeException If the input is not an `Instant` object.
                 */
                override fun serialize(dataFetcherResult: Any): String {
                    return (dataFetcherResult as? Instant)?.toString()
                        ?: throw CoercingSerializeException("Expected an Instant object.")
                }

                /**
                 * Parses a value (from a variable or direct input) into an `Instant` object.
                 * Accepts ISO-8601 formatted strings or long epoch timestamps.
                 *
                 * @param input The input value to parse.
                 * @return The parsed `Instant` object.
                 * @throws CoercingParseValueException If the input is not a valid String or Long,
                 *         or if it cannot be parsed into an `Instant`.
                 */
                override fun parseValue(input: Any): Instant {
                    return try {
                        when (input) {
                            is String -> Instant.parse(input)
                            is Long -> Instant.ofEpochMilli(input)
                            else -> throw CoercingParseValueException("Expected a String or Long")
                        }
                    } catch (e: DateTimeParseException) {
                        throw CoercingParseValueException("Invalid ISO-8601 format", e)
                    }
                }

                /**
                 * Parses a literal value (from a GraphQL query) into an `Instant` object.
                 * The input must be an ISO-8601 formatted string.
                 *
                 * @param input The literal value from the GraphQL query.
                 * @return The parsed `Instant` object.
                 * @throws CoercingParseLiteralException If the input is not a valid ISO-8601 formatted string,
                 *         or cannot be parsed into an `Instant`.
                 */
                override fun parseLiteral(input: Any): Instant {
                    val value = (input as? StringValue)?.value
                        ?: throw CoercingParseLiteralException("Expected an ISO-8601 formatted String")
                    return try {
                        Instant.parse(value)
                    } catch (e: DateTimeParseException) {
                        throw CoercingParseLiteralException("Invalid ISO-8601 format", e)
                    }
                }
            })
            .build()
    }
}
