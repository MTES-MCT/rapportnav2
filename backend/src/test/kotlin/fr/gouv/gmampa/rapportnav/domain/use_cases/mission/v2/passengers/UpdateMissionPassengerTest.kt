package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2.passengers

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionPassengerRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.UpdateMissionPassenger
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.gmampa.rapportnav.mocks.mission.passenger.MissionPassengerEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.LocalDate


@SpringBootTest(classes = [UpdateMissionPassenger::class])
class UpdateMissionPassengerTest {

    @MockitoBean
    private lateinit var repo: IMissionPassengerRepository

    @MockitoBean
    private lateinit var entityValidityValidator: EntityValidityValidator

    @Autowired
    private lateinit var useCase: UpdateMissionPassenger

    @BeforeEach
    fun setup() {
        // Default: validation passes
        doNothing().whenever(entityValidityValidator).validateAndThrow(anyOrNull(), anyOrNull())
    }

    @Test
    fun `should update a passenger when dates are valid`() {
        val passenger = MissionPassengerEntityMock.create(
            startDate = LocalDate.of(2024, 1, 15),
            endDate = LocalDate.of(2024, 1, 20)
        )
        Mockito.`when`(repo.save(any())).thenReturn(passenger.toMissionPassengerModel())

        val response = useCase.execute(passenger = passenger)

        assertThat(response).isNotNull()
    }

    @Test
    fun `should throw exception when passenger start date is before mission start`() {
        val passenger = MissionPassengerEntityMock.create(
            startDate = LocalDate.of(2024, 1, 5),
            endDate = LocalDate.of(2024, 1, 15)
        )

        doThrow(
            BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Erreur de validation",
                data = mapOf("fieldErrors" to listOf(
                    mapOf("field" to "_class", "message" to "Les dates doivent être comprises dans les dates de la mission", "rule" to "WithinMissionDateRange")
                ))
            )
        ).whenever(entityValidityValidator).validateAndThrow(anyOrNull(), anyOrNull())

        val exception = assertThrows<BackendUsageException> {
            useCase.execute(passenger = passenger)
        }
        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION)
    }

    @Test
    fun `should throw exception when passenger end date is after mission end`() {
        val passenger = MissionPassengerEntityMock.create(
            startDate = LocalDate.of(2024, 1, 15),
            endDate = LocalDate.of(2024, 1, 30)
        )

        doThrow(
            BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Erreur de validation",
                data = mapOf("fieldErrors" to listOf(
                    mapOf("field" to "_class", "message" to "Les dates doivent être comprises dans les dates de la mission", "rule" to "WithinMissionDateRange")
                ))
            )
        ).whenever(entityValidityValidator).validateAndThrow(anyOrNull(), anyOrNull())

        val exception = assertThrows<BackendUsageException> {
            useCase.execute(passenger = passenger)
        }
        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION)
    }

    @Test
    fun `should throw exception when end date is before start date`() {
        val passenger = MissionPassengerEntityMock.create(
            startDate = LocalDate.of(2024, 1, 20),
            endDate = LocalDate.of(2024, 1, 15)
        )

        doThrow(
            BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Erreur de validation",
                data = mapOf("fieldErrors" to listOf(
                    mapOf("field" to "_class", "message" to "La date de fin doit être postérieure à la date de début", "rule" to "EndAfterStart")
                ))
            )
        ).whenever(entityValidityValidator).validateAndThrow(anyOrNull(), anyOrNull())

        val exception = assertThrows<BackendUsageException> {
            useCase.execute(passenger = passenger)
        }
        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION)
    }
}
