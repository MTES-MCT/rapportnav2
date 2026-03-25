package fr.gouv.dgampa.rapportnav.domain.validation

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.MissionDatesOutput
import jakarta.validation.ConstraintValidatorContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [WithinMissionDateRangeValidator::class])
class WithinMissionDateRangeValidatorTest {

    @Autowired
    private lateinit var withinMissionDateRangeValidator: WithinMissionDateRangeValidator

    @MockitoBean
    private lateinit var getMissionDates: GetMissionDates

    private lateinit var mockContext: ConstraintValidatorContext

    @WithinMissionDateRange(groups = [ValidateThrowsBeforeSave::class])
    data class TestEntity(
        val missionId: Int?,
        val ownerId: UUID? = null,
        val actionType: ActionType? = null,
        val startDateTimeUtc: Instant?,
        val endDateTimeUtc: Instant?
    )

    @BeforeEach
    fun setUp() {
        mockContext = mock(ConstraintValidatorContext::class.java)
        // Initialize the validator with annotation from TestEntity
        val annotation = TestEntity::class.java.getAnnotation(WithinMissionDateRange::class.java)
        withinMissionDateRangeValidator.initialize(annotation)
    }

    private fun setupMissionDates(missionStart: Instant?, missionEnd: Instant?) {
        `when`(getMissionDates.execute(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(
            MissionDatesOutput(
                startDateTimeUtc = missionStart,
                endDateTimeUtc = missionEnd
            )
        )
    }

    @Nested
    @DisplayName("When validating dates within mission range")
    inner class WithinRangeValidation {

        @Test
        @DisplayName("should be valid when action dates are within mission range")
        fun `should be valid when action dates are within mission range`() {
            val missionStart = Instant.parse("2024-01-01T00:00:00Z")
            val missionEnd = Instant.parse("2024-01-31T23:59:59Z")
            setupMissionDates(missionStart, missionEnd)

            val entity = TestEntity(
                missionId = 123,
                startDateTimeUtc = Instant.parse("2024-01-02T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z")
            )

            val result = withinMissionDateRangeValidator.isValid(entity, mockContext)

            assertTrue(result)
        }

        @Test
        @DisplayName("should be invalid when action start is before mission start")
        fun `should be invalid when action start is before mission start`() {
            val missionStart = Instant.parse("2024-01-01T00:00:00Z")
            val missionEnd = Instant.parse("2024-01-31T23:59:59Z")
            setupMissionDates(missionStart, missionEnd)

            val entity = TestEntity(
                missionId = 123,
                startDateTimeUtc = Instant.parse("2023-12-31T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z")
            )

            val result = withinMissionDateRangeValidator.isValid(entity, mockContext)

            assertFalse(result)
        }

        @Test
        @DisplayName("should be invalid when action start is after mission end")
        fun `should be invalid when action start is after mission end`() {
            val missionStart = Instant.parse("2024-01-01T00:00:00Z")
            val missionEnd = Instant.parse("2024-01-31T23:59:59Z")
            setupMissionDates(missionStart, missionEnd)

            val entity = TestEntity(
                missionId = 123,
                startDateTimeUtc = Instant.parse("2024-02-15T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-02-15T12:00:00Z")
            )

            val result = withinMissionDateRangeValidator.isValid(entity, mockContext)

            assertFalse(result)
        }

        @Test
        @DisplayName("should be invalid when action end is after mission end")
        fun `should be invalid when action end is after mission end`() {
            val missionStart = Instant.parse("2024-01-01T00:00:00Z")
            val missionEnd = Instant.parse("2024-01-31T23:59:59Z")
            setupMissionDates(missionStart, missionEnd)

            val entity = TestEntity(
                missionId = 123,
                startDateTimeUtc = Instant.parse("2024-01-30T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-02-01T12:00:00Z")
            )

            val result = withinMissionDateRangeValidator.isValid(entity, mockContext)

            assertFalse(result)
        }

        @Test
        @DisplayName("should be valid when action dates equal mission boundaries")
        fun `should be valid when action dates equal mission boundaries`() {
            val missionStart = Instant.parse("2024-01-01T00:00:00Z")
            val missionEnd = Instant.parse("2024-01-31T23:59:59Z")
            setupMissionDates(missionStart, missionEnd)

            val entity = TestEntity(
                missionId = 123,
                startDateTimeUtc = missionStart,
                endDateTimeUtc = missionEnd
            )

            val result = withinMissionDateRangeValidator.isValid(entity, mockContext)

            assertTrue(result)
        }
    }

    @Nested
    @DisplayName("When mission dates are not set")
    inner class MissingMissionDates {

        @Test
        @DisplayName("should be valid when mission start date is null (skip validation)")
        fun `should be valid when mission start date is null`() {
            setupMissionDates(null, Instant.parse("2024-01-31T23:59:59Z"))

            val entity = TestEntity(
                missionId = 123,
                startDateTimeUtc = Instant.parse("2024-01-02T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z")
            )

            val result = withinMissionDateRangeValidator.isValid(entity, mockContext)

            assertTrue(result)
        }

        @Test
        @DisplayName("should be valid when mission end date is null (open-ended mission)")
        fun `should be valid when mission end date is null`() {
            setupMissionDates(Instant.parse("2024-01-01T00:00:00Z"), null)

            val entity = TestEntity(
                missionId = 123,
                startDateTimeUtc = Instant.parse("2024-01-02T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-12-31T12:00:00Z")
            )

            val result = withinMissionDateRangeValidator.isValid(entity, mockContext)

            assertTrue(result)
        }

        @Test
        @DisplayName("should be invalid when action start is before mission start even without mission end")
        fun `should be invalid when action start is before mission start even without mission end`() {
            setupMissionDates(Instant.parse("2024-01-01T00:00:00Z"), null)

            val entity = TestEntity(
                missionId = 123,
                startDateTimeUtc = Instant.parse("2023-12-31T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z")
            )

            val result = withinMissionDateRangeValidator.isValid(entity, mockContext)

            assertFalse(result)
        }
    }

    @Nested
    @DisplayName("When action dates are null")
    inner class MissingActionDates {

        @Test
        @DisplayName("should be valid when action start date is null")
        fun `should be valid when action start date is null`() {
            val missionStart = Instant.parse("2024-01-01T00:00:00Z")
            val missionEnd = Instant.parse("2024-01-31T23:59:59Z")
            setupMissionDates(missionStart, missionEnd)

            val entity = TestEntity(
                missionId = 123,
                startDateTimeUtc = null,
                endDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z")
            )

            val result = withinMissionDateRangeValidator.isValid(entity, mockContext)

            assertTrue(result)
        }

        @Test
        @DisplayName("should be valid when action end date is null")
        fun `should be valid when action end date is null`() {
            val missionStart = Instant.parse("2024-01-01T00:00:00Z")
            val missionEnd = Instant.parse("2024-01-31T23:59:59Z")
            setupMissionDates(missionStart, missionEnd)

            val entity = TestEntity(
                missionId = 123,
                startDateTimeUtc = Instant.parse("2024-01-02T10:00:00Z"),
                endDateTimeUtc = null
            )

            val result = withinMissionDateRangeValidator.isValid(entity, mockContext)

            assertTrue(result)
        }
    }

    @Nested
    @DisplayName("When missionId is null")
    inner class MissingMissionId {

        @Test
        @DisplayName("should be valid when missionId is null (skip validation)")
        fun `should be valid when missionId is null`() {
            val entity = TestEntity(
                missionId = null,
                startDateTimeUtc = Instant.parse("2023-12-31T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-02T12:00:00Z")
            )

            val result = withinMissionDateRangeValidator.isValid(entity, mockContext)

            assertTrue(result)
        }
    }

    @Nested
    @DisplayName("When entity is null")
    inner class NullEntity {

        @Test
        @DisplayName("should be valid when entity is null")
        fun `should be valid when entity is null`() {
            val result = withinMissionDateRangeValidator.isValid(null, mockContext)

            assertTrue(result)
        }
    }
}
