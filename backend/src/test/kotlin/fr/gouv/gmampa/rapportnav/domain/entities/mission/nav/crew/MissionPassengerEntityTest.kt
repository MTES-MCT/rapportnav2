package fr.gouv.gmampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerOrganization
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.MissionDatesOutput
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.domain.validation.WithinMissionDateRangeValidator
import jakarta.validation.ConstraintValidatorContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.time.LocalDate

@SpringBootTest(classes = [WithinMissionDateRangeValidator::class])
class MissionPassengerEntityTest {

    @Autowired
    private lateinit var withinMissionDateRangeValidator: WithinMissionDateRangeValidator

    @MockitoBean
    private lateinit var getMissionDates: GetMissionDates

    private lateinit var mockContext: ConstraintValidatorContext

    @BeforeEach
    fun setUp() {
        mockContext = mock(ConstraintValidatorContext::class.java)
        // Initialize the validator with annotation parameters
        val annotation = MissionPassengerEntity::class.java.getAnnotation(
            fr.gouv.dgampa.rapportnav.domain.validation.WithinMissionDateRange::class.java
        )
        withinMissionDateRangeValidator.initialize(annotation)
    }

    private fun createPassenger(
        missionId: Int = 100,
        startDate: LocalDate = LocalDate.of(2024, 1, 15),
        endDate: LocalDate = LocalDate.of(2024, 1, 20)
    ) = MissionPassengerEntity(
        id = 1,
        missionId = missionId,
        fullName = "Test Passenger",
        organization = MissionPassengerOrganization.OTHER,
        startDate = startDate,
        endDate = endDate
    )

    private fun setupMissionDates(missionStart: Instant?, missionEnd: Instant?) {
        `when`(getMissionDates.execute(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(
            MissionDatesOutput(
                startDateTimeUtc = missionStart,
                endDateTimeUtc = missionEnd
            )
        )
    }

    @Test
    fun `validation returns true when mission start is null`() {
        setupMissionDates(null, null)
        val passenger = createPassenger()

        val result = withinMissionDateRangeValidator.isValid(passenger, mockContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `validation returns true when passenger dates are within mission dates`() {
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-25T23:59:59Z")
        setupMissionDates(missionStart, missionEnd)

        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 15),
            endDate = LocalDate.of(2024, 1, 20)
        )

        val result = withinMissionDateRangeValidator.isValid(passenger, mockContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `validation returns true when passenger dates exactly match mission dates`() {
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-25T23:59:59Z")
        setupMissionDates(missionStart, missionEnd)

        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 10),
            endDate = LocalDate.of(2024, 1, 25)
        )

        val result = withinMissionDateRangeValidator.isValid(passenger, mockContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `validation returns false when passenger start date is before mission start`() {
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-25T23:59:59Z")
        setupMissionDates(missionStart, missionEnd)

        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 5),
            endDate = LocalDate.of(2024, 1, 15)
        )

        val result = withinMissionDateRangeValidator.isValid(passenger, mockContext)

        assertThat(result).isFalse()
    }

    @Test
    fun `validation returns false when passenger end date is after mission end`() {
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-25T23:59:59Z")
        setupMissionDates(missionStart, missionEnd)

        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 15),
            endDate = LocalDate.of(2024, 1, 30)
        )

        val result = withinMissionDateRangeValidator.isValid(passenger, mockContext)

        assertThat(result).isFalse()
    }

    @Test
    fun `validation returns false when passenger start date is after mission end`() {
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-25T23:59:59Z")
        setupMissionDates(missionStart, missionEnd)

        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 26),
            endDate = LocalDate.of(2024, 1, 28)
        )

        val result = withinMissionDateRangeValidator.isValid(passenger, mockContext)

        assertThat(result).isFalse()
    }

    @Test
    fun `validation returns true when mission has no end date and passenger is after mission start`() {
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")
        setupMissionDates(missionStart, null)

        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 15),
            endDate = LocalDate.of(2024, 2, 15)
        )

        val result = withinMissionDateRangeValidator.isValid(passenger, mockContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `validation returns false when mission has no end date but passenger is before mission start`() {
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")
        setupMissionDates(missionStart, null)

        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 5),
            endDate = LocalDate.of(2024, 1, 15)
        )

        val result = withinMissionDateRangeValidator.isValid(passenger, mockContext)

        assertThat(result).isFalse()
    }
}
