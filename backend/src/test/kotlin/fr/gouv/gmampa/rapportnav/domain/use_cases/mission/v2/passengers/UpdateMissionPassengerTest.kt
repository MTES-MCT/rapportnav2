package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2.passengers

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionPassengerRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.MissionDatesOutput
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.UpdateMissionPassenger
import fr.gouv.gmampa.rapportnav.mocks.mission.passenger.MissionPassengerEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.time.LocalDate


@SpringBootTest(classes = [UpdateMissionPassenger::class])
class UpdateMissionPassengerTest {

    @MockitoBean
    private lateinit var repo: IMissionPassengerRepository

    @MockitoBean
    private lateinit var getMissionDates: GetMissionDates

    @Autowired
    private lateinit var useCase: UpdateMissionPassenger

    @Test
    fun `should create a new passenger when dates are within mission range`() {
        val passenger = MissionPassengerEntityMock.create(
            startDate = LocalDate.of(2024, 1, 15),
            endDate = LocalDate.of(2024, 1, 20)
        )
        val missionDates = MissionDatesOutput(
            startDateTimeUtc = Instant.parse("2024-01-10T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-01-25T23:59:59Z")
        )
        Mockito.`when`(repo.save(any())).thenReturn(passenger.toMissionPassengerModel())
        Mockito.`when`(getMissionDates.execute(anyOrNull(), anyOrNull())).thenReturn(missionDates)

        useCase = UpdateMissionPassenger(
            repo = repo,
            getMissionDates = getMissionDates
        )

        val response = useCase.execute(passenger = passenger)
        assertThat(response).isNotNull()
    }

    @Test
    fun `should create a new passenger when mission has no dates`() {
        val passenger = MissionPassengerEntityMock.create()
        Mockito.`when`(repo.save(any())).thenReturn(passenger.toMissionPassengerModel())
        Mockito.`when`(getMissionDates.execute(anyOrNull(), anyOrNull())).thenReturn(null)

        useCase = UpdateMissionPassenger(
            repo = repo,
            getMissionDates = getMissionDates
        )

        val response = useCase.execute(passenger = passenger)
        assertThat(response).isNotNull()
    }

    @Test
    fun `should throw exception when passenger start date is before mission start`() {
        val passenger = MissionPassengerEntityMock.create(
            startDate = LocalDate.of(2024, 1, 5),
            endDate = LocalDate.of(2024, 1, 15)
        )
        val missionDates = MissionDatesOutput(
            startDateTimeUtc = Instant.parse("2024-01-10T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-01-25T23:59:59Z")
        )
        Mockito.`when`(getMissionDates.execute(anyOrNull(), anyOrNull())).thenReturn(missionDates)

        useCase = UpdateMissionPassenger(
            repo = repo,
            getMissionDates = getMissionDates
        )

        val exception = assertThrows<BackendUsageException> {
            useCase.execute(passenger = passenger)
        }
        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.DATES_OUTSIDE_MISSION_RANGE_EXCEPTION)
    }

    @Test
    fun `should throw exception when passenger end date is after mission end`() {
        val passenger = MissionPassengerEntityMock.create(
            startDate = LocalDate.of(2024, 1, 15),
            endDate = LocalDate.of(2024, 1, 30)
        )
        val missionDates = MissionDatesOutput(
            startDateTimeUtc = Instant.parse("2024-01-10T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-01-25T23:59:59Z")
        )
        Mockito.`when`(getMissionDates.execute(anyOrNull(), anyOrNull())).thenReturn(missionDates)

        useCase = UpdateMissionPassenger(
            repo = repo,
            getMissionDates = getMissionDates
        )

        val exception = assertThrows<BackendUsageException> {
            useCase.execute(passenger = passenger)
        }
        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.DATES_OUTSIDE_MISSION_RANGE_EXCEPTION)
    }

}
