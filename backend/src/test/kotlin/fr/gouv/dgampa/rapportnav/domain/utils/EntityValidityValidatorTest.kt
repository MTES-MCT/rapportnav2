package fr.gouv.dgampa.rapportnav.domain.utils

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.validation.EndAfterStart
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateWhenMissionFinished
import fr.gouv.dgampa.rapportnav.domain.validation.WithinMissionDateRange
import jakarta.validation.constraints.NotNull
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant

class EntityValidityValidatorTest {

    @EndAfterStart(groups = [ValidateThrowsBeforeSave::class])
    @WithinMissionDateRange(groups = [ValidateThrowsBeforeSave::class])
    data class TestActionEntity(
        @field:NotNull(groups = [ValidateWhenMissionFinished::class])
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

            val result = EntityValidityValidator.validateStatic(entity, ValidateThrowsBeforeSave::class.java)

            assertEquals(CompletenessForStatsStatusEnum.COMPLETE, result.status)
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

            val result = EntityValidityValidator.validateStatic(entity, ValidateThrowsBeforeSave::class.java)

            assertEquals(CompletenessForStatsStatusEnum.INCOMPLETE, result.status)
            assertFalse(result.errors.isEmpty())
            assertEquals("EndAfterStart", result.errors.first().rule)
            assertFalse(result.isComplete)
        }

        @Test
        @DisplayName("should NOT check required fields with ValidateThrowsBeforeSave group")
        fun `should NOT check required fields with ValidateThrowsBeforeSave group`() {
            val entity = TestActionEntity(
                id = null, // This would fail ValidateWhenMissionFinished but not ValidateThrowsBeforeSave
                startDateTimeUtc = Instant.parse("2024-01-02T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z"),
                missionStartDateTimeUtc = Instant.parse("2024-01-01T00:00:00Z"),
                missionEndDateTimeUtc = Instant.parse("2024-01-31T23:59:59Z")
            )

            val result = EntityValidityValidator.validateStatic(entity, ValidateThrowsBeforeSave::class.java)

            assertEquals(CompletenessForStatsStatusEnum.COMPLETE, result.status)
        }
    }

    @Nested
    @DisplayName("When validating with ValidateWhenMissionFinished group")
    inner class ValidateWhenMissionFinishedValidation {

        @Test
        @DisplayName("should check required fields with ValidateWhenMissionFinished group")
        fun `should check required fields with ValidateWhenMissionFinished group`() {
            val entity = TestActionEntity(
                id = null, // Missing required field
                startDateTimeUtc = Instant.parse("2024-01-02T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z"),
                missionStartDateTimeUtc = Instant.parse("2024-01-01T00:00:00Z"),
                missionEndDateTimeUtc = Instant.parse("2024-01-31T23:59:59Z")
            )

            val result = EntityValidityValidator.validateStatic(entity, ValidateWhenMissionFinished::class.java)

            assertEquals(CompletenessForStatsStatusEnum.INCOMPLETE, result.status)
            assertFalse(result.errors.isEmpty())
            assertEquals("id", result.errors.first().field)
        }
    }

    @Nested
    @DisplayName("When validating with multiple groups")
    inner class MultipleGroupsValidation {

        @Test
        @DisplayName("should validate all groups together")
        fun `should validate all groups together`() {
            val entity = TestActionEntity(
                id = null, // Fails ValidateWhenMissionFinished
                startDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-02T10:00:00Z"), // Fails ValidateThrowsBeforeSave
                missionStartDateTimeUtc = Instant.parse("2024-01-01T00:00:00Z"),
                missionEndDateTimeUtc = Instant.parse("2024-01-31T23:59:59Z")
            )

            val result = EntityValidityValidator.validateStatic(
                entity,
                ValidateThrowsBeforeSave::class.java,
                ValidateWhenMissionFinished::class.java
            )

            assertEquals(CompletenessForStatsStatusEnum.INCOMPLETE, result.status)
            assertEquals(2, result.errors.size)
        }
    }

    @Nested
    @DisplayName("When validating with source")
    inner class ValidateWithSourceTest {

        @Test
        @DisplayName("should include source in result when invalid")
        fun `should include source in result when invalid`() {
            val entity = TestActionEntity(
                id = "123",
                startDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-02T10:00:00Z"),
                missionStartDateTimeUtc = Instant.parse("2024-01-01T00:00:00Z"),
                missionEndDateTimeUtc = Instant.parse("2024-01-31T23:59:59Z")
            )

            val result = EntityValidityValidator.validateWithSourceStatic(
                entity,
                MissionSourceEnum.RAPPORT_NAV,
                ValidateThrowsBeforeSave::class.java
            )

            assertEquals(CompletenessForStatsStatusEnum.INCOMPLETE, result.status)
            assertEquals(listOf(MissionSourceEnum.RAPPORT_NAV), result.sources)
        }

        @Test
        @DisplayName("should not include source when valid")
        fun `should not include source when valid`() {
            val entity = TestActionEntity(
                id = "123",
                startDateTimeUtc = Instant.parse("2024-01-02T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z"),
                missionStartDateTimeUtc = Instant.parse("2024-01-01T00:00:00Z"),
                missionEndDateTimeUtc = Instant.parse("2024-01-31T23:59:59Z")
            )

            val result = EntityValidityValidator.validateWithSourceStatic(
                entity,
                MissionSourceEnum.RAPPORT_NAV,
                ValidateThrowsBeforeSave::class.java
            )

            assertEquals(CompletenessForStatsStatusEnum.COMPLETE, result.status)
            assertNull(result.sources)
        }
    }

    @Nested
    @DisplayName("When merging results")
    inner class MergeResultsTest {

        @Test
        @DisplayName("should merge multiple invalid results")
        fun `should merge multiple invalid results`() {
            val entity1 = TestActionEntity(
                id = "123",
                startDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-02T10:00:00Z"),
                missionStartDateTimeUtc = Instant.parse("2024-01-01T00:00:00Z"),
                missionEndDateTimeUtc = Instant.parse("2024-01-31T23:59:59Z")
            )

            val result1 = EntityValidityValidator.validateWithSourceStatic(
                entity1,
                MissionSourceEnum.RAPPORT_NAV,
                ValidateThrowsBeforeSave::class.java
            )

            val result2 = EntityValidityValidator.validateWithSourceStatic(
                entity1,
                MissionSourceEnum.MONITORENV,
                ValidateThrowsBeforeSave::class.java
            )

            val merged = EntityValidityValidator.merge(result1, result2)

            assertEquals(CompletenessForStatsStatusEnum.INCOMPLETE, merged.status)
            assertEquals(2, merged.errors.size)
            assertTrue(merged.sources?.contains(MissionSourceEnum.RAPPORT_NAV) == true)
            assertTrue(merged.sources?.contains(MissionSourceEnum.MONITORENV) == true)
        }
    }
}
