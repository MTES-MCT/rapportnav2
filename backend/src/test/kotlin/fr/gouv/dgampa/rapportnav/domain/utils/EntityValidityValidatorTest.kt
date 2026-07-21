package fr.gouv.dgampa.rapportnav.domain.utils

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.validation.EndAfterStart
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies
import fr.gouv.dgampa.rapportnav.domain.validation.WithinMissionDateRange
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class EntityValidityValidatorTest {

    private val validator = EntityValidityValidator.createDefault()

    @EndAfterStart(groups = [ValidateThrowsBeforeSave::class])
    @WithinMissionDateRange(groups = [ValidateThrowsBeforeSave::class])
    data class TestActionEntity(
        val id: String?,
        val startDateTimeUtc: Instant?,
        val endDateTimeUtc: Instant?,
        val missionStartDateTimeUtc: Instant?,
        val missionEndDateTimeUtc: Instant?
    )

    @Nested
    @DisplayName("When validating with ValidateThrowsBeforeSave group")
    inner class ValidateThrowsBeforeSaveValidation {

        @Test
        @DisplayName("should be valid when all date constraints pass")
        fun `should be valid when all date constraints pass`() {
            val entity = TestActionEntity(
                id = "123",
                startDateTimeUtc = Instant.parse("2024-01-02T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z"),
                missionStartDateTimeUtc = Instant.parse("2024-01-01T00:00:00Z"),
                missionEndDateTimeUtc = Instant.parse("2024-01-31T23:59:59Z")
            )

            val result = validator.validate(entity, ValidateThrowsBeforeSave::class.java)

            assertEquals(CompletenessForStatsStatusEnum.VALID, result.status)
            assertTrue(result.errors.isEmpty())
            assertTrue(result.isComplete)
        }

        @Test
        @DisplayName("should be invalid when end date is before start date")
        fun `should be invalid when end date is before start date`() {
            val entity = TestActionEntity(
                id = "123",
                startDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-02T10:00:00Z"),
                missionStartDateTimeUtc = Instant.parse("2024-01-01T00:00:00Z"),
                missionEndDateTimeUtc = Instant.parse("2024-01-31T23:59:59Z")
            )

            val result = validator.validate(entity, ValidateThrowsBeforeSave::class.java)

            assertEquals(CompletenessForStatsStatusEnum.INVALID, result.status)
            assertFalse(result.errors.isEmpty())
            assertEquals("EndAfterStart", result.errors.first().rule)
            assertFalse(result.isComplete)
        }
    }

    @Nested
    @DisplayName("When validating completeness with policy")
    inner class ValidateCompletenessTest {

        @Test
        @DisplayName("should be valid when all required fields present")
        fun `should be valid when all required fields present`() {
            val entity = MissionNavActionEntityMock.create(
                id = UUID.randomUUID(),
                missionId = 1,
                actionType = ActionType.OTHER,
                startDateTimeUtc = Instant.now(),
                endDateTimeUtc = Instant.now().plusSeconds(3600)
            )

            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertEquals(CompletenessForStatsStatusEnum.VALID, result.status)
            assertTrue(result.isComplete)
        }

        @Test
        @DisplayName("should be incomplete when required field is missing")
        fun `should be incomplete when required field is missing`() {
            val entity = MissionNavActionEntityMock.create(
                id = UUID.randomUUID(),
                missionId = 1,
                actionType = ActionType.OTHER,
                startDateTimeUtc = null
            )

            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertEquals(CompletenessForStatsStatusEnum.INCOMPLETE, result.status)
            assertTrue(result.errors.any { it.field == "startDateTimeUtc" })
        }
    }

    @Nested
    @DisplayName("When validating completeness with source")
    inner class ValidateCompletenessWithSourceTest {

        @Test
        @DisplayName("should include source in result when incomplete")
        fun `should include source in result when incomplete`() {
            val entity = MissionNavActionEntityMock.create(
                id = UUID.randomUUID(),
                missionId = 1,
                actionType = ActionType.OTHER,
                startDateTimeUtc = null
            )

            val result = validator.validateCompletenessWithSource(
                entity,
                MissionSourceEnum.RAPPORT_NAV,
                ValidationPolicies.v1
            )

            assertEquals(CompletenessForStatsStatusEnum.INCOMPLETE, result.status)
            assertEquals(listOf(MissionSourceEnum.RAPPORT_NAV), result.sources)
        }

        @Test
        @DisplayName("should not include source when valid")
        fun `should not include source when valid`() {
            val entity = MissionNavActionEntityMock.create(
                id = UUID.randomUUID(),
                missionId = 1,
                actionType = ActionType.OTHER,
                startDateTimeUtc = Instant.now(),
                endDateTimeUtc = Instant.now().plusSeconds(3600)
            )

            val result = validator.validateCompletenessWithSource(
                entity,
                MissionSourceEnum.RAPPORT_NAV,
                ValidationPolicies.v1
            )

            assertEquals(CompletenessForStatsStatusEnum.VALID, result.status)
            assertNull(result.sources)
        }
    }

    @Nested
    @DisplayName("When merging results")
    inner class MergeResultsTest {

        @Test
        @DisplayName("should merge multiple incomplete results")
        fun `should merge multiple incomplete results`() {
            val entity = MissionNavActionEntityMock.create(
                id = UUID.randomUUID(),
                missionId = 1,
                actionType = ActionType.OTHER,
                startDateTimeUtc = null
            )

            val result1 = validator.validateCompletenessWithSource(
                entity,
                MissionSourceEnum.RAPPORT_NAV,
                ValidationPolicies.v1
            )

            val result2 = validator.validateCompletenessWithSource(
                entity,
                MissionSourceEnum.MONITORENV,
                ValidationPolicies.v1
            )

            val merged = EntityValidityValidator.merge(result1, result2)

            assertEquals(CompletenessForStatsStatusEnum.INCOMPLETE, merged.status)
            assertTrue(merged.sources?.contains(MissionSourceEnum.RAPPORT_NAV) == true)
            assertTrue(merged.sources?.contains(MissionSourceEnum.MONITORENV) == true)
        }
    }
}
