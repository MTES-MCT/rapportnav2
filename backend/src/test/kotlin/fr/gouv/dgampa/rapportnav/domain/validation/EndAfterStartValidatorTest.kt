package fr.gouv.dgampa.rapportnav.domain.validation

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant

class EndAfterStartValidatorTest {

    private lateinit var validator: Validator

    @BeforeEach
    fun setUp() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @EndAfterStart(groups = [ValidateThrowsBeforeSave::class, ValidateThrowsBeforeSave::class])
    data class TestEntity(
        val startDateTimeUtc: Instant?,
        val endDateTimeUtc: Instant?
    )

    @Nested
    @DisplayName("When validating end date after start date")
    inner class EndAfterStartValidation {

        @Test
        @DisplayName("should be valid when end date is after start date")
        fun `should be valid when end date is after start date`() {
            val entity = TestEntity(
                startDateTimeUtc = Instant.parse("2024-01-01T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-01T12:00:00Z")
            )

            val violations = validator.validate(entity, ValidateThrowsBeforeSave::class.java)

            assertTrue(violations.isEmpty())
        }

        @Test
        @DisplayName("should be invalid when end date is before start date")
        fun `should be invalid when end date is before start date`() {
            val entity = TestEntity(
                startDateTimeUtc = Instant.parse("2024-01-01T12:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-01T10:00:00Z")
            )

            val violations = validator.validate(entity, ValidateThrowsBeforeSave::class.java)

            assertEquals(1, violations.size)
            assertEquals("La date de fin doit être postérieure à la date de début", violations.first().message)
        }

        @Test
        @DisplayName("should be invalid when end date equals start date")
        fun `should be invalid when end date equals start date`() {
            val sameTime = Instant.parse("2024-01-01T10:00:00Z")
            val entity = TestEntity(
                startDateTimeUtc = sameTime,
                endDateTimeUtc = sameTime
            )

            val violations = validator.validate(entity, ValidateThrowsBeforeSave::class.java)

            assertEquals(1, violations.size)
        }

        @Test
        @DisplayName("should be valid when start date is null")
        fun `should be valid when start date is null`() {
            val entity = TestEntity(
                startDateTimeUtc = null,
                endDateTimeUtc = Instant.parse("2024-01-01T10:00:00Z")
            )

            val violations = validator.validate(entity, ValidateThrowsBeforeSave::class.java)

            assertTrue(violations.isEmpty())
        }

        @Test
        @DisplayName("should be valid when end date is null")
        fun `should be valid when end date is null`() {
            val entity = TestEntity(
                startDateTimeUtc = Instant.parse("2024-01-01T10:00:00Z"),
                endDateTimeUtc = null
            )

            val violations = validator.validate(entity, ValidateThrowsBeforeSave::class.java)

            assertTrue(violations.isEmpty())
        }

        @Test
        @DisplayName("should be valid when both dates are null")
        fun `should be valid when both dates are null`() {
            val entity = TestEntity(
                startDateTimeUtc = null,
                endDateTimeUtc = null
            )

            val violations = validator.validate(entity, ValidateThrowsBeforeSave::class.java)

            assertTrue(violations.isEmpty())
        }
    }

    @Nested
    @DisplayName("When using validation groups")
    inner class ValidationGroups {

        @Test
        @DisplayName("should only validate when using ValidateThrowsBeforeSave group")
        fun `should only validate when using ValidateThrowsBeforeSave group`() {
            val entity = TestEntity(
                startDateTimeUtc = Instant.parse("2024-01-01T12:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-01T10:00:00Z")
            )

            // Without group - should not validate
            val violationsDefault = validator.validate(entity)
            assertTrue(violationsDefault.isEmpty())

            // With ValidateThrowsBeforeSave group - should validate
            val violationsWithGroup = validator.validate(entity, ValidateThrowsBeforeSave::class.java)
            assertEquals(1, violationsWithGroup.size)
        }
    }
}
